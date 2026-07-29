package strategy.input;

import java.util.List;
import car.Car;
import strategy.NamedStrategy;

public interface InputStrategy extends NamedStrategy {
    List<Car> getCars();
}
