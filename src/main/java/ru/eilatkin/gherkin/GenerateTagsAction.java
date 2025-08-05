package ru.eilatkin.gherkin;

import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

import java.util.Optional;

public final class GenerateTagsAction extends BaseFeatureAction {

    public GenerateTagsAction(EditorActionHandler defaultHandler) {
        super(defaultHandler);
    }

    public GenerateTagsAction() {
        this(new ActionHandler());
    }

    private static class ActionHandler extends BaseFeatureActionHandler {

        @Override
        public String processText(String text, String[] info) {
            Optional<Feature> feature = Feature.tryParse(text, info);
            assert feature.isPresent();


            return feature.get().allTags();
        }
    }
}