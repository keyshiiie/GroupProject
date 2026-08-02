package strategy.export;

import strategy.NamedStrategy;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface ExportStrategy extends NamedStrategy {
    void export(String fileName, Collection<?> data) throws IOException;
}
