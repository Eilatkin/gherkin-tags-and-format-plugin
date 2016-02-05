package sbl.gherkin;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import java.util.Optional;

public final class FormatTableAction extends BaseGherkinTableAction {

    public FormatTableAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public FormatTableAction() {
        this(new ActionHandler());
    }

    private static class ActionHandler extends BaseGherkinTableActionHandler {

        @Override
        public String processText(String text) {
            Optional<GherkinTable> table = GherkinTable.tryParse(text);
            assert table.isPresent();

            int indent = text.indexOf(GherkinTable.CELL_SEPARATOR);
            return table.get().format(indent);
        }
    }
}