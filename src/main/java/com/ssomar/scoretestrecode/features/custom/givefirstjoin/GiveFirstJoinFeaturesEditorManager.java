package com.ssomar.scoretestrecode.features.custom.givefirstjoin;


import com.ssomar.scoretestrecode.features.editor.FeatureEditorManagerAbstract;

public class GiveFirstJoinFeaturesEditorManager extends FeatureEditorManagerAbstract<GiveFirstJoinFeaturesEditor, GiveFirstJoinFeatures> {

    private static GiveFirstJoinFeaturesEditorManager instance;

    public static GiveFirstJoinFeaturesEditorManager getInstance() {
        if (instance == null) {
            instance = new GiveFirstJoinFeaturesEditorManager();
        }
        return instance;
    }

    @Override
    public GiveFirstJoinFeaturesEditor buildEditor(GiveFirstJoinFeatures parent) {
        return new GiveFirstJoinFeaturesEditor(parent.clone(parent.getParent()));
    }

}
