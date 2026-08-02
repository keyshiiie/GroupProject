package utils;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MultithreadedCounter - многопоточный подсчёт элементов")
class MultithreadedCounterTest {

    private CarList cars;
    private CarList emptyCars;
    private CarList nullCars;

    @BeforeEach
    void setUp() {
        cars = new CarList();
        cars.add(new Car.Builder()
                .setPower(150)
                .setModel("Toyota Camry")
                .setYear(2020)
                .build());
        cars.add(new Car.Builder()
                .setPower(200)
                .setModel("BMW X5")
                .setYear(2021)
                .build());
        cars.add(new Car.Builder()
                .setPower(150)
                .setModel("Toyota Camry")
                .setYear(2019)
                .build());
        cars.add(new Car.Builder()
                .setPower(300)
                .setModel("Audi A8")
                .setYear(2022)
                .build());
        cars.add(new Car.Builder()
                .setPower(150)
                .setModel("Honda Accord")
                .setYear(2020)
                .build());
        cars.add(new Car.Builder()
                .setPower(250)
                .setModel("Mercedes E-Class")
                .setYear(2021)
                .build());

        emptyCars = new CarList();
        nullCars = null;
    }

    @Test
    @DisplayName("Подсчёт всех автомобилей (условие всегда true)")
    void testCountAllCars() {
        Predicate<Car> alwaysTrue = car -> true;
        int result = MultithreadedCounter.countCars(cars, alwaysTrue);

        assertEquals(6, result, "Должны быть подсчитаны все 6 автомобилей");
    }

    @Test
    @DisplayName("Подсчёт автомобилей по модели (Toyota Camry)")
    void testCountByModel() {
        Predicate<Car> isToyotaCamry = car -> "Toyota Camry".equals(car.getModel());
        int result = MultithreadedCounter.countCars(cars, isToyotaCamry);

        assertEquals(2, result, "Должно быть 2 автомобиля Toyota Camry");
    }

    @Test
    @DisplayName("Подсчёт автомобилей по мощности (150 л.с.)")
    void testCountByPower() {
        Predicate<Car> power150 = car -> car.getPower() == 150;
        int result = MultithreadedCounter.countCars(cars, power150);

        assertEquals(3, result, "Должно быть 3 автомобиля с мощностью 150 л.с.");
    }

    @Test
    @DisplayName("Подсчёт автомобилей по году (2021)")
    void testCountByYear() {
        Predicate<Car> year2021 = car -> car.getYear() == 2021;
        int result = MultithreadedCounter.countCars(cars, year2021);

        assertEquals(2, result, "Должно быть 2 автомобиля 2021 года");
    }

    @Test
    @DisplayName("Подсчёт автомобилей, которых нет в списке")
    void testCountNoMatches() {
        Predicate<Car> noMatch = car -> car.getPower() == 999;
        int result = MultithreadedCounter.countCars(cars, noMatch);

        assertEquals(0, result, "Должно быть 0 совпадений");
    }

    @Test
    @DisplayName("Подсчёт на пустом списке - результат 0")
    void testCountEmptyList() {
        Predicate<Car> alwaysTrue = car -> true;
        int result = MultithreadedCounter.countCars(emptyCars, alwaysTrue);

        assertEquals(0, result, "Для пустого списка результат должен быть 0");
    }

    @Test
    @DisplayName("Подсчёт с null списком - результат 0")
    void testCountNullList() {
        Predicate<Car> alwaysTrue = car -> true;
        int result = MultithreadedCounter.countCars(nullCars, alwaysTrue);

        assertEquals(0, result, "Для null списка результат должен быть 0");
    }

