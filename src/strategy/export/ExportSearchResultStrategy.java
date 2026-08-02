package strategy.export;

import java.io.IOException;
import java.util.Collection;

import static export.FileManager.writeCountResultToFile;

public class ExportSearchResultStrategy implements ExportStrategy {
    @Override
    public String getLabel() {
        return "Результаты поиска";
    }

    @Override
    public void export(String fileName, Collection<?> data) throws IOException {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Нет результатов для экспорта");
        }

        // Проверяем, что все элементы - String
        for (Object obj : data) {
            if (!(obj instanceof String)) {
                throw new IllegalArgumentException("Ожидается список строк с результатами");
            }
        }

        @SuppressWarnings("unchecked")
        Collection<String> results = (Collection<String>) data;
        writeCountResultToFile(fileName, results);
    }
}