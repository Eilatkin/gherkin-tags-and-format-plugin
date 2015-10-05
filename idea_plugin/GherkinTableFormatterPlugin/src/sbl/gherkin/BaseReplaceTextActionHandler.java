package sbl.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

abstract class BaseReplaceTextActionHandler extends EditorWriteActionHandler {

    @Override
    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
        Document document = editor.getDocument();

        if (editor == null || document == null || !document.isWritable()) {
            return;
        }

        TextRange range = EditorHelper.getSelectedLines(editor);
        String replaced = EditorHelper.getTextForRange(editor, range);

        String replacing = process(replaced);

        int start = range.getStartOffset(), end = range.getEndOffset();
        document.deleteString(start, end);
        document.insertString(start, replacing);

        editor.getSelectionModel().setSelection(start, start + replacing.length());
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
    }

    abstract String process(String text);
}