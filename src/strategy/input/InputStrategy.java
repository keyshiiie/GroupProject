package strategy.input;

import car.CarList;
import strategy.NamedStrategy;

public interface InputStrategy extends NamedStrategy {
    CarList setCars();
}
