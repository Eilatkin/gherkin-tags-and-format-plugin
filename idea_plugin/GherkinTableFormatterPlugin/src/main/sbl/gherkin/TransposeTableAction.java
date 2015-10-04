package sbl.gherkin;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import java.util.Optional;

public class TransposeTableAction extends BaseGherkinTableAction {

    public TransposeTableAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public TransposeTableAction() {
        this(new TransposeHandler());
    }

    private static class TransposeHandler extends BaseReplaceTextActionHandler {

        @Override
        protected String process(String text) {
            Optional<GherkinTable> table = GherkinTable.tryParse(text);
            if (!table.isPresent()) {
                // TODO: AA: handle negotiation scenario - when parsing failed
            }

            int indent = text.indexOf(GherkinTable.CELL_SEPARATOR);
            return table.get().transpose().format(indent);
        }
    }
}
