package com.mxraven.mail.mime;

import com.mxraven.mail.internal.Java8;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttachmentTest {

    private static Attachment attachment() {
        return new Attachment("notes.txt", "text/plain", false, null,
                "hello".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void downloadsToAnExplicitFile(@TempDir Path directory) throws Exception {
        Path target = directory.resolve("out.txt");

        assertEquals(target, attachment().download(target));
        assertEquals("hello", new String(Files.readAllBytes(target), StandardCharsets.UTF_8));
    }

    @Test
    void downloadsIntoADirectoryUsingTheFilename(@TempDir Path directory) throws Exception {
        assertEquals(directory.resolve("notes.txt"), attachment().download(directory));
        assertEquals("hello", new String(Files.readAllBytes(directory.resolve("notes.txt")), StandardCharsets.UTF_8));
    }

    @Test
    void exposesStreamAndOutputStream() throws Exception {
        assertEquals("hello",
                new String(Java8.readAllBytes(attachment().openStream()), StandardCharsets.UTF_8));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        attachment().writeTo(out);
        assertEquals("hello", new String(out.toByteArray(), StandardCharsets.UTF_8));
    }
}
