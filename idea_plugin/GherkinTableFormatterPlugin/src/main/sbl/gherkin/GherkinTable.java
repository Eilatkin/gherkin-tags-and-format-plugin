package sbl.gherkin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class GherkinTable {
    private static final String COMMENT_MARK = "#";
    private static final String LINE_SEPARATOR_REGEX = "\r?\n";
    private static final String CELL_SEPARATOR_REGEX = "\\|";
    private static final String CELL_SEPARATOR = "|";

    private List<String[]> _table = null;
    private int _columnsCount = -1;
    private int[] _columnsWidths;

    private GherkinTable(List<String[]> table) {
        rebuild(table);
    }

    public String format() {
        return format(0);
    }

    public String format(int indent) {
        assert indent >= 0;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < _table.size(); i++) {
            String format = indent == 0 ? "%s" : "%" + indent + "s";
            sb.append(String.format(format, "|"));
            for (int j = 0; j < _columnsCount; j++) {
                sb.append(String.format(" %-" + _columnsWidths[j] + "s |", _table.get(i)[j]));
            }
            if (i != _table.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    // TODO: AA: add transpose method

    private Stream<String> getColumn(int index) {
        return _table.stream().map(row -> row[index]);
    }

    private void rebuild(List<String[]> table) {
        _table = table;

        _columnsCount = _table.get(0).length;
        _columnsWidths = new int[_columnsCount];

        for(int i = 0; i < _columnsCount; i++) {
            _columnsWidths[i] = getColumn(i).mapToInt(x -> x.length()).max().getAsInt();
        }
    }

    public static Optional<GherkinTable> tryParse(String text) {
        if (!getRows(text).allMatch(r -> r.startsWith(CELL_SEPARATOR) && r.endsWith(CELL_SEPARATOR))) {
            return Optional.empty();
        }

        if (getTable(text).mapToInt(r -> r.length).distinct().count() != 1) {
            return Optional.empty();
        }

        return Optional.of(new GherkinTable(getTable(text).collect(toList())));
    }

    private static Stream<String> getRows(String text) {
        return Stream.of(text.split(LINE_SEPARATOR_REGEX))
                .map(r -> r.trim())
                .filter(r -> !r.startsWith(COMMENT_MARK));
    }

    private static Stream<String[]> getTable(String text) {
        return getRows(text).map(r -> r.substring(1, r.length() - 1))
                .map(r -> Stream.of(r.split(CELL_SEPARATOR_REGEX))
                        .map(x -> x.trim())
                        .toArray(size -> new String[size]));
    }
}
