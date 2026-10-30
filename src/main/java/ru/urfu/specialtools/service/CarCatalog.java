package ru.urfu.specialtools.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import ru.urfu.specialtools.collection.DynamicArrayList;
import ru.urfu.specialtools.model.Car;

public final class CarCatalog {
    private final List<Car> cars = new DynamicArrayList<>();

    public void add(Car car) {
        cars.add(Objects.requireNonNull(car));
    }

    public void addAll(List<Car> imported) {
        imported.forEach(Objects::requireNonNull);
        cars.addAll(imported);
    }

    public Car get(int index) {
        return cars.get(index);
    }

    public Car remove(int index) {
        return cars.remove(index);
    }

    public int size() {
        return cars.size();
    }

    public List<Car> snapshot() {
        List<Car> result = new DynamicArrayList<>();
        result.addAll(cars);
        return result;
    }

    public List<String> createReport(int minimumPower) {
        return cars.stream()
                .filter(car -> car.getPower() >= minimumPower)
                .sorted(Comparator.comparingInt(Car::getPower).reversed())
                .map(Car::toString)
                .collect(Collectors.toCollection(DynamicArrayList::new));
    }
}
