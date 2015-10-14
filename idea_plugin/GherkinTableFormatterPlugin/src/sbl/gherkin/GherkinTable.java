package sbl.gherkin;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

final class GherkinTable {
    public static final String CELL_SEPARATOR = "|";

    private static final String COMMENT_MARK = "#";
    private static final String LINE_SEPARATOR_REGEX = "\r?\n";
    private static final String LINE_SEPARATOR = "\n";
    private static final String CELL_SEPARATOR_REGEX = "\\|";
    private static final String EMPTY_CELL = " ";

    private List<String[]> _table = null;
    private Map<Integer, List<String>> _comments = null;
    private int _columnsCount = -1;
    private int[] _columnsWidths;

    private GherkinTable(List<String[]> table, Map<Integer, List<String>> comments) {
        _table = table;
        _comments = comments;
        _columnsCount = _table.stream().mapToInt(r -> r.length).max().getAsInt();
        _columnsWidths = new int[_columnsCount];

        for(int i = 0; i < _columnsCount; i++) {
            int width = getColumn(i).mapToInt(String::length).max().getAsInt();
            _columnsWidths[i] = width == 0 ? 1 : width;
        }
    }

    public GherkinTable transpose() {
        List<String[]> transposed = new ArrayList<>(_columnsCount);
        for(int i = 0; i < _columnsCount; i++) {
            transposed.add(getColumn(i).toArray(String[]::new));
        }

        List<String> allComments = _comments.values().stream()
                .flatMap(Collection::stream)
                .filter(GherkinTable::isComment)
                .collect(toList());

        return new GherkinTable(transposed, new HashMap<Integer, List<String>>() { { put(0, allComments); } });
    }

    public String format() {
        return format(0);
    }

    public String format(int indent) {
        assert indent >= 0;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < _table.size(); i++) {
            String format = indent == 0 ? "%s" : "%" + (indent+1) + "s";
            appendComments(sb, i, format);

            sb.append(String.format(format, "|"));
            for (int j = 0; j < _columnsCount; j++) {
                sb.append(String.format(" %-" + _columnsWidths[j] + "s |", getValue(_table.get(i), j)));
            }
            if (i != _table.size() - 1) {
                sb.append(LINE_SEPARATOR);
            }
        }

        return sb.toString();
    }

    private void appendComments(StringBuilder stringBuilder, int row, String format) {
        if (_comments == null || !_comments.containsKey(row)) {
            return;
        }

        _comments.get(row).stream().forEach(c -> stringBuilder.append(String.format(format + LINE_SEPARATOR, c)));
    }

    @Override
    public String toString() {
        return format();
    }

    private Stream<String> getColumn(int index) {
        return _table.stream().map(row -> getValue(row, index));
    }

    private String getValue(String[] row, int index) {
        return index < row.length ? row[index] : EMPTY_CELL;
    }

    public static boolean isTableRow(String row) {
        return StringUtils.stripToEmpty(row).startsWith(CELL_SEPARATOR);
    }
    public static boolean isComment(String text) { return StringUtils.stripToEmpty(text).startsWith(COMMENT_MARK); }
    public static boolean isIgnoredText(String text) { return StringUtils.isBlank(text) || isComment(text); }
    public static boolean isSuitableText(String text) {
        return isTableRow(text) || isIgnoredText(text);
    }

    public static Optional<GherkinTable> tryParse(String text) {
        String[] lines = StringUtils.splitPreserveAllTokens(text, LINE_SEPARATOR_REGEX, -1);

        List<String[]> table = new ArrayList<>();
        Map<Integer, List<String>> ignored = new HashMap<>();

        for (String line : lines) {
            if (isTableRow(line)) {
                String stripped = StringUtils.strip(StringUtils.strip(line), CELL_SEPARATOR);
                String[] values = Stream.of(StringUtils.split(stripped, CELL_SEPARATOR_REGEX, -1))
                        .map(StringUtils::strip)
                        .toArray(String[]::new);
                table.add(values);
            }
            else if (isIgnoredText(line)) {
                int commentForLine = table.size();
                if (!ignored.containsKey(commentForLine)) {
                    ignored.put(commentForLine, new ArrayList<>());
                }
                List<String> comments = ignored.get(commentForLine);
                comments.add(line);
            }
            else {
                return Optional.empty();
            }
        }

        if (table.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new GherkinTable(table, ignored));
    }
}
