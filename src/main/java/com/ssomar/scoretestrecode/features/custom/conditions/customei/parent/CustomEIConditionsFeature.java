package com.ssomar.scoretestrecode.features.custom.conditions.customei.parent;

import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.custom.conditions.customei.CustomEIConditionFeature;
import com.ssomar.scoretestrecode.features.custom.conditions.customei.condition.IfNeedPlayerConfirmation;
import com.ssomar.scoretestrecode.features.custom.conditions.customei.condition.IfNotOwnerOfTheEI;
import com.ssomar.scoretestrecode.features.custom.conditions.customei.condition.IfOwnerOfTheEI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class CustomEIConditionsFeature extends FeatureWithHisOwnEditor<CustomEIConditionsFeature, CustomEIConditionsFeature, CustomEIConditionsFeatureEditor, CustomEIConditionsFeatureEditorManager> {

    private List<CustomEIConditionFeature> conditions;

    public CustomEIConditionsFeature(FeatureParentInterface parent, String name, String editorName, String[] editorDescription) {
        super(parent, name, editorName, editorDescription, Material.ANVIL, false);
        reset();
    }

    @Override
    public void reset() {
        conditions = new ArrayList<>();
        /** Boolean features **/
        conditions.add(new IfNeedPlayerConfirmation(this));
        conditions.add(new IfOwnerOfTheEI(this));
        conditions.add(new IfNotOwnerOfTheEI(this));

    }

    public boolean isNeedConfirmation() {
        for (CustomEIConditionFeature condition : conditions) {
            if (condition instanceof IfNeedPlayerConfirmation) {
                return ((IfNeedPlayerConfirmation) condition).getCondition().getValue();
            }
        }
        return false;
    }

    public String getNeedConfirmationMessage() {
        for (CustomEIConditionFeature condition : conditions) {
            if (condition instanceof IfNeedPlayerConfirmation) {
                return ((IfNeedPlayerConfirmation) condition).getErrorMessage().getValue().get();
            }
        }
        return "";
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(getName())) {
            ConfigurationSection section = config.getConfigurationSection(getName());
            for (CustomEIConditionFeature condition : conditions) {
                error.addAll(condition.load(plugin, section, isPremiumLoading));
            }
        }

        return error;
    }

    public boolean verifConditions(Player player, ItemStack itemStack, Optional<Player> playerOpt, SendMessage messageSender, @Nullable Event event) {
        for (CustomEIConditionFeature condition : conditions) {
            if (!condition.verifCondition(player, itemStack, playerOpt, messageSender, event)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void save(ConfigurationSection config) {
        config.set(getName(), null);
        ConfigurationSection section = config.createSection(getName());
        for (CustomEIConditionFeature condition : conditions) {
            condition.save(section);
        }
    }

    @Override
    public CustomEIConditionsFeature getValue() {
        return this;
    }

    @Override
    public CustomEIConditionsFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = "&7Block condition(s) enabled: &e" + getBlockConditionEnabledCount();
        finalDescription[finalDescription.length - 1] = gui.CLICK_HERE_TO_CHANGE;


        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    public int getBlockConditionEnabledCount() {
        int i = 0;
        for (CustomEIConditionFeature condition : conditions) {
            if (condition.hasCondition()) {
                i++;
            }
        }
        return i;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public CustomEIConditionsFeature clone(FeatureParentInterface newParent) {
        CustomEIConditionsFeature clone = new CustomEIConditionsFeature(newParent, getName(), getEditorName(), getEditorDescription());
        List<CustomEIConditionFeature> clones = new ArrayList<>();
        for (CustomEIConditionFeature condition : conditions) {
            clones.add((CustomEIConditionFeature) condition.clone(clone));
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
        if (section.isConfigurationSection(this.getName())) {
            return section.getConfigurationSection(this.getName());
        } else return section.createSection(this.getName());
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for (FeatureInterface feature : getParent().getFeatures()) {
            if (feature instanceof CustomEIConditionsFeature && feature.getName().equals(getName())) {
                CustomEIConditionsFeature bCF = (CustomEIConditionsFeature) feature;
                List<CustomEIConditionFeature> clones = new ArrayList<>();
                for (CustomEIConditionFeature condition : conditions) {
                    clones.add((CustomEIConditionFeature) condition);
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
        CustomEIConditionsFeatureEditorManager.getInstance().startEditing(player, this);
    }

}
