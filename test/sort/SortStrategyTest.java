package sort;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.sort.SortByModelStrategy;
import strategy.sort.SortByPowerStrategy;
import strategy.sort.SortByYearStrategy;

import static org.junit.jupiter.api.Assertions.*;

class SortStrategyTest {

    private SortByModelStrategy modelStrategy;
    private SortByPowerStrategy powerStrategy;
    private SortByYearStrategy yearStrategy;

    @BeforeEach
    void setUp() {
        modelStrategy = new SortByModelStrategy();
        powerStrategy = new SortByPowerStrategy();
        yearStrategy = new SortByYearStrategy();
    }

    @Test
    void sortByModel_normal() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("Volvo XC90").setPower(150).setYear(2018).build());
        cars.add(new Car.Builder().setModel("Audi a3").setPower(200).setYear(2020).build());
        cars.add(new Car.Builder().setModel("BMW 530d").setPower(180).setYear(2019).build());

        CarList sorted = modelStrategy.sort(cars);

        assertEquals("Audi a3", sorted.get(0).getModel());
        assertEquals("BMW 530d", sorted.get(1).getModel());
        assertEquals("Volvo XC90", sorted.get(2).getModel());
    }

    @Test
    void sortByPower_normal() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("AAAAA").setPower(200).setYear(2010).build());
        cars.add(new Car.Builder().setModel("BBBBB").setPower(100).setYear(2015).build());
        cars.add(new Car.Builder().setModel("CCCCC").setPower(150).setYear(2012).build());

        CarList sorted = powerStrategy.sort(cars);

        assertEquals(100, sorted.get(0).getPower());
        assertEquals(150, sorted.get(1).getPower());
        assertEquals(200, sorted.get(2).getPower());
    }

    @Test
    void sortByYear_normal() {
        CarList cars = new CarList();
        cars.add(new Car.Builder().setModel("XXXXX").setPower(120).setYear(2022).build());
        cars.add(new Car.Builder().setModel("YYYYY").setPower(130).setYear(2018).build());
        cars.add(new Car.Builder().setModel("ZZZZZ").setPower(140).setYear(2020).build());

        CarList sorted = yearStrategy.sort(cars);

        assertEquals(2018, sorted.get(0).getYear());
        assertEquals(2020, sorted.get(1).getYear());
        assertEquals(2022, sorted.get(2).getYear());
    }

    @Test
    void sort_emptyList_returnsEmpty() {
        CarList empty = new CarList();

        assertTrue(modelStrategy.sort(empty).isEmpty());
        assertTrue(powerStrategy.sort(empty).isEmpty());
        assertTrue(yearStrategy.sort(empty).isEmpty());
    }

    @Test
    void sort_nullList_returnsEmpty() {
        assertAll(
                () -> assertTrue(modelStrategy.sort(null).isEmpty()),
                () -> assertTrue(powerStrategy.sort(null).isEmpty()),
                () -> assertTrue(yearStrategy.sort(null).isEmpty())
        );
    }
}