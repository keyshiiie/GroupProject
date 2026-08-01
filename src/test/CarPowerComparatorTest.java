package test;

import Comparators.CarPowerComparator;
import car.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarPowerComparatorTest {

    private final CarPowerComparator comparator = new CarPowerComparator();

    @Test
    @DisplayName("Мощность меньше: результат отрицательный")
    void compare_lowerPower_returnsNegative() {
        var c1 = new Car.Builder().setModel("A").setPower(150).setYear(2020).build();
        var c2 = new Car.Builder().setModel("B").setPower(200).setYear(2019).build();

        assertTrue(comparator.compare(c1, c2) < 0);
    }

    @Test
    @DisplayName("Мощность больше: результат положительный")
    void compare_higherPower_returnsPositive() {
        var c1 = new Car.Builder().setModel("A").setPower(200).setYear(2020).build();
        var c2 = new Car.Builder().setModel("B").setPower(150).setYear(2019).build();

        assertTrue(comparator.compare(c1, c2) > 0);
    }

    @Test
    @DisplayName("Одинаковая мощность: результат равен 0")
    void compare_equalPower_returnsZero() {
        var c1 = new Car.Builder().setModel("A").setPower(180).setYear(2020).build();
        var c2 = new Car.Builder().setModel("B").setPower(180).setYear(2019).build();

        assertEquals(0, comparator.compare(c1, c2));
    }
}