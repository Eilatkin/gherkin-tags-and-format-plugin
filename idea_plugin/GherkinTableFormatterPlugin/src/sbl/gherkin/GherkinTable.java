package sbl.gherkin;

import java.util.ArrayList;
import java.util.List;

public class GherkinTable {
    private static final String LINE_SEPARATOR_REGEX = "\r?\n";
    private static final String CELL_SEPARATOR_REGEX = "\\|";
    private static final String CELL_SEPARATOR = "|";

    private List<String[]> _table = null;

    public static boolean isTable(String text) {
        return tryParse(text) != null;
    }

    public static GherkinTable tryParse(String text) {
        int length = -1;

        String[] lines = text.split(LINE_SEPARATOR_REGEX);
        List<String[]> table = new ArrayList<>();

        for(String line : lines) {
            line = line.trim();
            if (!line.startsWith(CELL_SEPARATOR) || !line.endsWith(CELL_SEPARATOR)) {
                return null;
            }

            String[] cells = line.split(CELL_SEPARATOR_REGEX);
            for(int i = 0; i < cells.length; i++) {
                cells[i] = cells[i].trim();
            }

            if (length == -1) {
                length = cells.length;
            }
            else if (length != cells.length) {
                return null;
            }

             table.add(cells);
        }

        return new GherkinTable(table);
    }

    private GherkinTable(List<String[]> table) {
        _table = table;
    }

    // TODO: AA: rework with Column and Rows
    public String format(int indent) {
        StringBuilder sb = new StringBuilder();
        for(String[] row : _table) {
            sb.append(repeat(" ", indent));
            sb.append("|");
            for (String cell : row) {
                sb.append(cell);
                sb.append("|");
            }
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    // TODO: AA: add transpose method

    private static String repeat(String source, int count) {
        StringBuilder sb = new StringBuilder(source.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(source);
        }

        return sb.toString();
    }
}
