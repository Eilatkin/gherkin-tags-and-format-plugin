package sbl.gherkin;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import java.util.Optional;

public final class FormatTableAction extends BaseGherkinTableAction {

    public FormatTableAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public FormatTableAction() {
        this(new FormatHandler());
    }

    private static class FormatHandler extends BaseReplaceTextActionHandler {

        @Override
        public String process(String text) {
            Optional<GherkinTable> table = GherkinTable.tryParse(text);
            if (!table.isPresent()) {
                // TODO: AA: handle negotiation scenario - when parsing failed
            }

            int indent = text.indexOf(GherkinTable.CELL_SEPARATOR);
            return table.get().format(indent);
        }
    }
}