package strategy;

import java.util.List;
import car.Car;

public interface InputStrategy extends NamedStrategy {
    List<Car> getCars();
}
