package strategy.count;

import car.Car;
import strategy.NamedStrategy;

import java.util.List;

public interface CountStrategy extends NamedStrategy {
    int count(List<Car> cars);
    String getSearchValue();
}
