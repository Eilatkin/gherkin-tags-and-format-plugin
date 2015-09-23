package sbl.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.util.TextRange;

public class FormatTableAction extends EditorAction {

    public FormatTableAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public FormatTableAction() {
        this(new FormatHandler());
    }

    @Override
    public void update(Editor editor, Presentation presentation, DataContext dataContext) {
        boolean isActionEnabled = false;
        if (editor.getDocument().isWritable()) {
            TextRange range = EditorHelper.getSelectedLines(editor);
            String text = EditorHelper.getTextForRange(editor, range);

            isActionEnabled = GherkinTable.isTable(text);
        }

        presentation.setEnabled(isActionEnabled);
    }

    private static class FormatHandler extends EditorWriteActionHandler {

        @Override
        public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
            Document document = editor.getDocument();

            if (editor == null || document == null || !document.isWritable()) {
                return;
            }


            TextRange range = EditorHelper.getSelectedLines(editor);
            String tableText = EditorHelper.getTextForRange(editor, range);

            GherkinTable table = GherkinTable.tryParse(tableText);
            // TODO: AA: handle null-value when parsing failed

            document.deleteString(range.getStartOffset(), range.getEndOffset());

            // TODO: AA: calculate indent
            document.insertString(range.getStartOffset(), table.format(2));

            caret.moveToOffset(range.getStartOffset());
            editor.getScrollingModel().scrollToCaret(ScrollType.RELATIVE);
        }
    }
}