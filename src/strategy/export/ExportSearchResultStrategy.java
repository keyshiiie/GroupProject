package strategy.export;

import java.io.IOException;
import java.util.List;

import static export.FileManager.writeCountResultToFile;

public class ExportSearchResultStrategy implements ExportStrategy {
    @Override
    public String getLabel() {
        return "Результаты поиска";
    }

    @Override
    public void export(String fileName, List<?> data) throws IOException {
        if (data.isEmpty() || !(data.get(0) instanceof String)) {
            throw new IllegalArgumentException("Ожидается список строк с результатами");
        }

        @SuppressWarnings("unchecked")
        List<String> results = (List<String>) data;
        writeCountResultToFile(fileName, results);
    }
}