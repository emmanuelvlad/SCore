package com.ssomar.scoretestrecode.features.custom.required.items.group;

import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.FeaturesGroup;
import com.ssomar.scoretestrecode.features.custom.required.RequiredPlayerInterface;
import com.ssomar.scoretestrecode.features.custom.required.items.item.RequiredItemFeature;
import com.ssomar.scoretestrecode.features.types.BooleanFeature;
import com.ssomar.scoretestrecode.features.types.ColoredStringFeature;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

@Getter
@Setter
public class RequiredItemGroupFeature extends FeatureWithHisOwnEditor<RequiredItemGroupFeature, RequiredItemGroupFeature, RequiredItemGroupFeatureEditor, RequiredItemGroupFeatureEditorManager> implements FeaturesGroup<RequiredItemFeature>, RequiredPlayerInterface {

    private Map<String, RequiredItemFeature> requiredItems;
    private ColoredStringFeature errorMessage;
    private BooleanFeature cancelEventIfError;

    public RequiredItemGroupFeature(FeatureParentInterface parent) {
        super(parent, "requiredItems", "Required Items", new String[]{"&7&oThe required items"}, Material.DIAMOND, true);
        reset();
    }

    @Override
    public void reset() {
        this.requiredItems = new HashMap<>();
        this.errorMessage = new ColoredStringFeature(this, "errorMessage", Optional.of("&4&l>> &cError you don't have the required items"), "Error message", new String[]{"&7&oThe error message"}, GUI.WRITABLE_BOOK, false, true);
        this.cancelEventIfError = new BooleanFeature(this, "cancelEventIfError", false, "Cancel event if error", new String[]{"&7&oCancel the event if", "&7&othe player don't have", "&7&othe required items"}, Material.LEVER, false, true);
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(this.getName())) {
            ConfigurationSection requiredItemGroupSection = config.getConfigurationSection(this.getName());
            for (String attributeID : requiredItemGroupSection.getKeys(false)) {
                if (attributeID.equals(errorMessage.getName()) || attributeID.equals(cancelEventIfError.getName())) {
                    continue;
                }
                if (!isPremiumLoading && isRequirePremium()) {
                    error.add("&cERROR, Couldn't load the " + attributeID + " value of " + this.getEditorName() + " from config &6>> Because it's a premium feature !");
                    break;
                }
                RequiredItemFeature attribute = new RequiredItemFeature(this, attributeID);
                List<String> subErrors = attribute.load(plugin, requiredItemGroupSection, isPremiumLoading);
                if (subErrors.size() > 0) {
                    error.addAll(subErrors);
                    continue;
                }
                requiredItems.put(attributeID, attribute);
            }
            errorMessage.load(plugin, requiredItemGroupSection, isPremiumLoading);
            cancelEventIfError.load(plugin, requiredItemGroupSection, isPremiumLoading);
        }
        return error;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(this.getName(), null);
        ConfigurationSection attributesSection = config.createSection(this.getName());
        for (String enchantmentID : requiredItems.keySet()) {
            requiredItems.get(enchantmentID).save(attributesSection);
        }
        errorMessage.save(attributesSection);
        cancelEventIfError.save(attributesSection);
    }

    @Override
    public RequiredItemGroupFeature getValue() {
        return this;
    }

    @Override
    public RequiredItemGroupFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        if (!isPremium() && requirePremium()) {
            finalDescription[finalDescription.length - 2] = gui.PREMIUM;
        } else finalDescription[finalDescription.length - 2] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = "&7&oRequiredItem(s) added: &e" + requiredItems.size();

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public RequiredItemFeature getTheChildFeatureClickedParentEditor(String featureClicked) {
        for (RequiredItemFeature x : requiredItems.values()) {
            if (x.isTheFeatureClickedParentEditor(featureClicked)) return x;
        }
        return null;
    }

    @Override
    public RequiredItemGroupFeature clone(FeatureParentInterface newParent) {
        RequiredItemGroupFeature eF = new RequiredItemGroupFeature(newParent);
        HashMap<String, RequiredItemFeature> newRequiredItems = new HashMap<>();
        for (String key : requiredItems.keySet()) {
            newRequiredItems.put(key, requiredItems.get(key).clone(eF));
        }
        eF.setRequiredItems(newRequiredItems);
        eF.setErrorMessage(this.getErrorMessage().clone(eF));
        eF.setCancelEventIfError(this.getCancelEventIfError().clone(eF));
        eF.setPremium(this.isPremium());
        return eF;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        List<FeatureInterface> features = new ArrayList<>(requiredItems.values());
        features.add(errorMessage);
        features.add(cancelEventIfError);
        return features;
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
            if (feature instanceof RequiredItemGroupFeature) {
                RequiredItemGroupFeature eF = (RequiredItemGroupFeature) feature;
                eF.setRequiredItems(this.getRequiredItems());
                eF.setErrorMessage(this.getErrorMessage());
                eF.setCancelEventIfError(this.getCancelEventIfError());
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
        if (!isPremium() && requirePremium()) return;
        RequiredItemGroupFeatureEditorManager.getInstance().startEditing(player, this);
    }

    @Override
    public void createNewFeature(@NotNull Player editor) {
        String baseId = "requiredItem";
        for (int i = 0; i < 1000; i++) {
            String id = baseId + i;
            if (!requiredItems.containsKey(id)) {
                RequiredItemFeature eF = new RequiredItemFeature(this, id);
                requiredItems.put(id, eF);
                eF.openEditor(editor);
                break;
            }
        }
    }

    @Override
    public void deleteFeature(@NotNull Player editor, RequiredItemFeature feature) {
        requiredItems.remove(feature.getId());
    }

    @Override
    public boolean verify(Player player, Event event) {
        for (RequiredItemFeature feature : requiredItems.values()) {
            if (!feature.verify(player, event)) {
                if (errorMessage.getValue().isPresent()) {
                    SendMessage.sendMessageNoPlch(player, errorMessage.getValue().get());
                }
                if (cancelEventIfError.getValue() && event instanceof Cancellable) {
                    ((Cancellable) event).setCancelled(true);
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void take(Player player) {
        for (RequiredItemFeature feature : requiredItems.values()) {
            feature.take(player);
        }
    }
}
