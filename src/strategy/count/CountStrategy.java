package strategy.count;

import Utils.LinkedList;
import car.Car;
import strategy.NamedStrategy;


public interface CountStrategy extends NamedStrategy {
    int count(LinkedList<Car> cars);
    String getSearchValue();
}
