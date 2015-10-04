package sbl.gherkin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class GherkinTableFormattingTest {

    private String _source;
    private String _formatted;

    public GherkinTableFormattingTest(String source, String formatted) {
        _source = source;
        _formatted = formatted;
    }

    @Test
    public void tryParseShouldParseTableCorrectly() {
        Optional<GherkinTable> table = GherkinTable.tryParse(_source);

        assert table.isPresent();
        assert table.get().format().equals(_formatted);
    }

    @Parameterized.Parameters
    public static Collection tables() {
        return asList(new String[][]{
                {
                        "|Hello|World|",

                        "| Hello | World |"
                },
                {
                        "  | Column1|  SuperColumn2 | Col3 |   \n" +
                        "  | there is value 1| value2 |3 | ",

                        "| Column1          | SuperColumn2 | Col3 |\n" +
                        "| there is value 1 | value2       | 3    |"
                },
                {
                        "| Column1 |\"Column2\"        | Column3 | | Column   5 |\n" +
                        "| Value1|               | v2|v3| v4|\n" +
                        "|v6         | v7 |v8|value     9         |          |",

                        "| Column1 | \"Column2\" | Column3 |             | Column   5 |\n" +
                        "| Value1  |           | v2      | v3          | v4         |\n" +
                        "| v6      | v7        | v8      | value     9 |            |"
                }
        });
    }
}
