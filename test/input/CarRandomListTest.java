package input;

import car.Car;
import car.CarList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.CarRandomList;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CarRandomListTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldInitializeOnceAndReturnSameList() throws Exception {
        CarRandomList.initCars();
        CarList firstList = CarRandomList.getCars();
        assertNotNull(firstList);
        assertEquals(1000000, firstList.size());

        CarRandomList.initCars();
        CarList secondList = CarRandomList.getCars();
        assertSame(firstList, secondList);
    }

    @Test
    void getCarsShouldReturnListAfterInit() throws Exception {
        CarRandomList.initCars();
        assertNotNull(CarRandomList.getCars());
        assertFalse(CarRandomList.getCars().isEmpty());
    }
}
