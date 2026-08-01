package comparators;

import car.Car;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorsTest {

    private static Car car1;
    private static Car car2;
    private static Car car3;
    private static Car car4;
    private static Car carNullModel1;
    private static Car carNullModel2;

    @BeforeAll
    static void setUp() {
        car1 = new Car.Builder()
                .setPower(150)
                .setModel("BMW X5")
                .setYear(2020)
                .build();

        car2 = new Car.Builder()
                .setPower(180)
                .setModel("Audi Q7")
                .setYear(2018)
                .build();

        car3 = new Car.Builder()
                .setPower(150)
                .setModel("Mercedes GLE")
                .setYear(2022)
                .build();

        car4 = new Car.Builder()
                .setPower(200)
                .setModel("BMW X5")
                .setYear(2020)
                .build();

        carNullModel1 = new Car.Builder()
                .setPower(100)
                .setModel(null)
                .setYear(2015)
                .build();

        carNullModel2 = new Car.Builder()
                .setPower(110)
                .setModel(null)
                .setYear(2016)
                .build();
    }

    @Test
    void testYearComparator() {
        Comparator<Car> comparator = new CarYearComparator();

        assertTrue(comparator.compare(car1, car2) > 0);
        assertTrue(comparator.compare(car2, car1) < 0);
        assertEquals(0, comparator.compare(car1, car4));
        assertTrue(comparator.compare(car3, car1) > 0);
    }

    @Test
    void testPowerComparator() {
        Comparator<Car> comparator = new CarPowerComparator();
        assertTrue(comparator.compare(car1, car2) < 0);
        assertTrue(comparator.compare(car2, car1) > 0);
        assertEquals(0, comparator.compare(car1, car3));
        assertTrue(comparator.compare(car4, car1) > 0);
    }

    @Test
    void testModelComparator() {
        Comparator<Car> comparator = new CarModelComparator();
        assertTrue(comparator.compare(car2, car1) < 0);
        assertTrue(comparator.compare(car1, car2) > 0);
        assertEquals(0, comparator.compare(car1, car4));
        assertTrue(comparator.compare(car3, car1) > 0);
        assertTrue(comparator.compare(carNullModel1, car1) < 0);
        assertTrue(comparator.compare(car1, carNullModel1) > 0);
        assertEquals(0, comparator.compare(carNullModel1, carNullModel2));
    }

    @Test
    void testYearEvenComparator() {
        Comparator<Car> comparator = new CarYearEvenComparator();
        assertTrue(comparator.compare(car1, car2) > 0);
        assertTrue(comparator.compare(car2, car1) < 0);
        assertEquals(0, comparator.compare(car1, car4));
        Car carOdd1 = new Car.Builder().setPower(100).setModel("A").setYear(2021).build();
        Car carOdd2 = new Car.Builder().setPower(200).setModel("B").setYear(2023).build();
        assertEquals(0, comparator.compare(carOdd1, carOdd2));
        assertEquals(0, comparator.compare(carOdd1, carOdd2));
        Car carEven = new Car.Builder().setPower(100).setModel("A").setYear(2020).build();
        Car carOdd = new Car.Builder().setPower(100).setModel("B").setYear(2021).build();
        assertEquals(0, comparator.compare(carEven, carOdd));
        assertEquals(0, comparator.compare(carOdd, carEven));
    }
    @Test
    void testPowerEvenComparator() {
        Comparator<Car> comparator = new CarPowerEvenComparator();
        assertTrue(comparator.compare(car1, car2) < 0);
        assertTrue(comparator.compare(car2, car1) > 0);
        assertEquals(0, comparator.compare(car1, car3));
        Car carOddPower1 = new Car.Builder().setPower(151).setModel("A").setYear(2020).build();
        Car carOddPower2 = new Car.Builder().setPower(153).setModel("B").setYear(2021).build();
        assertEquals(0, comparator.compare(carOddPower1, carOddPower2));
        Car carEvenPower = new Car.Builder().setPower(150).setModel("A").setYear(2020).build();
        Car carOddPower = new Car.Builder().setPower(151).setModel("B").setYear(2021).build();
        assertEquals(0, comparator.compare(carEvenPower, carOddPower));
        assertEquals(0, comparator.compare(carOddPower, carEvenPower));
    }
}