package com.ssomar.scoretestrecode.features.custom.required.money;

import com.ssomar.score.menu.GUI;
import com.ssomar.scoretestrecode.features.editor.FeatureEditorInterface;

public class RequiredMoneyEditor extends FeatureEditorInterface<RequiredMoney> {

    public RequiredMoney requiredLevel;

    public RequiredMoneyEditor(RequiredMoney requiredLevel) {
        super("&lRequired Level Editor", 3 * 9);
        this.requiredLevel = requiredLevel.clone(requiredLevel.getParent());
        load();
    }

    @Override
    public void load() {
        requiredLevel.getMoney().initAndUpdateItemParentEditor(this, 0);
        requiredLevel.getCancelEventIfError().initAndUpdateItemParentEditor(this, 1);
        requiredLevel.getErrorMessage().initAndUpdateItemParentEditor(this, 2);

        // Back
        createItem(RED, 1, 18, GUI.BACK, false, false);

        // Reset menu
        createItem(ORANGE, 1, 19, GUI.RESET, false, false, "", "&c&oClick here to reset");

        // Save menu
        createItem(GREEN, 1, 26, GUI.SAVE, false, false, "", "&a&oClick here to save");
    }

    @Override
    public RequiredMoney getParent() {
        return requiredLevel;
    }
}
