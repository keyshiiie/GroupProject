package car;

import java.time.Year;
import java.util.Objects;

public class Car {
    private final int power;
    private final String model;
    private final int year;


    private Car(Builder builder){
        this.power = builder.power;
        this.model = builder.model.trim();
        this.year = builder.year;
    }

    public int getPower(){
        return power;
    }

    public String getModel(){
        return model;
    }

    public int getYear(){
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return getPower() == car.getPower() && getYear() == car.getYear() && getModel().equals(car.getModel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPower(), getModel(), getYear());
    }

    @Override
    public String toString() {
        return "Автомобиль{" +
                "Мощность=" + power +
                " л.с., Модель='" + model + '\'' +
                ", Год выпуска=" + year +
                '}';
    }

    public static class Builder {
        private int power;
        private String model;
        private int year;

        public Builder setPower(int power) {
            this.power = power;
            return this;
        }

        public Builder setModel(String model) {
            this.model = model;
            return this;
        }

        public Builder setYear(int year) {
            this.year = year;
            return this;
        }

        public Car build(){
            if(power <= 0) {
                throw new IllegalArgumentException(
                        "Мощность должна быть больше 0 л.с. (текущее значение: " + power + ")"
                );
            }
            if (power >= 10000) {
                throw new IllegalArgumentException(
                        "Мощность должна быть меньше 10000 л.с. (текущее значение: " + power + ")"
                );
            }
            if (model == null) {
                throw new IllegalArgumentException(
                        "Модель не может быть null"
                );
            }
            String trimmedModel = model.trim();
            if (trimmedModel.isEmpty()) {
                throw new IllegalArgumentException(
                        "Модель не может быть пустой"
                );
            }
            if (trimmedModel.length() < 2) {
                throw new IllegalArgumentException(
                        "Модель должна содержать минимум 2 символа (текущая длина: " + trimmedModel.length() + ")"
                );
            }
            if (trimmedModel.length() > 255) {
                throw new IllegalArgumentException(
                        "Модель должна содержать максимум 255 символов (текущая длина: " + trimmedModel.length() + ")"
                );
            }
            if (!trimmedModel.matches("^[\\p{L}\\p{N}\\s./-]+$")) {
                throw new IllegalArgumentException(
                        "Модель содержит запрещенные символы. " +
                                "Разрешены только буквы, цифры, пробелы и дефисы, точки и слеши. " +
                                "(текущее значение: '" + trimmedModel + "')"
                );
            }
            int currentYear = Year.now().getValue();
            if (year < 1886) {
                throw new IllegalArgumentException(
                        "Год выпуска не может быть раньше 1886 года" +
                                "(текущее значение: " + year + ")"
                );
            }
            if (year > currentYear) {
                throw new IllegalArgumentException(
                        "Год выпуска не может быть в будущем (текущее значение: " + year + ")"
                );
            }
            return new Car(this);
        }
    }
}
