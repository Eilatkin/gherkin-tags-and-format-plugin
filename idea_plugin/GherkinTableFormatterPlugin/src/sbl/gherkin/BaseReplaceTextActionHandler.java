package sbl.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

import java.util.List;

abstract class BaseReplaceTextActionHandler extends EditorWriteActionHandler {

    @Override
    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
        Document document = editor.getDocument();

        if (!document.isWritable()) {
            return;
        }

        List<CaretState> caretsBefore = editor.getCaretModel().getCaretsAndSelections();

        TextRange range = EditorHelper.findTable(editor);
        String replaced = document.getText(range);
        String replacing = process(replaced);

        int start = range.getStartOffset(), end = range.getEndOffset();
        document.deleteString(start, end);
        document.insertString(start, replacing);

        editor.getCaretModel().setCaretsAndSelections(caretsBefore);
    }

    abstract String process(String text);
}