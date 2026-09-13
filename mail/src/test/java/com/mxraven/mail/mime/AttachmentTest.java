package com.mxraven.mail.mime;

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
        assertEquals("hello", Files.readString(target));
    }

    @Test
    void downloadsIntoADirectoryUsingTheFilename(@TempDir Path directory) throws Exception {
        assertEquals(directory.resolve("notes.txt"), attachment().download(directory));
        assertEquals("hello", Files.readString(directory.resolve("notes.txt")));
    }

    @Test
    void exposesStreamAndOutputStream() throws Exception {
        assertEquals("hello",
                new String(attachment().openStream().readAllBytes(), StandardCharsets.UTF_8));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        attachment().writeTo(out);
        assertEquals("hello", out.toString(StandardCharsets.UTF_8));
    }
}
