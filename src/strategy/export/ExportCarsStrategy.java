package strategy.export;

import car.Car;

import java.io.IOException;
import java.util.Collection;

import static export.FileManager.writeCarToFile;

public class ExportCarsStrategy implements ExportStrategy {
    @Override
    public String getLabel() {
        return "Список машин";
    }

    @Override
    public void export(String fileName, Collection<?> data) throws IOException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Список машин пуст");
        }

        for (Object obj : data) {
            if (!(obj instanceof Car)) {
                throw new IllegalArgumentException("Ожидается список машин");
            }
        }

        @SuppressWarnings("unchecked")
        Collection<Car> cars = (Collection<Car>) data;
        writeCarToFile(fileName, cars);
    }
}