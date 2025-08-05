package ru.eilatkin.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretState;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

import java.util.List;

abstract class BaseFeatureActionHandler extends EditorWriteActionHandler {

    @Override
    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
        Document document = editor.getDocument();

        if (!document.isWritable()) {
            return;
        }

        List<CaretState> caretsBefore = editor.getCaretModel().getCaretsAndSelections();

        String basePath = editor.getProject().getBasePath();
        Project project = editor.getProject();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
        String path = vFile.getPath();

        String[] info = {basePath.split("/")[basePath.split("/").length-1],path.replaceFirst(basePath,"")};
//        info-> {"IJ plugin template", "/src/test/resources/Меню избранное по всем страницам.feature"}
//        basePath-> C:/Users/eilat/Documents/Github/IJ plugin template,
//        path-> C:/Users/eilat/Documents/Github/IJ plugin template/src/test/resources/Меню избранное по всем страницам.feature

        String replaced = document.getText();
        String replacing = processText(replaced,info);

        if (replacing.equals(replaced)) {
            return;
        }
        document.setText("");
        document.insertString(0, replacing);

        editor.getCaretModel().setCaretsAndSelections(caretsBefore);
    }

    abstract String processText(String text, String[] info);
}