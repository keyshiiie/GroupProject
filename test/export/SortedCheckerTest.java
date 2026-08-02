package export;

import car.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.SortedChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortedCheckerTest {

    private List<Car> cars;
    private Comparator<Car> byModelComparator;
    private Comparator<Car> byYearComparator;
    private Comparator<Car> byPowerComparator;

    @BeforeEach
    void setUp() {
        cars = new ArrayList<>();

        byModelComparator = Comparator.comparing(Car::getModel);
        byYearComparator = Comparator.comparingInt(Car::getYear);
        byPowerComparator = Comparator.comparingInt(Car::getPower);
    }

    private Car createCar(String model, int year, int power) {
        return new Car.Builder()
                .setModel(model)
                .setYear(year)
                .setPower(power)
                .build();
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsNull() {
        assertTrue(SortedChecker.isSorted(null, byModelComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsEmpty() {
        assertTrue(SortedChecker.isSorted(new ArrayList<>(), byModelComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListHasOneElement() {
        cars.add(createCar("Toyota Camry", 2020, 200));
        assertTrue(SortedChecker.isSorted(cars, byModelComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsSortedByModel() {
        cars.add(createCar("Audi A4", 2021, 250));
        cars.add(createCar("BMW 3 Series", 2020, 300));
        cars.add(createCar("Toyota Camry", 2022, 200));

        assertTrue(SortedChecker.isSorted(cars, byModelComparator));
    }

    @Test
    void isSorted_ShouldReturnFalse_WhenListIsNotSortedByModel() {
        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("BMW 3 Series", 2021, 300));
        cars.add(createCar("Audi A4", 2022, 250));

        assertFalse(SortedChecker.isSorted(cars, byModelComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsSortedByYear() {
        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("BMW 3 Series", 2021, 300));
        cars.add(createCar("Audi A4", 2022, 250));

        assertTrue(SortedChecker.isSorted(cars, byYearComparator));
    }

    @Test
    void isSorted_ShouldReturnFalse_WhenListIsNotSortedByYear() {
        cars.add(createCar("Toyota Camry", 2022, 250));
        cars.add(createCar("BMW 3 Series", 2020, 200));
        cars.add(createCar("Audi A4", 2021, 300));

        assertFalse(SortedChecker.isSorted(cars, byYearComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsSortedByPower() {
        cars.add(createCar("Toyota Camry", 2020, 150));
        cars.add(createCar("BMW 3 Series", 2021, 200));
        cars.add(createCar("Audi A4", 2022, 300));

        assertTrue(SortedChecker.isSorted(cars, byPowerComparator));
    }

    @Test
    void isSorted_ShouldReturnFalse_WhenListIsNotSortedByPower() {
        cars.add(createCar("Toyota Camry", 2020, 300));
        cars.add(createCar("BMW 3 Series", 2021, 200));
        cars.add(createCar("Audi A4", 2022, 150));

        assertFalse(SortedChecker.isSorted(cars, byPowerComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListHasEqualElements() {
        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("Toyota Camry", 2020, 200));

        assertTrue(SortedChecker.isSorted(cars, byModelComparator));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenComparatorIsReversedAndListIsSortedDescending() {
        Comparator<Car> reverseModelComparator = byModelComparator.reversed();

        List<Car> descendingCars = new ArrayList<>();
        descendingCars.add(createCar("Toyota Camry", 2022, 250));
        descendingCars.add(createCar("BMW 3 Series", 2021, 300));
        descendingCars.add(createCar("Audi A4", 2020, 200));

        assertTrue(SortedChecker.isSorted(descendingCars, reverseModelComparator));
    }

    @Test
    void isSorted_ShouldReturnFalse_WhenListIsNotSortedDescending() {
        Comparator<Car> reverseModelComparator = byModelComparator.reversed();

        List<Car> notDescendingCars = new ArrayList<>();
        notDescendingCars.add(createCar("Audi A4", 2020, 200));
        notDescendingCars.add(createCar("Toyota Camry", 2022, 250));
        notDescendingCars.add(createCar("BMW 3 Series", 2021, 300));

        assertFalse(SortedChecker.isSorted(notDescendingCars, reverseModelComparator));
    }

    @Test
    void isSorted_ShouldHandleCustomComparator_WhenComparingByMultipleFields() {
        Comparator<Car> byModelThenYear = byModelComparator
                .thenComparing(byYearComparator);

        cars.add(createCar("Audi A4", 2020, 200));
        cars.add(createCar("Audi A4", 2021, 250));
        cars.add(createCar("BMW 3 Series", 2020, 300));

        assertTrue(SortedChecker.isSorted(cars, byModelThenYear));
    }

    @Test
    void isSorted_ShouldReturnFalse_WhenListIsNotSortedByCustomComparator() {
        Comparator<Car> byModelThenYear = byModelComparator
                .thenComparing(byYearComparator);

        cars.add(createCar("Audi A4", 2021, 250));
        cars.add(createCar("Audi A4", 2020, 200));
        cars.add(createCar("BMW 3 Series", 2020, 300));

        assertFalse(SortedChecker.isSorted(cars, byModelThenYear));
    }

    @Test
    void isSorted_ShouldHandleComparator_WhenComparingByPowerAndYear() {
        Comparator<Car> byPowerThenYear = byPowerComparator
                .thenComparing(byYearComparator);

        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("BMW 3 Series", 2021, 200));
        cars.add(createCar("Audi A4", 2022, 300));

        assertTrue(SortedChecker.isSorted(cars, byPowerThenYear));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsNullAndComparatorIsNull() {
        assertTrue(SortedChecker.isSorted(null, null));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListIsEmptyAndComparatorIsNull() {
        assertTrue(SortedChecker.isSorted(new ArrayList<>(), null));
    }

    @Test
    void isSorted_ShouldReturnTrue_WhenListHasOneElementAndComparatorIsNull() {
        List<Car> singleCarList = new ArrayList<>();
        singleCarList.add(createCar("Toyota Camry", 2020, 200));
        assertTrue(SortedChecker.isSorted(singleCarList, null));
    }

    @Test
    void isSorted_ShouldThrowNullPointerException_WhenListHasMultipleElementsAndComparatorIsNull() {
        cars.add(createCar("Toyota Camry", 2020, 200));
        cars.add(createCar("BMW 3 Series", 2021, 300));

        assertThrows(NullPointerException.class,
                () -> SortedChecker.isSorted(cars, null));
    }

    @Test
    void isSorted_ShouldWorkWithRealWorldData() {
        List<Car> cars = new ArrayList<>();
        cars.add(createCar("Honda Accord", 2019, 192));
        cars.add(createCar("Honda Accord", 2020, 192));
        cars.add(createCar("Honda Accord", 2021, 252));
        cars.add(createCar("Toyota Camry", 2020, 203));
        cars.add(createCar("Toyota Camry", 2021, 203));

        Comparator<Car> byModelThenYear = Comparator
                .comparing(Car::getModel)
                .thenComparingInt(Car::getYear);

        assertTrue(SortedChecker.isSorted(cars, byModelThenYear));
    }
}