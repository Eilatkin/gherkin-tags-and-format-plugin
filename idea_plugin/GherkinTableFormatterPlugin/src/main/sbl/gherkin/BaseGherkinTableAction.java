package sbl.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.TextRange;

abstract class BaseGherkinTableAction extends EditorAction {

    protected BaseGherkinTableAction(EditorActionHandler defaultHandler) { super(defaultHandler); }

    @Override
    public void update(Editor editor, Presentation presentation, DataContext dataContext) {
        boolean isActionEnabled = false;
        if (editor.getDocument().isWritable()) {
            TextRange range = EditorHelper.getSelectedLines(editor);
            String text = EditorHelper.getTextForRange(editor, range);

            isActionEnabled = GherkinTable.tryParse(text).isPresent();
        }

        presentation.setEnabled(isActionEnabled);
    }
}
