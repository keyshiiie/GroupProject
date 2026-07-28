package strategy;

import car.Car;

import java.util.List;

public interface CountStrategy extends NamedStrategy {
    int count(List<Car> cars);
}
