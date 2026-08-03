package car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarListTest {

    private CarList carList;
    private Car car1;
    private Car car2;
    private Car car3;

    @BeforeEach
    void setUp() {
        carList = new CarList();
        car1 = new Car.Builder()
                .setModel("Toyota")
                .setPower(150)
                .setYear(2020)
                .build();
        car2 = new Car.Builder()
                .setModel("BMW")
                .setPower(200)
                .setYear(2021)
                .build();
        car3 = new Car.Builder()
                .setModel("Audi")
                .setPower(180)
                .setYear(2019)
                .build();
    }

    @Test
    void testDefaultConstructor() {
        CarList emptyList = new CarList();
        assertNotNull(emptyList);
        assertEquals(0, emptyList.size());
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void testConstructorWithCollection() {
        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);

        CarList list = new CarList(cars);
        assertEquals(2, list.size());
        assertEquals(car1, list.get(0));
        assertEquals(car2, list.get(1));
    }

    @Test
    void testAddCar() {
        carList.add(car1);
        assertEquals(1, carList.size());
        assertEquals(car1, carList.get(0));
    }

    @Test
    void testAddAllCars() {
        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        cars.add(car3);

        carList.addAll(cars);
        assertEquals(3, carList.size());
        assertEquals(car1, carList.get(0));
        assertEquals(car2, carList.get(1));
        assertEquals(car3, carList.get(2));
    }

    @Test
    void testRemoveCar() {
        carList.add(car1);
        carList.add(car2);
        assertEquals(2, carList.size());

        carList.remove(car1);
        assertEquals(1, carList.size());
        assertEquals(car2, carList.get(0));
    }

    @Test
    void testGetCar() {
        carList.add(car1);
        carList.add(car2);

        assertEquals(car1, carList.get(0));
        assertEquals(car2, carList.get(1));
    }

    @Test
    void testGetIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> carList.get(0));
    }

    @Test
    void testClear() {
        carList.add(car1);
        carList.add(car2);
        assertEquals(2, carList.size());

        carList.clear();
        assertEquals(0, carList.size());
        assertTrue(carList.isEmpty());
    }

    @Test
    void testIsEmpty() {
        assertTrue(carList.isEmpty());
        carList.add(car1);
        assertFalse(carList.isEmpty());
    }

    @Test
    void testSize() {
        assertEquals(0, carList.size());
        carList.add(car1);
        assertEquals(1, carList.size());
        carList.add(car2);
        assertEquals(2, carList.size());
    }

    @Test
    void testContains() {
        carList.add(car1);
        carList.add(car2);

        assertTrue(carList.contains(car1));
        assertTrue(carList.contains(car2));
        assertFalse(carList.contains(car3));
    }

    @Test
    void testContainsAll() {
        carList.add(car1);
        carList.add(car2);

        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);

        assertTrue(carList.containsAll(cars));

        cars.add(car3);
        assertFalse(carList.containsAll(cars));
    }

    @Test
    void testToArray() {
        carList.add(car1);
        carList.add(car2);

        Object[] array = carList.toArray();
        assertEquals(2, array.length);
        assertEquals(car1, array[0]);
        assertEquals(car2, array[1]);
    }

    @Test
    void testIterator() {
        carList.add(car1);
        carList.add(car2);

        int count = 0;
        for (Car car : carList) {
            assertNotNull(car);
            count++;
        }
        assertEquals(2, count);
    }
}