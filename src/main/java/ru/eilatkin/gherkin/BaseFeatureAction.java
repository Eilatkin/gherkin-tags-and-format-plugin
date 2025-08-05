package ru.eilatkin.gherkin;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;

abstract class BaseFeatureAction extends EditorAction {

    BaseFeatureAction(EditorActionHandler defaultHandler) { super(defaultHandler); }

    @Override
    public void update(Editor editor, Presentation presentation, DataContext dataContext) {
        boolean isActionEnabled = false;
        Document document = editor.getDocument();
        if (document.isWritable()) {
            Project project = editor.getProject();
            PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
            VirtualFile vFile = psiFile.getOriginalFile().getVirtualFile();
            String path = vFile.getPath();

            isActionEnabled = path.contains(".feature");
        }

        presentation.setEnabled(isActionEnabled);
    }
}
