package car;

import java.util.ArrayList;
import java.util.List;

public class CarList extends ArrayList<Car> {
    public CarList() {
        super();
    }
    public CarList(int initialCapacity) {
        super(initialCapacity);
    }
    public CarList(List<Car> cars) {
        super(cars);
    }
}