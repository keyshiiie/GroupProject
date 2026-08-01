package test;

import Comparators.CarModelComparator;
import car.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarModelComparatorTest {

    private final CarModelComparator comparator = new CarModelComparator();

    @Test
    @DisplayName("Сравнение двух не-null моделей: лексикографический порядок")
    void compare_twoNonNullModels_lexicographical() {
        var c1 = new Car.Builder().setModel("AudiA").setPower(200).setYear(2020).build(); // >=5 символов
        var c2 = new Car.Builder().setModel("BMWBB").setPower(300).setYear(2019).build();

        assertTrue(comparator.compare(c1, c2) < 0);
        assertTrue(comparator.compare(c2, c1) > 0);
    }

    @Test
    @DisplayName("Одинаковые модели: результат сравнения равен 0")
    void compare_sameModel_returnsZero() {
        var c1 = new Car.Builder().setModel("Tesla3").setPower(250).setYear(2021).build(); // без спецсимволов и >=5
        var c2 = new Car.Builder().setModel("Tesla3").setPower(280).setYear(2022).build();

        assertEquals(0, comparator.compare(c1, c2));
    }
}
