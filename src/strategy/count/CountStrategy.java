package strategy.count;

import car.Car;
import car.CarList;
import strategy.NamedStrategy;

import java.util.List;

public interface CountStrategy extends NamedStrategy {
    int count(CarList cars);
    String getSearchValue();
}
