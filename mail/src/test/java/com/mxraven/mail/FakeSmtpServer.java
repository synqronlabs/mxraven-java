package com.mxraven.mail;

import com.mxraven.mail.internal.Java8;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Minimal in-process SMTP server used by {@link SmtpClientTest}. It accepts a
 * single connection, speaks just enough of ESMTP for the client, and records
 * the conversation for assertions.
 */
final class FakeSmtpServer implements Closeable {
    private static final byte[] DATA_TERMINATOR = "\r\n.\r\n".getBytes(StandardCharsets.UTF_8);

    private final ServerSocket serverSocket;
    private final Thread worker;
    private final CountDownLatch finished = new CountDownLatch(1);
    private volatile Socket connection;

    private final boolean advertiseStartTls;
    private final boolean advertiseAuthPlain;
    private final boolean advertiseAuthLogin;
    private final int rcptCode;
    private final List<String> extraCapabilities;

    final List<String> commands = new CopyOnWriteArrayList<>();
    final List<String> recipients = new CopyOnWriteArrayList<>();

    volatile String greeting = "220 fake ESMTP ready";
    volatile String mailFrom;
    volatile String dataPayload;
    volatile String authUser;
    volatile String authPassword;
    volatile IOException failure;

    FakeSmtpServer() {
        this(false, false, false, 250);
    }

    FakeSmtpServer(boolean advertiseStartTls, boolean advertiseAuthPlain, boolean advertiseAuthLogin, int rcptCode) {
        this(advertiseStartTls, advertiseAuthPlain, advertiseAuthLogin, rcptCode, Java8.list());
    }

    FakeSmtpServer(boolean advertiseStartTls, boolean advertiseAuthPlain, boolean advertiseAuthLogin,
                   int rcptCode, List<String> extraCapabilities) {
        this.advertiseStartTls = advertiseStartTls;
        this.advertiseAuthPlain = advertiseAuthPlain;
        this.advertiseAuthLogin = advertiseAuthLogin;
        this.rcptCode = rcptCode;
        this.extraCapabilities = Java8.copyList(extraCapabilities);
        try {
            this.serverSocket = new ServerSocket();
            this.serverSocket.bind(new InetSocketAddress("127.0.0.1", 0));
        } catch (IOException e) {
            throw new IllegalStateException("cannot start fake SMTP server", e);
        }
        this.worker = new Thread(this::serve, "fake-smtp");
        this.worker.setDaemon(true);
        this.worker.start();
    }

    int port() {
        return serverSocket.getLocalPort();
    }

    private void serve() {
        try (Socket socket = serverSocket.accept()) {
            connection = socket;
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            writeLine(out, greeting);
            String line;
            while ((line = readLine(in)) != null) {
                commands.add(line);
                String upper = line.toUpperCase(Locale.ROOT);
                if (upper.startsWith("EHLO") || upper.startsWith("HELO")) {
                    writeCapabilities(out);
                } else if (upper.startsWith("MAIL FROM:")) {
                    mailFrom = line.substring("MAIL FROM:".length()).trim();
                    writeLine(out, "250 OK");
                } else if (upper.startsWith("RCPT TO:")) {
                    recipients.add(line.substring("RCPT TO:".length()).trim());
                    writeLine(out, rcptCode == 250 ? "250 OK" : rcptCode + " rejected");
                } else if (upper.startsWith("DATA")) {
                    writeLine(out, "354 End data with <CR><LF>.<CR><LF>");
                    dataPayload = new String(readData(in), StandardCharsets.UTF_8);
                    writeLine(out, "250 Queued");
                } else if (upper.startsWith("AUTH PLAIN")) {
                    decodePlain(line.substring("AUTH PLAIN".length()).trim());
                    writeLine(out, "235 Authenticated");
                } else if (upper.startsWith("AUTH LOGIN")) {
                    writeLine(out, "334 VXNlcm5hbWU6");
                    authUser = new String(Base64.getDecoder().decode(readLine(in)), StandardCharsets.UTF_8);
                    writeLine(out, "334 UGFzc3dvcmQ6");
                    authPassword = new String(Base64.getDecoder().decode(readLine(in)), StandardCharsets.UTF_8);
                    writeLine(out, "235 Authenticated");
                } else if (upper.startsWith("QUIT")) {
                    writeLine(out, "221 Bye");
                    break;
                } else {
                    writeLine(out, "250 OK");
                }
            }
        } catch (IOException e) {
            failure = e;
        } finally {
            finished.countDown();
        }
    }

    private void writeCapabilities(OutputStream out) throws IOException {
        List<String> caps = new ArrayList<>();
        caps.add("fake");
        caps.add("SIZE 10240000");
        caps.add("8BITMIME");
        if (advertiseAuthPlain || advertiseAuthLogin) {
            StringBuilder auth = new StringBuilder("AUTH");
            if (advertiseAuthPlain) {
                auth.append(" PLAIN");
            }
            if (advertiseAuthLogin) {
                auth.append(" LOGIN");
            }
            caps.add(auth.toString());
        }
        if (advertiseStartTls) {
            caps.add("STARTTLS");
        }
        caps.add("SMTPUTF8");
        caps.addAll(extraCapabilities);
        for (int i = 0; i < caps.size(); i++) {
            writeLine(out, "250" + (i == caps.size() - 1 ? " " : "-") + caps.get(i));
        }
    }

    private void decodePlain(String token) {
        String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
        String[] parts = decoded.split("\u0000", -1);
        authUser = parts.length > 1 ? parts[1] : null;
        authPassword = parts.length > 2 ? parts[2] : null;
    }

    private static void writeLine(OutputStream out, String value) throws IOException {
        out.write((value + "\r\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    private static String readLine(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            if (b == '\n') {
                break;
            }
            if (b != '\r') {
                buffer.write(b);
            }
        }
        if (b == -1 && buffer.size() == 0) {
            return null;
        }
        return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
    }

    private static byte[] readData(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            buffer.write(b);
            byte[] current = buffer.toByteArray();
            if (endsWith(current, DATA_TERMINATOR)) {
                byte[] payload = new byte[current.length - DATA_TERMINATOR.length];
                System.arraycopy(current, 0, payload, 0, payload.length);
                return payload;
            }
        }
        return buffer.toByteArray();
    }

    private static boolean endsWith(byte[] value, byte[] suffix) {
        if (value.length < suffix.length) {
            return false;
        }
        for (int i = 0; i < suffix.length; i++) {
            if (value[value.length - suffix.length + i] != suffix[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
        Socket open = connection;
        if (open != null) {
            open.close();
        }
        try {
            finished.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
