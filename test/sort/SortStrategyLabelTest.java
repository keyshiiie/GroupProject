package sort;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import strategy.*;
import strategy.sort.*;

class SortStrategyLabelTest {

    @Test
    void labels_areCorrect() {
        Assertions.assertEquals("По модели (model)", new SortByModelStrategy().getLabel());
        Assertions.assertEquals("По мощности (power)", new SortByPowerStrategy().getLabel());
        Assertions.assertEquals("По году выпуска (year)", new SortByYearStrategy().getLabel());
        Assertions.assertEquals("По мощности (чётные только)", new SortByPowerEvenStrategy().getLabel());
        Assertions.assertEquals("По году (чётные только)", new SortByYearEvenStrategy().getLabel());
    }
}