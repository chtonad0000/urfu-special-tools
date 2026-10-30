package ru.urfu.specialtools.model;

import java.time.Year;

public final class Car {
    public static final int MIN_YEAR = 1886;

    private final String model;
    private final int power;
    private final int year;

    public Car(String model, int power, int year) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Car model must not be blank");
        }
        this.model = model.strip();
        if (this.model.indexOf(';') >= 0 || this.model.codePoints().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Car model contains an invalid character");
        }
        if (power <= 0) {
            throw new IllegalArgumentException("Power must be a positive integer");
        }
        int currentYear = Year.now().getValue();
        if (year < MIN_YEAR || year > currentYear) {
            throw new IllegalArgumentException(
                    "Year of manufacture must be between " + MIN_YEAR + " and " + currentYear);
        }
        this.power = power;
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public int getPower() {
        return power;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return model + " | power: " + power + " hp | year: " + year;
    }
}
