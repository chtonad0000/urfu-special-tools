package ru.urfu.specialtools.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import ru.urfu.specialtools.collection.DynamicArrayList;
import ru.urfu.specialtools.model.Car;

public final class CarFileReader {
    private static final String HEADER = "model;power;year";

    public List<Car> read(Path path) throws IOException {
        List<Car> cars = new DynamicArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            boolean firstRecord = true;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                line = line.strip();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (firstRecord && HEADER.equals(line)) {
                    firstRecord = false;
                    continue;
                }
                firstRecord = false;
                try {
                    cars.add(parseCar(line));
                } catch (IllegalArgumentException exception) {
                    throw new IllegalArgumentException(
                            "Line " + lineNumber + ": " + exception.getMessage(), exception);
                }
            }
        }
        return cars;
    }

    private Car parseCar(String line) {
        String[] fields = line.split(";", -1);
        if (fields.length != 3) {
            throw new IllegalArgumentException(
                    "Expected three fields: model, power, year");
        }
        return new Car(fields[0], parseInteger(fields[1], "Power"), parseInteger(fields[2], "Year of manufacture"));
    }

    private int parseInteger(String text, String field) {
        try {
            return Integer.parseInt(text.strip());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(field + ": an integer is required", exception);
        }
    }
}
