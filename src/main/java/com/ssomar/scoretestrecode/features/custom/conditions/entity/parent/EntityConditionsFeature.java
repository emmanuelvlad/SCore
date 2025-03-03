package com.ssomar.scoretestrecode.features.custom.conditions.entity.parent;

import com.ssomar.score.SCore;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.scoretestrecode.features.custom.conditions.entity.condition.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class EntityConditionsFeature extends FeatureWithHisOwnEditor<EntityConditionsFeature, EntityConditionsFeature, EntityConditionsFeatureEditor, EntityConditionsFeatureEditorManager> {

    private List<EntityConditionFeature> conditions;

    public EntityConditionsFeature(FeatureParentInterface parent, String name, String editorName, String[] editorDescription) {
        super(parent, name, editorName, editorDescription, Material.ANVIL, false);
        reset();
    }

    @Override
    public void reset() {
        conditions = new ArrayList<>();
        /** Boolean features **/
        conditions.add(new IfAdult(this));
        conditions.add(new IfBaby(this));
        conditions.add(new IfFrozen(this));
        conditions.add(new IfGlowing(this));
        conditions.add(new IfInvulnerable(this));
        /* Nameable not available in 1.8 */
        if (!SCore.is1v11Less()) conditions.add(new IfNamed(this));
        conditions.add(new IfOnFire(this));
        conditions.add(new IfPowered(this));
        conditions.add(new IfTamed(this));

        /** Number condition features **/
        conditions.add(new IfEntityHealth(this));

        /** List uncolored string **/
        conditions.add(new IfHasTag(this));
        conditions.add(new IfNotHasTag(this));

        /** List Material with tags **/
        conditions.add(new IfIsOnTheBlock(this));
        conditions.add(new IfIsNotOnTheBlock(this));

        /** List colored string **/
        conditions.add(new IfName(this));

        /** List EntityType **/
        conditions.add(new IfNotEntityType(this));

    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(getName())) {
            ConfigurationSection section = config.getConfigurationSection(getName());
            for (EntityConditionFeature condition : conditions) {
                error.addAll(condition.load(plugin, section, isPremiumLoading));
            }
        }

        return error;
    }

    public boolean verifConditions(Entity entity, Optional<Player> playerOpt, SendMessage messageSender, @Nullable Event event) {
        for (EntityConditionFeature condition : conditions) {
            if (!condition.verifCondition(entity, playerOpt, messageSender, event)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(getName(), null);
        ConfigurationSection section = config.createSection(getName());
        for (EntityConditionFeature condition : conditions) {
            condition.save(section);
        }
    }

    @Override
    public EntityConditionsFeature getValue() {
        return this;
    }

    @Override
    public EntityConditionsFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = "&7Entity condition(s) enabled: &e" + getEntityConditionEnabledCount();
        finalDescription[finalDescription.length - 1] = gui.CLICK_HERE_TO_CHANGE;


        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    public int getEntityConditionEnabledCount() {
        int i = 0;
        for (EntityConditionFeature condition : conditions) {
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
    public EntityConditionsFeature clone(FeatureParentInterface newParent) {
        EntityConditionsFeature clone = new EntityConditionsFeature(newParent, getName(), getEditorName(), getEditorDescription());
        List<EntityConditionFeature> clones = new ArrayList<>();
        for (EntityConditionFeature condition : conditions) {
            clones.add((EntityConditionFeature) condition.clone(clone));
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
            if (feature instanceof EntityConditionsFeature && feature.getName().equals(getName())) {
                EntityConditionsFeature bCF = (EntityConditionsFeature) feature;
                List<EntityConditionFeature> clones = new ArrayList<>();
                for (EntityConditionFeature condition : conditions) {
                    clones.add((EntityConditionFeature) condition);
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
        EntityConditionsFeatureEditorManager.getInstance().startEditing(player, this);
    }

}
