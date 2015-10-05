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
        String tableText = EditorHelper.getTextForRange(editor, range);

        document.deleteString(range.getStartOffset(), range.getEndOffset());
        document.insertString(range.getStartOffset(), process(tableText));

        caret.moveToOffset(range.getStartOffset());
        editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
    }

    abstract String process(String text);
}