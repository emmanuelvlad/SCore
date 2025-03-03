package com.ssomar.scoretestrecode.features.custom.required.money;


import com.ssomar.scoretestrecode.features.editor.FeatureEditorManagerAbstract;

public class RequiredMoneyEditorManager extends FeatureEditorManagerAbstract<RequiredMoneyEditor, RequiredMoney> {

    private static RequiredMoneyEditorManager instance;

    public static RequiredMoneyEditorManager getInstance() {
        if (instance == null) {
            instance = new RequiredMoneyEditorManager();
        }
        return instance;
    }

    @Override
    public RequiredMoneyEditor buildEditor(RequiredMoney parent) {
        return new RequiredMoneyEditor(parent.clone(parent.getParent()));
    }
}
