package com.ssomar.scoretestrecode.features.custom.conditions.item.parent;

import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.scoretestrecode.editor.NewGUIManager;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.custom.conditions.item.ItemConditionFeature;
import com.ssomar.scoretestrecode.features.custom.conditions.item.condition.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemConditionsFeature extends FeatureWithHisOwnEditor<ItemConditionsFeature, ItemConditionsFeature, ItemConditionsFeatureEditor, ItemConditionsFeatureEditorManager> {

    private List<ItemConditionFeature> conditions;

    public ItemConditionsFeature(FeatureParentInterface parent, String name, String editorName, String[] editorDescription) {
        super(parent, name, editorName, editorDescription, Material.ANVIL, false);
        reset();
    }

    @Override
    public void reset() {
        conditions = new ArrayList<>();
        /** Boolean features **/
        conditions.add(new IfCrossbowMustBeCharged(this));
        conditions.add(new IfCrossbowMustNotBeCharged(this));

        /** Number condition **/
        conditions.add(new IfDurability(this));
        conditions.add(new IfUsage(this));

        /** ListEnchantWithLevel **/
        conditions.add(new IfHasEnchant(this));
        conditions.add(new IfHasNotEnchant(this));

    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        for(ItemConditionFeature condition : conditions) {
            error.addAll(condition.load(plugin, config, isPremiumLoading));
        }

        return error;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(getName(), null);
        ConfigurationSection section = config.createSection(getName());
        for (ItemConditionFeature condition : conditions) {
            condition.save(section);
        }
    }

    @Override
    public ItemConditionsFeature getValue() {
        return this;
    }

    @Override
    public ItemConditionsFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = "&7Item condition(s) enabled: &e" + getBlockConditionEnabledCount();
        finalDescription[finalDescription.length - 1] = gui.CLICK_HERE_TO_CHANGE;


        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    public int getBlockConditionEnabledCount() {
        int i = 0;
        for(ItemConditionFeature condition : conditions) {
            if(condition.hasCondition()) {
                i++;
            }
        }
        return i;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public void extractInfoFromParentEditor(NewGUIManager manager, Player player) {

    }

    @Override
    public ItemConditionsFeature clone() {
        ItemConditionsFeature clone = new ItemConditionsFeature(getParent(), getName(), getEditorName(), getEditorDescription());
        List<ItemConditionFeature> clones = new ArrayList<>();
        for (ItemConditionFeature condition : conditions) {
            clones.add((ItemConditionFeature) condition.clone());
        }
        clone.setConditions(clones);
        return clone;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(conditions);
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo();
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        ConfigurationSection section = getParent().getConfigurationSection();
        if(section.isConfigurationSection(this.getName())) {
            return section.getConfigurationSection(this.getName());
        }
        else return section.createSection(this.getName());
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for (FeatureInterface feature : getParent().getFeatures()) {
            if (feature instanceof ItemConditionsFeature) {
                ItemConditionsFeature bCF = (ItemConditionsFeature) feature;
                List<ItemConditionFeature> clones = new ArrayList<>();
                for (ItemConditionFeature condition : conditions) {
                    clones.add((ItemConditionFeature) condition);
                }
                bCF.setConditions(clones);
                break;
            }
        }
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public void openEditor(@NotNull Player player) {
        ItemConditionsFeatureEditorManager.getInstance().startEditing(player, this);
    }

}
