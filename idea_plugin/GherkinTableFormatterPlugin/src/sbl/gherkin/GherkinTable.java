package sbl.gherkin;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public String format(int indent) {
        StringBuilder sb = new StringBuilder();

        for (String[] row : _table) {
            sb.append(String.format("%" + indent + "s", "|"));
            for (int i = 0; i < _columnsCount; i++) {
                sb.append(String.format(" %-" + _columnsWidths[i] + "s |", row[i]));
            }
            sb.append(System.lineSeparator());
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
        Stream<String> rows = Stream.of(text.split(LINE_SEPARATOR_REGEX))
                .map(r -> r.trim())
                .filter(r -> !r.startsWith(COMMENT_MARK));

        if (!rows.allMatch(r -> r.startsWith(CELL_SEPARATOR) && r.endsWith(CELL_SEPARATOR))) {
            return Optional.empty();
        }

        Stream<String[]> table = rows.map(r -> r.substring(1, r.length() - 2))
                .map(r -> Stream.of(r.split(CELL_SEPARATOR_REGEX))
                        .map(x -> x.trim())
                        .toArray(size -> new String[size]));

        if (table.mapToInt(r -> r.length).distinct().count() != 1) {
            return Optional.empty();
        }

        return Optional.of(new GherkinTable(table.collect(Collectors.toList())));
    }

    private static String repeat(String source, int count) {
        StringBuilder sb = new StringBuilder(source.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(source);
        }

        return sb.toString();
    }
}
