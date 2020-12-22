package org.course.homework.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс PrintServiceWithStream")
class PrintServiceWithStreamTest {

    @Test
    void printf(){
        String input = "Some input";
        String output = "Some output";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        PrintServiceWithStream service = new PrintServiceWithStream(printStream, inputStream);
        service.printf(output);
        assertEquals(output, new String(outputStream.toByteArray()));
    }

    @Test
    void readLine() {
        String input = "Some input";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        PrintStream printStream = new PrintStream(outputStream);

        PrintServiceWithStream service = new PrintServiceWithStream(printStream, inputStream);
        assertEquals(input, service.readLine());
    }

}