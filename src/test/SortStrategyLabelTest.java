package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import strategy.*;
import strategy.sort.*;

class SortStrategyLabelTest {

    @Test
    void labels_areCorrect() {
        assertEquals("По модели (model)", new SortByModelStrategy().getLabel());
        assertEquals("По мощности (power)", new SortByPowerStrategy().getLabel());
        assertEquals("По году выпуска (year)", new SortByYearStrategy().getLabel());
        assertEquals("По мощности (чётные только)", new SortByPowerEvenStrategy().getLabel());
        assertEquals("По году (чётные только)", new SortByYearEvenStrategy().getLabel());
    }
}