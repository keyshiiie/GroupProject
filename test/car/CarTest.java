package car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Car - создание объекта через builder")
public class CarTest {
    private Car.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new Car.Builder()
                .setPower(150)
                .setModel("Toyota Camry")
                .setYear(2020);
    }

    @Test
    @DisplayName("Создание валидного автомобиля")
    void testValidCarCreation(){
        Car car = builder.build();

        assertAll(
                () -> assertEquals(150, car.getPower(), "Мощность должна быть 150"),
                () -> assertEquals("Toyota Camry", car.getModel(), "Модель должна быть Toyota Camry"),
                () -> assertEquals(2020, car.getYear(), "Год должен быть 2020")
        );
    }

    @Test
    @DisplayName("Создание автомобиля с минимальными валидными значениями")
    void testValidCarWithMinimalValues() {
        Car car = new Car.Builder()
                .setPower(1)
                .setModel("A1") // 2 символа - минимальная длина
                .setYear(1886)
                .build();

        assertNotNull(car);
        assertEquals(1, car.getPower());
        assertEquals("A1", car.getModel());
        assertEquals(1886, car.getYear());
    }

    @Test
    @DisplayName("Создание автомобиля с пробелами в модели")
    void testValidCarWithSpacesInModel() {
        Car car = new Car.Builder()
                .setPower(200)
                .setModel(" BMW X5 ")
                .setYear(2023)
                .build();

        assertEquals("BMW X5", car.getModel(), "Пробелы должны быть обрезаны");
    }

    @Test
    @DisplayName("Мощность равна 0 - должно быть исключение")
    void testPowerZero() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setPower(0).build()
        );

        assertTrue(exception.getMessage().contains("больше 0"));
        assertTrue(exception.getMessage().contains("0"));
    }

    @Test
    @DisplayName("Мощность отрицательная - должно быть исключение")
    void testPowerNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setPower(-50).build()
        );

        assertTrue(exception.getMessage().contains("больше 0"));
        assertTrue(exception.getMessage().contains("-50"));
    }

    @Test
    @DisplayName("Мощность равна 10000 - должно быть исключение")
    void testPowerEqualToMax() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setPower(10000).build()
        );

        assertTrue(exception.getMessage().contains("меньше 10000"));
        assertTrue(exception.getMessage().contains("10000"));
    }

    @Test
    @DisplayName("Мощность больше 10000 - должно быть исключение")
    void testPowerGreaterThanMax() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setPower(15000).build()
        );

        assertTrue(exception.getMessage().contains("меньше 10000"));
        assertTrue(exception.getMessage().contains("15000"));
    }

    @Test
    @DisplayName("Модель равна null - должно быть исключение")
    void testModelNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setModel(null).build()
        );

        assertTrue(exception.getMessage().contains("не может быть null"));
    }

    @Test
    @DisplayName("Модель пустая - должно быть исключение")
    void testModelEmpty() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setModel("").build()
        );

        assertTrue(exception.getMessage().contains("не может быть пустой"));
    }

    @Test
    @DisplayName("Модель состоит только из пробелов - должно быть исключение")
    void testModelOnlySpaces() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setModel("   ").build()
        );

        assertTrue(exception.getMessage().contains("не может быть пустой"));
    }

    @Test
    @DisplayName("Модель слишком короткая (меньше 2 символов) - должно быть исключение")
    void testModelTooShort() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setModel("A").build()
        );

        assertTrue(exception.getMessage().contains("минимум 2 символа"));
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    @DisplayName("Модель с 2 символами - валидная")
    void testModelTwoCharacters() {
        Car car = builder.setModel("A1").build();
        assertEquals("A1", car.getModel());
    }

    @Test
    @DisplayName("Модель слишком длинная (больше 255 символов) - должно быть исключение")
    void testModelTooLong() {
        String longModel = "A".repeat(256);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setModel(longModel).build()
        );

        assertTrue(exception.getMessage().contains("максимум 255 символов"));
        assertTrue(exception.getMessage().contains("256"));
    }

    @Test
    @DisplayName("Модель с запрещенными символами - должно быть исключение")
    void testModelWithInvalidCharacters() {
        String[] invalidModels = {
                "Toyota@Camry",    // @
                "BMW#X5",          // #
                "Ford$Focus",      // $
                "Audi%",           // %
                "Tesla^Model",     // ^
                "VW&Golf",         // &
                "Mazda*3",         // *
                "Honda(Civic",     // (
                "Nissan)Sentra",   // )
                "Kia!Soul",        // !
                "Hyundai<Tucson",  // <
                "Chevy>Malibu",    // >
                "Dodge?Charger",   // ?
                "Subaru_Outback"   // _
        };

        for (String invalidModel : invalidModels) {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> builder.setModel(invalidModel).build()
            );

            assertTrue(exception.getMessage().contains("запрещенные символы"),
                    "Модель '" + invalidModel + "' должна вызывать исключение");
        }
    }

    @Test
    @DisplayName("Модель с разрешенными символами")
    void testModelWithValidCharacters() {
        String[] validModels = {
                "Toyota Camry",        // буквы и пробел
                "BMW X5",              // буквы, пробел, цифра
                "Ford-Focus",          // дефис
                "Audi A4",             // буквы, пробел, цифра
                "Tesla Model 3",       // буквы, пробел, цифра
                "Mercedes-Benz",       // дефис
                "VW Golf",             // буквы, пробел
                "Mazda3",              // буквы и цифры
                "Nissan Skyline",      // буквы и пробел
                "Land-Rover",          // дефис
                "Rolls-Royce",         // дефис
                "Aston Martin",        // пробел
                "Bugatti Veyron",      // пробел
                "Lamborghini Aventador", // пробел
                "Porsche 911",         // пробел и цифры
                "Ferrari F40",         // пробел, буква, цифра
                "Mitsubishi Lancer Evolution", // пробелы
                "Subaru Impreza WRX",  // пробелы
                "Toyota Corolla",      // пробел
                "Honda Civic Type-R",  // пробел и дефис
                "A1",                  // 2 символа (минимальная длина)
                "Ab"                   // 2 символа (минимальная длина)
        };

        for (String validModel : validModels) {
            Car car = builder.setModel(validModel).build();
            assertEquals(validModel.trim(), car.getModel(),
                    "Модель '" + validModel + "' должна быть валидной");
        }
    }

    @Test
    @DisplayName("Год меньше 1886 - должно быть исключение")
    void testYearBefore1886() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setYear(1885).build()
        );

        assertTrue(exception.getMessage().contains("раньше 1886 года"));
        assertTrue(exception.getMessage().contains("1885"));
    }

    @Test
    @DisplayName("Год равен 1886 - валидный")
    void testYear1886() {
        Car car = builder.setYear(1886).build();
        assertEquals(1886, car.getYear());
    }

    @Test
    @DisplayName("Год в будущем - должно быть исключение")
    void testYearInFuture() {
        int futureYear = java.time.Year.now().getValue() + 1;
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> builder.setYear(futureYear).build()
        );

        assertTrue(exception.getMessage().contains("не может быть в будущем"));
        assertTrue(exception.getMessage().contains(String.valueOf(futureYear)));
    }

    @Test
    @DisplayName("Текущий год - валидный")
    void testCurrentYear() {
        int currentYear = java.time.Year.now().getValue();
        Car car = builder.setYear(currentYear).build();
        assertEquals(currentYear, car.getYear());
    }

    @Test
    @DisplayName("Проверка equals и hashCode")
    void testEqualsAndHashCode() {
        Car car1 = builder.build();
        Car car2 = new Car.Builder()
                .setPower(150)
                .setModel("Toyota Camry")
                .setYear(2020)
                .build();

        Car car3 = new Car.Builder()
                .setPower(200)
                .setModel("Toyota Camry")
                .setYear(2020)
                .build();

        assertAll(
                () -> assertEquals(car1, car2, "Одинаковые машины должны быть равны"),
                () -> assertEquals(car1.hashCode(), car2.hashCode(), "HashCode должен совпадать"),
                () -> assertNotEquals(car1, car3, "Разные машины не должны быть равны"),
                () -> assertNotEquals(car1, null, "Машина не должна быть равна null")
        );
    }

    @Test
    @DisplayName("Проверка toString")
    void testToString() {
        Car car = builder.build();
        String toString = car.toString();

        // Исправленный тест, соответствующий формату toString()
        assertAll(
                () -> assertTrue(toString.contains("Автомобиль"), "Строка должна содержать 'Автомобиль'"),
                () -> assertTrue(toString.contains("Мощность: 150"), "Строка должна содержать 'Мощность: 150'"),
                () -> assertTrue(toString.contains("Модель: 'Toyota Camry'"), "Строка должна содержать 'Модель: 'Toyota Camry''"),
                () -> assertTrue(toString.contains("Год выпуска: 2020"), "Строка должна содержать 'Год выпуска: 2020'")
        );
    }

    @Test
    @DisplayName("Проверка toString с разными значениями")
    void testToStringWithDifferentValues() {
        Car car = new Car.Builder()
                .setPower(300)
                .setModel("BMW M3")
                .setYear(2022)
                .build();

        String toString = car.toString();

        assertAll(
                () -> assertTrue(toString.contains("Автомобиль")),
                () -> assertTrue(toString.contains("Мощность: 300")),
                () -> assertTrue(toString.contains("Модель: 'BMW M3'")),
                () -> assertTrue(toString.contains("Год выпуска: 2022"))
        );
    }

    @Test
    @DisplayName("Геттеры работают корректно")
    void testGetters() {
        Car car = builder
                .setPower(300)
                .setModel("BMW M3")
                .setYear(2022)
                .build();

        assertAll(
                () -> assertEquals(300, car.getPower()),
                () -> assertEquals("BMW M3", car.getModel()),
                () -> assertEquals(2022, car.getYear())
        );
    }
}