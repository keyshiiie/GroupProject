package registry;

import strategy.NamedStrategy;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StrategyRegistry<T extends NamedStrategy> {

    private final Map<String, T> strategies = new LinkedHashMap<>();

    public void register(T strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }

        String label = strategy.getLabel(); // Теперь это безопасно: T extends NamedStrategy

        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Label cannot be null or blank");
        }

        strategies.put(label, strategy);
    }

    public List<T> getAll() {
        return List.copyOf(strategies.values());
    }

    public T getByLabel(String label) {
        return strategies.get(label);
    }
}
