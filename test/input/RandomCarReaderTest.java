package input;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.Test;
import reader.RandomCarReader;

import static org.junit.jupiter.api.Assertions.*;

class RandomCarReaderTest {
    @Test
    void shouldReadRangeValues() throws Exception {
        CarList cars = RandomCarReader.readRandomCarsFromFile();
        assertEquals(1000000, cars.size());
        for (Car car : cars) {
            assertTrue(car.getPower() >= 20 && car.getPower() <= 1000);
            assertTrue(car.getYear() >= 1950 && car.getYear() <= 2026);
        }
    }
}