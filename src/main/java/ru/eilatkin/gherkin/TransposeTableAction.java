package ru.eilatkin.gherkin;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import java.util.Optional;

public final class TransposeTableAction extends BaseGherkinTableAction {

    public TransposeTableAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public TransposeTableAction() {
        this(new ActionHandler());
    }

    private static class ActionHandler extends BaseGherkinTableActionHandler {

        @Override
        protected String processText(String text) {
            Optional<GherkinTable> table = GherkinTable.tryParse(text);
            assert table.isPresent();

            int indent = text.indexOf(GherkinTable.CELL_SEPARATOR);
            return table.get().transpose().format(indent);
        }
    }
}
