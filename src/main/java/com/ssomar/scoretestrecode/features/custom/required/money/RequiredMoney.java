package com.ssomar.scoretestrecode.features.custom.required.money;

import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.usedapi.VaultAPI;
import com.ssomar.scoretestrecode.features.FeatureInterface;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureWithHisOwnEditor;
import com.ssomar.scoretestrecode.features.custom.required.RequiredPlayerInterface;
import com.ssomar.scoretestrecode.features.types.BooleanFeature;
import com.ssomar.scoretestrecode.features.types.ColoredStringFeature;
import com.ssomar.scoretestrecode.features.types.IntegerFeature;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ssomar.score.menu.GUI.WRITABLE_BOOK;

@Getter
@Setter
public class RequiredMoney extends FeatureWithHisOwnEditor<RequiredMoney, RequiredMoney, RequiredMoneyEditor, RequiredMoneyEditorManager> implements RequiredPlayerInterface {

    private IntegerFeature money;
    private ColoredStringFeature errorMessage;
    private BooleanFeature cancelEventIfError;

    public RequiredMoney(FeatureParentInterface parent) {
        super(parent, "requiredMoney", "Required Money", new String[]{"&7&oRequired money", "&4&lRequire: &6Vault"}, Material.GOLD_BLOCK, true);
        reset();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(getName())) {
            ConfigurationSection section = config.getConfigurationSection(getName());
            if (!isPremiumLoading) {
                error.add("&cERROR, Couldn't load the Required Money value of " + this.getName() + " from config, &7&o" + getParent().getParentInfo() + " &6>> Because it's a premium feature !");
                return error;
            }
            money.load(plugin, section, isPremiumLoading);
            errorMessage.load(plugin, section, isPremiumLoading);
            cancelEventIfError.load(plugin, section, isPremiumLoading);
        }
        return error;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(this.getName(), null);
        if (money.getValue().isPresent() && money.getValue().get() > 0) {
            ConfigurationSection requiredMoneySection = config.createSection(this.getName());
            money.save(requiredMoneySection);
            errorMessage.save(requiredMoneySection);
            cancelEventIfError.save(requiredMoneySection);
        }
    }

    @Override
    public boolean verify(Player player, Event event) {
        VaultAPI v = new VaultAPI();
        if (money.getValue().isPresent() && money.getValue().get() > 0 && v.verifEconomy(player)) {
            String errMessage = "";
            if (errorMessage.getValue().isPresent()) {
                errMessage = errorMessage.getValue().get();
            }
            if (!v.hasMoney(player, money.getValue().get(), errMessage)) {
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
        if (money.getValue().isPresent() && money.getValue().get() > 0) {
            VaultAPI v = new VaultAPI();
            v.takeMoney(player, money.getValue().get());
        }
    }

    @Override
    public RequiredMoney getValue() {
        return this;
    }

    @Override
    public RequiredMoney initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        if (!isPremium() && requirePremium()) {
            finalDescription[finalDescription.length - 2] = gui.PREMIUM;
        } else finalDescription[finalDescription.length - 2] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = "&7&oRequired money: &e" + money.getValue().get();

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }


    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public RequiredMoney clone(FeatureParentInterface newParent) {
        RequiredMoney requiredLevel = new RequiredMoney(newParent);
        requiredLevel.setMoney(money.clone(requiredLevel));
        requiredLevel.setErrorMessage(errorMessage.clone(requiredLevel));
        requiredLevel.setCancelEventIfError(cancelEventIfError.clone(requiredLevel));
        return requiredLevel;
    }

    @Override
    public void reset() {
        this.money = new IntegerFeature(getParent(), "requiredMoney", Optional.of(0), "Required Money", new String[]{"&7&oRequired money"}, Material.ANVIL, false);
        this.errorMessage = new ColoredStringFeature(getParent(), "errorMessage", Optional.of("&4&l>> &cError you don't have the required money"), "Error message", new String[]{"&7&oEdit the error message"}, WRITABLE_BOOK, false, true);
        this.cancelEventIfError = new BooleanFeature(getParent(), "cancelEventIfError", false, "cancelEventIfInvalidRequiredMoney", new String[]{"&7&oCancel the vanilla event"}, Material.LEVER, false, true);
    }

    @Override
    public void openEditor(Player player) {
        if (!isPremium() && requirePremium()) return;
        RequiredMoneyEditorManager.getInstance().startEditing(player, this);
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return Arrays.asList(money, errorMessage, cancelEventIfError);
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo() + ".(requiredMoney)";
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
            if (feature instanceof RequiredMoney) {
                RequiredMoney requiredLevel = (RequiredMoney) feature;
                requiredLevel.setMoney(money);
                requiredLevel.setErrorMessage(errorMessage);
                requiredLevel.setCancelEventIfError(cancelEventIfError);
                break;
            }
        }
    }
}
