package ru.eilatkin.gherkin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.util.TextRange;

final class EditorHelper {

    public static TextRange findTable(Editor editor) {
        LogicalPosition cursor = editor.getCaretModel().getLogicalPosition();
        Document document = editor.getDocument();

        int startLine = -1;
        for (int line = cursor.line; line >= 0; line--) {
            String text = getLineText(document, line);
            if (GherkinTable.isNotSuitableText(getLineText(document, line))) {
                break;
            }

            if (GherkinTable.isTableRow(text)) {
                startLine = line;
            }
        }

        int endLine = -1;
        for (int line = cursor.line; line < document.getLineCount(); line++) {
            String text = getLineText(document, line);
            if (GherkinTable.isNotSuitableText(getLineText(document, line))) {
                break;
            }

            if (GherkinTable.isTableRow(text)) {
                endLine = line;
            }
        }

        if (startLine == -1 || endLine == -1) {
            return TextRange.EMPTY_RANGE;
        }

        return TextRange.create(document.getLineStartOffset(startLine), document.getLineEndOffset(endLine));
    }

    private static String getLineText(Document document, int lineNumber) {
        TextRange range = new TextRange(document.getLineStartOffset(lineNumber), document.getLineEndOffset(lineNumber));
        return document.getText(range);
    }
}
