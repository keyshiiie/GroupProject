package test;

import Comparators.CarYearComparator;
import car.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarYearComparatorTest {

    private final CarYearComparator comparator = new CarYearComparator();

    @Test
    @DisplayName("Год раньше: результат отрицательный")
    void compare_earlierYear_returnsNegative() {
        var c1 = new Car.Builder().setModel("A").setPower(150).setYear(2018).build();
        var c2 = new Car.Builder().setModel("B").setPower(200).setYear(2020).build();

        assertTrue(comparator.compare(c1, c2) < 0);
    }

    @Test
    @DisplayName("Год позже: результат положительный")
    void compare_laterYear_returnsPositive() {
        var c1 = new Car.Builder().setModel("A").setPower(150).setYear(2020).build();
        var c2 = new Car.Builder().setModel("B").setPower(200).setYear(2018).build();

        assertTrue(comparator.compare(c1, c2) > 0);
    }

    @Test
    @DisplayName("Одинаковый год: результат равен 0")
    void compare_equalYear_returnsZero() {
        var c1 = new Car.Builder().setModel("A").setPower(150).setYear(2019).build();
        var c2 = new Car.Builder().setModel("B").setPower(200).setYear(2019).build();

        assertEquals(0, comparator.compare(c1, c2));
    }
}