    @Test
    @DisplayName("Подсчёт с null условием - исключение")
    void testCountNullCondition() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> MultithreadedCounter.countCars(cars, null),
                "При null условии должно быть исключение"
        );

        assertEquals("Условие не может быть null", exception.getMessage());
    }

    @Test
    @DisplayName("Подсчёт с составным условием (мощность > 200 И год >= 2021)")
    void testCountWithComplexCondition() {
        Predicate<Car> condition = car -> car.getPower() > 200 && car.getYear() >= 2021;
        int result = MultithreadedCounter.countCars(cars, condition);

        assertEquals(2, result, "Должно быть 2 автомобиля: Audi A8 (300 л.с., 2022) и Mercedes (250 л.с., 2021)");
    }

    @Test
    @DisplayName("Подсчёт с условием по мощности > 200")
    void testCountPowerGreaterThan200() {
        Predicate<Car> powerGreater200 = car -> car.getPower() > 200;
        int result = MultithreadedCounter.countCars(cars, powerGreater200);

        assertEquals(2, result, "Должно быть 2 автомобиля с мощностью > 200 л.с.");
    }

    @Test
    @DisplayName("Подсчёт с условием по мощности <= 150")
    void testCountPowerLessOrEqual150() {
        Predicate<Car> powerLessOrEqual150 = car -> car.getPower() <= 150;
        int result = MultithreadedCounter.countCars(cars, powerLessOrEqual150);

        assertEquals(3, result, "Должно быть 3 автомобиля с мощностью <= 150 л.с.");
    }

    @Test
    @DisplayName("Подсчёт с условием по году между 2020 и 2022")
    void testCountYearBetween2020And2022() {
        Predicate<Car> yearBetween = car -> car.getYear() >= 2020 && car.getYear() <= 2022;
        int result = MultithreadedCounter.countCars(cars, yearBetween);

        assertEquals(5, result, "Должно быть 5 автомобилей с годом от 2020 до 2022");
    }

    @Test
    @DisplayName("Подсчёт с условием по модели, содержащей подстроку")
    void testCountModelContainsSubstring() {
        Predicate<Car> modelContains = car -> car.getModel().contains("Toyota");
        int result = MultithreadedCounter.countCars(cars, modelContains);

        assertEquals(2, result, "Должно быть 2 автомобиля с моделью, содержащей 'Toyota'");
    }

    @Test
    @DisplayName("Подсчёт с несколькими условиями через OR")
    void testCountWithOrCondition() {
        Predicate<Car> condition = car -> car.getPower() == 150 || car.getPower() == 300;
        int result = MultithreadedCounter.countCars(cars, condition);

        assertEquals(4, result, "Должно быть 4 автомобиля: 3 с мощностью 150 и 1 с мощностью 300");
    }

    @Test
    @DisplayName("Подсчёт на большом списке (проверка многопоточности)")
    void testCountLargeList() {
        CarList largeList = new CarList();
        for (int i = 0; i < 1000; i++) {
            largeList.add(new Car.Builder()
                    .setPower(100 + i % 10)
                    .setModel("Model" + i)
                    .setYear(2000 + i % 25)
                    .build());
        }

        Predicate<Car> powerGreater150 = car -> car.getPower() > 150;
        int result = MultithreadedCounter.countCars(largeList, powerGreater150);

        // Подсчитываем ожидаемое количество вручную
        int expected = 0;
        for (int i = 0; i < largeList.size(); i++) {
            if (powerGreater150.test(largeList.get(i))) {
                expected++;
            }
        }
        assertEquals(expected, result, "Многопоточный подсчёт должен совпадать с последовательным");
    }

    @Test
    @DisplayName("Подсчёт на списке с одним элементом")
    void testCountSingleElement() {
        CarList singleCar = new CarList();
        singleCar.add(new Car.Builder()
                .setPower(150)
                .setModel("Toyota")
                .setYear(2020)
                .build());

        Predicate<Car> isToyota = car -> "Toyota".equals(car.getModel());
        int result = MultithreadedCounter.countCars(singleCar, isToyota);

        assertEquals(1, result, "Должен быть найден 1 автомобиль");
    }

    @Test
    @DisplayName("Подсчёт на списке с одним элементом, не соответствующим условию")
    void testCountSingleElementNoMatch() {
        CarList singleCar = new CarList();
        singleCar.add(new Car.Builder()
                .setPower(150)
                .setModel("Toyota")
                .setYear(2020)
                .build());

        Predicate<Car> isBMW = car -> "BMW".equals(car.getModel());
        int result = MultithreadedCounter.countCars(singleCar, isBMW);

        assertEquals(0, result, "Не должно быть найдено ни одного автомобиля");
    }

    @Test
    @DisplayName("Подсчёт с условием, которое всегда false")
    void testCountAlwaysFalse() {
        Predicate<Car> alwaysFalse = car -> false;
        int result = MultithreadedCounter.countCars(cars, alwaysFalse);

        assertEquals(0, result, "Результат должен быть 0");
    }

    @Test
    @DisplayName("Подсчёт с учётом регистра (equalsIgnoreCase)")
    void testCountWithCaseInsensitive() {
        Predicate<Car> condition = car -> car.getModel().equalsIgnoreCase("toyota camry");
        int result = MultithreadedCounter.countCars(cars, condition);

        assertEquals(2, result, "Должно быть 2 автомобиля Toyota Camry (регистр не важен)");
    }
}