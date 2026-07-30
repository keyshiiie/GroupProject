package strategy.export;

import car.Car;

import java.io.IOException;
import java.util.List;
import static export.FileManager.writeCarToFile;

public class ExportCarsStrategy implements ExportStrategy {
    @Override
    public String getLabel() {
        return "Список машин";
    }

    @Override
    public void export(String fileName, List<?> data) throws IOException {
        if (data.isEmpty() || !(data.get(0) instanceof Car)) {
            throw new IllegalArgumentException("Ожидается список машин");
        }

        @SuppressWarnings("unchecked")
        List<Car> cars = (List<Car>) data;

        writeCarToFile(fileName, cars);
    }
}