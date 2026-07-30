package strategy.export;

import strategy.NamedStrategy;

import java.io.IOException;
import java.util.List;

public interface ExportStrategy extends NamedStrategy {
    void export(String fileName, List<?> data) throws IOException;}

