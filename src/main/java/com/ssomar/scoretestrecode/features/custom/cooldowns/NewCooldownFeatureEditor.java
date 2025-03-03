package com.ssomar.scoretestrecode.features.custom.cooldowns;

import com.ssomar.score.menu.GUI;
import com.ssomar.scoretestrecode.features.editor.FeatureEditorInterface;

public class NewCooldownFeatureEditor extends FeatureEditorInterface<NewCooldownFeature> {

    public NewCooldownFeature cooldownFeature;

    public NewCooldownFeatureEditor(NewCooldownFeature cooldownFeature) {
        super("&lCooldown feature Editor", 3 * 9);
        this.cooldownFeature = cooldownFeature.clone(cooldownFeature.getParent());
        load();
    }

    @Override
    public void load() {
        cooldownFeature.getCooldown().initAndUpdateItemParentEditor(this, 0);
        cooldownFeature.getIsCooldownInTicks().initAndUpdateItemParentEditor(this, 1);
        cooldownFeature.getDisplayCooldownMessage().initAndUpdateItemParentEditor(this, 2);
        cooldownFeature.getCooldownMessage().initAndUpdateItemParentEditor(this, 3);
        cooldownFeature.getCancelEventIfInCooldown().initAndUpdateItemParentEditor(this, 4);

        // Back
        createItem(RED, 1, 18, GUI.BACK, false, false);

        // Reset menu
        createItem(ORANGE, 1, 19, GUI.RESET, false, false, "", "&c&oClick here to reset");

        // Save menu
        createItem(GREEN, 1, 26, GUI.SAVE, false, false, "", "&a&oClick here to save");
    }

    @Override
    public NewCooldownFeature getParent() {
        return cooldownFeature;
    }
}
