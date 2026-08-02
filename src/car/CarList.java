package car;

import utils.LinkedList;

public class CarList extends LinkedList<Car> {
    public CarList() {
        super();
    }

    public CarList(java.util.Collection<Car> cars) {
        super();
        this.addAll(cars);
    }
}