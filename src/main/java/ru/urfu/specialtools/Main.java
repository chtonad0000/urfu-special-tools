package ru.urfu.specialtools;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import ru.urfu.specialtools.console.ConsoleApplication;
import ru.urfu.specialtools.console.ConsoleInput;
import ru.urfu.specialtools.io.CarFileReader;
import ru.urfu.specialtools.service.CarCatalog;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            Path defaultFile = Path.of(args.length == 0 ? "data/cars.csv" : args[0]);
            ConsoleInput input = new ConsoleInput(reader, System.out);
            ConsoleApplication application = new ConsoleApplication(
                    input, System.out, new CarCatalog(), new CarFileReader(), defaultFile);
            application.run();
        } catch (EOFException exception) {
            System.out.println("\nEnd of input, goodbye");
        } catch (IOException exception) {
            System.err.println("Error reading input: " + exception.getMessage());
        } catch (InvalidPathException exception) {
            System.err.println("Invalid file path: " + exception.getInput());
        }
    }
}
