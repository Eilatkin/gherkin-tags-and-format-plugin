package sbl.gherkin;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import java.util.Optional;

public class FormatTableAction extends BaseGherkinTableAction {

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

            // TODO: AA: calculate indent

            return table.get().format();
        }
    }
}