package com.ssomar.scoretestrecode.features.custom.activators.group;

import com.ssomar.executableblocks.executableblocks.activators.Option;
import com.ssomar.score.SCore;
import com.ssomar.score.SsomarDev;
import com.ssomar.score.events.loop.LoopManager;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.FeaturesGroup;
import com.ssomar.scoretestrecode.features.custom.activators.activator.NewSActivator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
public class ActivatorsFeature extends FeatureWithHisOwnEditor<ActivatorsFeature, ActivatorsFeature, ActivatorsFeatureEditor, ActivatorsFeatureEditorManager> implements FeaturesGroup<NewSActivator> {

    private LinkedHashMap<String, NewSActivator> activators;
    private NewSActivator builderInstance;
    private int premiumLimit = 1;

    public ActivatorsFeature(FeatureParentInterface parent, NewSActivator builderInstance) {
        super(parent, "activators", "Activators", new String[]{"&7&oThe activators / triggers"}, Material.BEACON, false);
        this.builderInstance = builderInstance;
        reset();
    }

    @Override
    public void reset() {
        this.activators = new LinkedHashMap<>();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(this.getName())) {
            ConfigurationSection activatorsSection = config.getConfigurationSection(this.getName());
            for (String activatorID : activatorsSection.getKeys(false)) {
                if (activators.size() >= premiumLimit && !isPremium()) {
                    error.add("&cERROR, Couldn't load the Activator of " + activatorID + " from config, &7&o" + getParent().getParentInfo() + " &6>> Because it requires the premium version to have more than 1 activator !");
                    return error;
                }
                NewSActivator activator = builderInstance.getBuilderInstance(this, activatorID);
                List<String> subErrors = activator.load(plugin, activatorsSection, isPremiumLoading);
                if (subErrors.size() > 0) {
                    error.addAll(subErrors);
                }
                //SsomarDev.testMsg("Activator " + activatorID + " loaded");
                if (activator.getOption().isLoopOption()) {
                    //SsomarDev.testMsg("Activator " + activatorID + " is a loop activator");
                    LoopManager.getInstance().getLoopActivators().put(activator, 0);
                    //SsomarDev.testMsg("loopmanager size: "+LoopManager.getInstance().getLoopActivators().size());
                }
                if (SCore.hasExecutableBlocks && activator.getOption().equals(Option.ENTITY_WALK_ON)) {
                    LoopManager.getInstance().getCheckEntityOnofEB().add(activator);
                }
                activators.put(activatorID, activator);
            }
        }
        return error;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(this.getName(), null);
        ConfigurationSection attributesSection = config.createSection(this.getName());
        for (String actId : activators.keySet()) {
            activators.get(actId).save(attributesSection);
        }
    }

    @Override
    public ActivatorsFeature getValue() {
        return this;
    }

    @Override
    public ActivatorsFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = "&7&oActivator(s) added: &e" + activators.size();

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public NewSActivator getTheChildFeatureClickedParentEditor(String featureClicked) {
        for (NewSActivator x : activators.values()) {
            if (x.isTheFeatureClickedParentEditor(featureClicked)) return x;
        }
        return null;
    }

    @Override
    public ActivatorsFeature clone() {
        return null;
    }

    @Override
    public ActivatorsFeature clone(FeatureParentInterface newParent) {
        ActivatorsFeature eF = new ActivatorsFeature(newParent, builderInstance);
        LinkedHashMap<String, NewSActivator> newActivators = new LinkedHashMap<>();
        for (String key : activators.keySet()) {
            NewSActivator clone = (NewSActivator) activators.get(key).clone(eF);
            newActivators.put(key, clone);
        }
        eF.setActivators(newActivators);
        return eF;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(activators.values());
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
            if (feature instanceof ActivatorsFeature) {
                ActivatorsFeature eF = (ActivatorsFeature) feature;
                eF.setActivators(activators);
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
        ActivatorsFeatureEditorManager.getInstance().startEditing(player, this);
    }

    @Override
    public void createNewFeature(@NotNull Player editor) {
        if (!isPremium() && activators.size() >= premiumLimit) return;
        String baseId = "activator";
        for (int i = 0; i < 1000; i++) {
            String id = baseId + i;
            if (!activators.containsKey(id)) {
                SsomarDev.testMsg("INSTANCE CREATE: " + this.hashCode());
                NewSActivator activator = builderInstance.getBuilderInstance(this, id);
                activators.put(id, activator);
                activator.openEditor(editor);
                break;
            }
        }
    }

    @Override
    public void deleteFeature(@NotNull Player editor, NewSActivator feature) {
        LoopManager.getInstance().getCheckEntityOnofEB().remove(activators.get(feature.getId()));
        LoopManager.getInstance().getLoopActivators().remove(activators.get(feature.getId()));
        activators.remove(feature.getId());
    }
}
