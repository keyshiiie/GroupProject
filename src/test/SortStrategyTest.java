package test;

import car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import strategy.*;

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
    @DisplayName("Сортировка по модели: автомобили упорядочиваются в алфавитном порядке по названию модели")
    void sortByModel_normal() {
        List<Car> cars = Arrays.asList(
                new Car.Builder().setModel("Volvo XC90").setPower(150).setYear(2018).build(),
                new Car.Builder().setModel("Audi a3").setPower(200).setYear(2020).build(),
                new Car.Builder().setModel("BMW 530d").setPower(180).setYear(2019).build()
        );

        List<Car> sorted = modelStrategy.sort(cars);

        assertEquals("Audi a3", sorted.get(0).getModel());
        assertEquals("BMW 530d", sorted.get(1).getModel());
        assertEquals("Volvo XC90", sorted.get(2).getModel());
    }

    @Test
    @DisplayName("Сортировка по мощности: автомобили упорядочиваются по возрастанию мощности двигателя")
    void sortByPower_normal() {
        List<Car> cars = Arrays.asList(
                new Car.Builder().setModel("AAAAA").setPower(200).setYear(2010).build(),
                new Car.Builder().setModel("BBBBB").setPower(100).setYear(2015).build(),
                new Car.Builder().setModel("CCCCC").setPower(150).setYear(2012).build()
        );

        List<Car> sorted = powerStrategy.sort(cars);

        assertEquals(100, sorted.get(0).getPower());
        assertEquals(150, sorted.get(1).getPower());
        assertEquals(200, sorted.get(2).getPower());
    }

    @Test
    @DisplayName("Сортировка по году: автомобили упорядочиваются по возрастанию года выпуска")
    void sortByYear_normal() {
        List<Car> cars = Arrays.asList(
                new Car.Builder().setModel("XXXXX").setPower(120).setYear(2022).build(),
                new Car.Builder().setModel("YYYYY").setPower(130).setYear(2018).build(),
                new Car.Builder().setModel("ZZZZZ").setPower(140).setYear(2020).build()
        );

        List<Car> sorted = yearStrategy.sort(cars);

        assertEquals(2018, sorted.get(0).getYear());
        assertEquals(2020, sorted.get(1).getYear());
        assertEquals(2022, sorted.get(2).getYear());
    }

    @Test
    @DisplayName("Пустой список: все стратегии корректно возвращают пустой список")
    void sort_emptyList_returnsEmpty() {
        List<Car> empty = Collections.emptyList();

        assertTrue(modelStrategy.sort(empty).isEmpty());
        assertTrue(powerStrategy.sort(empty).isEmpty());
        assertTrue(yearStrategy.sort(empty).isEmpty());
    }

    @Test
    @DisplayName("Null‑список: все стратегии корректно обрабатывают null и возвращают пустой список")
    void sort_nullList_returnsEmpty() {
        assertAll(
                () -> assertTrue(modelStrategy.sort(null).isEmpty()),
                () -> assertTrue(powerStrategy.sort(null).isEmpty()),
                () -> assertTrue(yearStrategy.sort(null).isEmpty())
        );
    }
}
