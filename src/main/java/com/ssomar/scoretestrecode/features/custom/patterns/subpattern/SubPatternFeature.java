package com.ssomar.scoretestrecode.features.custom.patterns.subpattern;

import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.types.ObjectFeature;
import com.ssomar.scoretestrecode.features.types.UncoloredStringFeature;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class SubPatternFeature extends FeatureWithHisOwnEditor<SubPatternFeature, SubPatternFeature, SubPatternFeatureEditor, SubPatternFeatureEditorManager> {

    private UncoloredStringFeature string;
    private ObjectFeature object;
    private String id;

    public SubPatternFeature(FeatureParentInterface parent, String id) {
        super(parent, "subPattern", "Sub Pattern", new String[]{"&7&oA sub pattern with its options"}, Material.ANVIL, false);
        this.id = id;
        reset();
    }

    @Override
    public void reset() {
        this.string = new UncoloredStringFeature(this, "string", Optional.empty(), "String", new String[]{"&7&oA string"}, Material.PAPER, false, false);
        this.object = new ObjectFeature(this, "object", "Object", new String[]{"&7&oAn object"}, Material.PAPER, false);
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        if (config.isConfigurationSection(id)) {
            ConfigurationSection enchantmentConfig = config.getConfigurationSection(id);
            errors.addAll(this.string.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.object.load(plugin, enchantmentConfig, isPremiumLoading));
        } else {
            errors.add("&cERROR, Couldn't load the Sub Pattern with its options because there is not section with the good ID: " + id + " &7&o" + getParent().getParentInfo());
        }
        return errors;
    }

    @Override
    public boolean isTheFeatureClickedParentEditor(String featureClicked) {
        return featureClicked.contains(getEditorName()) && featureClicked.contains("(" + id + ")");
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(id, null);
        ConfigurationSection attributeConfig = config.createSection(id);
        this.string.save(attributeConfig);
        this.object.save(attributeConfig);
    }

    @Override
    public SubPatternFeature getValue() {
        return this;
    }

    @Override
    public SubPatternFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 1];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 1] = gui.CLICK_HERE_TO_CHANGE;

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName() + " - " + "(" + id + ")", false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {
    }

    @Override
    public SubPatternFeature clone(FeatureParentInterface newParent) {
        SubPatternFeature eF = new SubPatternFeature(newParent, id);
        eF.setString(string.clone(eF));
        eF.setObject(object.clone(eF));
        return eF;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(Arrays.asList(string, object));
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo();
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return getParent().getConfigurationSection();
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for (FeatureInterface feature : getParent().getFeatures()) {
            if (feature instanceof SubPatternFeature) {
                SubPatternFeature aFOF = (SubPatternFeature) feature;
                if (aFOF.getId().equals(id)) {
                    aFOF.setString(string);
                    aFOF.setObject(object);
                    break;
                }
            }
        }
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public void openEditor(@NotNull Player player) {
        SubPatternFeatureEditorManager.getInstance().startEditing(player, this);
    }

}
