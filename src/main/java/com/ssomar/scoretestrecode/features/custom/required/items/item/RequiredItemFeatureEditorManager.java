package com.ssomar.scoretestrecode.features.custom.required.items.item;


import com.ssomar.scoretestrecode.features.editor.FeatureEditorManagerAbstract;

public class RequiredItemFeatureEditorManager extends FeatureEditorManagerAbstract<RequiredItemFeatureEditor, RequiredItemFeature> {

    private static RequiredItemFeatureEditorManager instance;

    public static RequiredItemFeatureEditorManager getInstance() {
        if (instance == null) {
            instance = new RequiredItemFeatureEditorManager();
        }
        return instance;
    }

    @Override
    public RequiredItemFeatureEditor buildEditor(RequiredItemFeature parent) {
        return new RequiredItemFeatureEditor(parent.clone(parent.getParent()));
    }

}
