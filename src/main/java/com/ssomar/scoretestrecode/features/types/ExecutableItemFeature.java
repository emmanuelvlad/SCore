package com.ssomar.scoretestrecode.features.types;

import com.ssomar.score.SCore;
import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import com.ssomar.scoretestrecode.editor.NewGUIManager;
import com.ssomar.scoretestrecode.features.FeatureAbstract;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.FeatureRequireOnlyClicksInEditor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
@Setter
public class ExecutableItemFeature extends FeatureAbstract<Optional<ExecutableItemInterface>, ExecutableItemFeature> implements FeatureRequireOnlyClicksInEditor {

    private Optional<String> value;

    public ExecutableItemFeature(FeatureParentInterface parent, String name, String editorName, String[] editorDescription, Material editorMaterial, boolean requirePremium) {
        super(parent, name, editorName, editorDescription, editorMaterial, requirePremium);
        this.value = Optional.empty();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        String colorStr = config.getString(this.getName(), "");
        if (colorStr.isEmpty()) {
            value = Optional.empty();
        } else {
            value = Optional.of(colorStr);
        }
        return errors;
    }

    @Override
    public void save(ConfigurationSection config) {
        if (value.isPresent()) config.set(this.getName(), value.get());
    }

    @Override
    public Optional<ExecutableItemInterface> getValue() {
        if (value.isPresent() && SCore.hasExecutableItems && ExecutableItemsAPI.getExecutableItemsManager().isValidID(value.get()))
            return ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(value.get());
        else return Optional.empty();
    }

    @Override
    public ExecutableItemFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 3];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 3] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 2] = "&8>> &6SHIFT : &eBOOST SCROLL";
        finalDescription[finalDescription.length - 1] = "&8>> &6UP: &eRIGHT | &6DOWN: &eLEFT";

        ItemStack item = new ItemStack(getEditorMaterial());
        if (value.isPresent() && SCore.hasExecutableItems && ExecutableItemsAPI.getExecutableItemsManager().isValidID(value.get())) {
            item = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(value.get()).get().buildItem(1, Optional.empty(), Optional.empty());
        }

        gui.createItem(item, 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {
        if (SCore.hasExecutableItems) {
            if (value.isPresent() && ExecutableItemsAPI.getExecutableItemsManager().isValidID(value.get()))
                updateExecutableItem(value.get(), gui);
            else
                updateExecutableItem(ExecutableItemsAPI.getExecutableItemsManager().getExecutableItemIdsList().get(0), gui);
        } else
            updateExecutableItem(null, gui);
    }

    @Override
    public ExecutableItemFeature clone(FeatureParentInterface newParent) {
        ExecutableItemFeature clone = new ExecutableItemFeature(newParent, this.getName(), getEditorName(), getEditorDescription(), getEditorMaterial(), requirePremium());
        clone.value = value;
        return clone;
    }

    @Override
    public void reset() {
        this.value = Optional.empty();
    }

    @Override
    public void clickParentEditor(Player editor, NewGUIManager manager) {
        return;
    }

    @Override
    public boolean noShiftclicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean noShiftLeftclicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean noShiftRightclicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean shiftClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean shiftLeftClicked(Player editor, NewGUIManager manager) {
        if (SCore.hasExecutableItems) {
            String id = getExecutableItem((GUI) manager.getCache().get(editor)).get();
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            id = nextExecutableItem(id);
            updateExecutableItem(id, (GUI) manager.getCache().get(editor));
        }
        return true;
    }

    @Override
    public boolean shiftRightClicked(Player editor, NewGUIManager manager) {
        if (SCore.hasExecutableItems) {
            String id = getExecutableItem((GUI) manager.getCache().get(editor)).get();
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            id = prevExecutableItem(id);
            updateExecutableItem(id, (GUI) manager.getCache().get(editor));
        }
        return true;
    }

    @Override
    public boolean leftClicked(Player editor, NewGUIManager manager) {
        if (SCore.hasExecutableItems)
            updateExecutableItem(nextExecutableItem(getExecutableItem((GUI) manager.getCache().get(editor)).get()), (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean rightClicked(Player editor, NewGUIManager manager) {
        if (SCore.hasExecutableItems)
            updateExecutableItem(prevExecutableItem(getExecutableItem((GUI) manager.getCache().get(editor)).get()), (GUI) manager.getCache().get(editor));
        return true;
    }

    public String nextExecutableItem(String id) {
        boolean next = false;
        for (String check : getSortExecutableItems()) {
            if (check.equals(id)) {
                next = true;
                continue;
            }
            if (next) return check;
        }
        return getSortExecutableItems().get(0);
    }

    public String prevExecutableItem(String id) {
        int i = -1;
        int cpt = 0;
        for (String check : getSortExecutableItems()) {
            if (check.equals(id)) {
                i = cpt;
                break;
            }
            cpt++;
        }
        if (i == 0) return getSortExecutableItems().get(getSortExecutableItems().size() - 1);
        else return getSortExecutableItems().get(cpt - 1);
    }

    public void updateExecutableItem(String id, GUI gui) {
        if (id == null) id = "NULLLLL";
        value = Optional.of(id);
        ItemStack item = gui.getByName(getEditorName());
        int slot = gui.getInv().first(item);
        initItemParentEditor(gui, slot);
        item = gui.getByName(getEditorName());

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore().subList(0, getEditorDescription().length + 3);
        if (SCore.hasExecutableItems) {
            boolean find = false;
            for (String check : getSortExecutableItems()) {
                if (id.equals(check)) {
                    lore.add(StringConverter.coloredString("&2➤ &a" + id));
                    find = true;
                } else if (find) {
                    if (lore.size() == 17) break;
                    lore.add(StringConverter.coloredString("&6✦ &e" + check));
                }
            }
            for (String check : getSortExecutableItems()) {
                if (lore.size() == 17) break;
                else {
                    lore.add(StringConverter.coloredString("&6✦ &e" + check));
                }
            }
        } else {
            lore.add(StringConverter.coloredString("&4➤ &cYou must have &6ExecutableItems"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        /* Update the gui only for the right click , for the left it updated automaticaly idk why */
        for (HumanEntity e : gui.getInv().getViewers()) {
            if (e instanceof Player) {
                Player p = (Player) e;
                p.updateInventory();
            }
        }
    }

    public Optional<String> getExecutableItem(GUI gui) {
        ItemStack item = gui.getByName(getEditorName());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for (String str : lore) {
            if (str.contains("➤ ")) {
                if (str.contains("You must have &6ExecutableItems")) return Optional.empty();
                str = StringConverter.decoloredString(str).replaceAll(" Premium", "");
                return Optional.ofNullable(str.split("➤ ")[1]);
            }
        }
        return Optional.empty();
    }

    public List<String> getSortExecutableItems() {
        SortedMap<String, String> map = new TreeMap<String, String>();
        if (SCore.hasExecutableItems) {
            for (String id : ExecutableItemsAPI.getExecutableItemsManager().getExecutableItemIdsList()) {
                map.put(id, id);
            }
        }
        return new ArrayList<>(map.values());
    }

}
