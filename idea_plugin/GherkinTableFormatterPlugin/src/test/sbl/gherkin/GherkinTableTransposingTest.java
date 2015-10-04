package sbl.gherkin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.Optional;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public class GherkinTableTransposingTest {

    private String _source;
    private String _formatted;

    public GherkinTableTransposingTest(String source, String formatted) {
        _source = source;
        _formatted = formatted;
    }

    @Test
    public void tryParseShouldParseTableCorrectly() {
        Optional<GherkinTable> table = GherkinTable.tryParse(_source);

        assert table.isPresent();
        assert table.get().transpose().format().equals(_formatted);
    }

    @Parameterized.Parameters
    public static Collection tables() {
        return asList(new String[][]{
                {
                        "|Hello|World|",

                        "| Hello |\n" +
                        "| World |"
                },
                {
                        "  | Column1|  SuperColumn2 | Col3 |   \n" +
                        "  | there is value 1| value2 |3 | ",

                        "| Column1      | there is value 1 |\n" +
                        "| SuperColumn2 | value2           |\n" +
                        "| Col3         | 3                |"
                },
                {
                        "| Column1 |\"Column2\"        | Column3 | | Column   5 |\n" +
                        "| Value1|               | v2|v3| v4|\n" +
                        "|v6         | v7 |v8|value     9         |          |",

                        "| Column1    | Value1 | v6          |\n" +
                        "| \"Column2\"  |        | v7          |\n" +
                        "| Column3    | v2     | v8          |\n" +
                        "|            | v3     | value     9 |\n" +
                        "| Column   5 | v4     |             |"
                }
        });
    }
}
