package sbl.gherkin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.TextRange;

final class EditorHelper {

    public static TextRange getSelectedLines(Editor editor) {
        Document document = editor.getDocument();
        SelectionModel selection = editor.getSelectionModel();

        TextRange charsRange = new TextRange(selection.getSelectionStart(), selection.getSelectionEnd());
        TextRange linesRange = new TextRange(document.getLineNumber(charsRange.getStartOffset()), document.getLineNumber(charsRange.getEndOffset()));
        TextRange linesBlock = new TextRange(document.getLineStartOffset(linesRange.getStartOffset()), document.getLineEndOffset(linesRange.getEndOffset()));

        return linesBlock;
    }

    public static String getTextForRange(Editor editor, TextRange range) {
        Document document = editor.getDocument();
        return document.getText().substring(range.getStartOffset(), range.getEndOffset());
    }
}
