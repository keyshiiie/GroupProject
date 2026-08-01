package sort;

import car.Car;
import org.junit.jupiter.api.Test;
import strategy.sort.SortByPowerEvenStrategy;
import strategy.sort.SortByYearEvenStrategy;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SortEvenStrategiesTest {

    @Test
    void sortByYearEven_keepsOddsInPlace_sortsEvens() {
        var strategy = new SortByYearEvenStrategy();

        List<Car> cars = Arrays.asList(
                new Car.Builder().setModel("AAAAA").setPower(100).setYear(2016).build(),
                new Car.Builder().setModel("BBBBB").setPower(110).setYear(2013).build(),
                new Car.Builder().setModel("CCCCC").setPower(120).setYear(2014).build()
        );

        List<Car> sorted = strategy.sort(cars);

        assertEquals(2013, sorted.get(1).getYear(), "Нечётный год должен остаться на своей позиции");

        assertEquals(2014, sorted.get(0).getYear());
        assertEquals(2016, sorted.get(2).getYear());
    }

    @Test
    void sortByYearEven_multipleOdds_keepAllInPlace() {
        var strategy = new SortByYearEvenStrategy();

        List<Car> cars = Arrays.asList(
                new Car.Builder().setModel("AAAAA").setYear(2020).setPower(150).build(),
                new Car.Builder().setModel("BBBBB").setYear(2019).setPower(150).build(),
                new Car.Builder().setModel("CCCCC").setYear(2021).setPower(150).build(),
                new Car.Builder().setModel("DDDDD").setYear(2018).setPower(150).build(),
                new Car.Builder().setModel("EEEEE").setYear(2017).setPower(150).build(),
                new Car.Builder().setModel("FFFFF").setYear(2022).setPower(150).build()
        );

        List<Car> sorted = strategy.sort(cars);

        assertEquals(2019, sorted.get(1).getYear());
        assertEquals(2021, sorted.get(2).getYear());
        assertEquals(2017, sorted.get(4).getYear());

        assertEquals(2018, sorted.get(0).getYear());
        assertEquals(2020, sorted.get(3).getYear());
        assertEquals(2022, sorted.get(5).getYear());
    }

    @Test
    void sortByPowerEven_keepsOddsInPlace() {
        var strategy = new SortByPowerEvenStrategy();

        List<Car> cars = Arrays.asList(
                new Car.Builder().setModel("AAAAA").setPower(150).setYear(2000).build(),
                new Car.Builder().setModel("BBBBB").setPower(151).setYear(2001).build(),
                new Car.Builder().setModel("CCCCC").setPower(148).setYear(2002).build(),
                new Car.Builder().setModel("DDDDD").setPower(153).setYear(2003).build(),
                new Car.Builder().setModel("EEEEE").setPower(160).setYear(2004).build()
        );

        List<Car> sorted = strategy.sort(cars);

        assertEquals(151, sorted.get(1).getPower());
        assertEquals(153, sorted.get(3).getPower());

        // Чётные отсортированы: 148, 150, 160
        assertEquals(148, sorted.get(0).getPower());
        assertEquals(150, sorted.get(2).getPower());
        assertEquals(160, sorted.get(4).getPower());
    }

    @Test
    void evenStrategies_handleEmptyAndNull() {
        var powerEven = new SortByPowerEvenStrategy();
        var yearEven = new SortByYearEvenStrategy();

        assertTrue(powerEven.sort(null).isEmpty());
        assertTrue(powerEven.sort(List.of()).isEmpty());

        assertTrue(yearEven.sort(null).isEmpty());
        assertTrue(yearEven.sort(List.of()).isEmpty());
    }
}
