package ru.urfu.specialtools.console;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;

public final class ConsoleInput {
    private final BufferedReader reader;
    private final PrintStream output;

    public ConsoleInput(BufferedReader reader, PrintStream output) {
        this.reader = reader;
        this.output = output;
    }

    public String readText(String prompt) throws IOException {
        output.print(prompt);
        output.flush();
        String line = reader.readLine();
        if (line == null) {
            throw new EOFException();
        }
        return line.strip();
    }

    public int readInt(String prompt, int minimum, int maximum) throws IOException {
        while (true) {
            try {
                int value = Integer.parseInt(readText(prompt));
                if (value >= minimum && value <= maximum) {
                    return value;
                }
                output.println("Enter an integer from " + minimum + " to " + maximum);
            } catch (NumberFormatException exception) {
                output.println("Error: an integer is required");
            }
        }
    }
}
