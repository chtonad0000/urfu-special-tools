package ru.urfu.specialtools.console;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Year;
import java.util.List;
import java.util.stream.IntStream;
import ru.urfu.specialtools.io.CarFileReader;
import ru.urfu.specialtools.model.Car;
import ru.urfu.specialtools.service.CarCatalog;

public final class ConsoleApplication {
    private final ConsoleInput input;
    private final PrintStream output;
    private final CarCatalog catalog;
    private final CarFileReader fileReader;
    private final Path defaultFile;

    public ConsoleApplication(ConsoleInput input, PrintStream output, CarCatalog catalog,
                              CarFileReader fileReader, Path defaultFile) {
        this.input = input;
        this.output = output;
        this.catalog = catalog;
        this.fileReader = fileReader;
        this.defaultFile = defaultFile;
    }

    public void run() throws IOException {
        output.println("SpecialTools: car catalog");
        while (true) {
            printMenu();
            try {
                int command = input.readInt("Select a menu option: ", 0, 7);
                switch (command) {
                    case 0 -> {
                        output.println("Goodbye");
                        return;
                    }
                    case 1 -> addManually();
                    case 2 -> loadFromFile();
                    case 3 -> printCars();
                    case 4 -> printReport();
                    case 5 -> printCar();
                    case 6 -> removeCar();
                    case 7 -> output.println("Total cars: " + catalog.size());
                    default -> throw new IllegalArgumentException("Unknown command");
                }
            } catch (IllegalArgumentException exception) {
                output.println("Error: " + exception.getMessage());
            }
        }
    }

    private void printMenu() {
        output.println("""

                ===== MENU =====
                1. Add a car manually
                2. Load cars from a file
                3. Show all cars
                4. Process data using Stream API
                5. Get a car by its list number
                6. Remove a car by its list number
                7. Show collection size
                0. Exit
                """);
    }

    private void addManually() throws IOException {
        String model = input.readText("Car model: ");
        int power = input.readInt("Power (hp, positive integer): ", 1, Integer.MAX_VALUE);
        int currentYear = Year.now().getValue();
        int year = input.readInt("Year of manufacture (" + Car.MIN_YEAR + "-" + currentYear + "): ",
                Car.MIN_YEAR, currentYear);
        catalog.add(new Car(model, power, year));
        output.println("Car added");
    }

    private void loadFromFile() throws IOException {
        String value = input.readText("CSV/TXT file path (Enter for " + defaultFile + "): ");
        Path path = value.isEmpty() ? defaultFile : Path.of(value);
        try {
            List<Car> imported = fileReader.read(path);
            catalog.addAll(imported);
            output.println("Cars loaded: " + imported.size());
        } catch (IOException | SecurityException exception) {
            output.println("Could not read the file: " + exception.getMessage());
        }
    }

    private void printCars() {
        if (isEmpty()) {
            return;
        }
        List<Car> cars = catalog.snapshot();
        output.println("Cars (" + cars.size() + "):");
        IntStream.range(0, cars.size())
                .mapToObj(index -> (index + 1) + ". " + cars.get(index))
                .forEach(output::println);
    }

    private void printReport() throws IOException {
        if (isEmpty()) {
            return;
        }
        int minimumPower = input.readInt("Minimum power (hp): ", 1, Integer.MAX_VALUE);
        List<String> report = catalog.createReport(minimumPower);
        output.println("Results sorted by power in descending order");
        if (report.isEmpty()) {
            output.println("No cars found with the specified minimum power");
        } else {
            report.forEach(output::println);
        }
    }

    private void printCar() throws IOException {
        if (!isEmpty()) {
            output.println(catalog.get(readIndex()));
        }
    }

    private void removeCar() throws IOException {
        if (!isEmpty()) {
            output.println("Car removed: " + catalog.remove(readIndex()));
        }
    }

    private int readIndex() throws IOException {
        return input.readInt("List number (1-" + catalog.size() + "): ", 1, catalog.size()) - 1;
    }

    private boolean isEmpty() {
        if (catalog.size() == 0) {
            output.println("The collection is empty, add cars first");
            return true;
        }
        return false;
    }
}
