package org.course.homework.service;

import org.course.homework.service.interfaces.PrintService;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class PrintServiceWithStream implements PrintService {

    private final PrintStream outputStream;
    private final InputStream inputStream;

    public PrintServiceWithStream(PrintStream outputStream, InputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void printf(String format, Object... args) {
        outputStream.printf(format, args);
    }

    @Override
    public String readLine() {
        Scanner scanner = new Scanner(inputStream);
        return scanner.nextLine();
    }
}
