package strategy;

import java.util.List;
import Car.Car;

public interface InputStrategy extends NamedStrategy {
    List<Car> getCars();
}
