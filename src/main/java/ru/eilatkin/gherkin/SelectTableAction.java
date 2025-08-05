package ru.eilatkin.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

public class SelectTableAction extends BaseGherkinTableAction {

    public SelectTableAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public SelectTableAction() {
        this(new ActionHandler());
    }

    private static class ActionHandler extends EditorWriteActionHandler {

        @Override
        public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
            TextRange range = EditorHelper.findTable(editor);
            editor.getSelectionModel().setSelection(range.getStartOffset(), range.getEndOffset());
        }
    }
}
