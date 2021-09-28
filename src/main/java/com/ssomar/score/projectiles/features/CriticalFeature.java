package com.ssomar.score.projectiles.features;

import com.ssomar.score.SCore;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import com.ssomar.score.projectiles.types.SProjectiles;
import com.ssomar.score.utils.StringConverter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CriticalFeature extends DecorateurCustomProjectiles {

    boolean isCritical;

    public CriticalFeature(CustomProjectile cProj){
        super.cProj = cProj;
        isCritical = false;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        isCritical = projConfig.getBoolean("critical", false);
        return cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if (!SCore.is1v12() && e instanceof AbstractArrow) {
            AbstractArrow aA = (AbstractArrow) e;
            aA.setCritical(isCritical);
        }
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI loadConfigGUI(SProjectiles sProj) {
        SimpleGUI gui = cProj.loadConfigGUI(sProj);
        gui.addItem(Material.DIAMOND_AXE, 1, gui.TITLE_COLOR+"Critical", false, false, gui.CLICK_HERE_TO_CHANGE, "&7actually: ");
        gui.updateBoolean(gui.TITLE_COLOR+"Critical", isCritical);
        return gui;
    }

    @Override
    public boolean interactionConfigGUI(GUI gui, Player player, ItemStack itemS, String title) {
        if(cProj.interactionConfigGUI(gui, player, itemS, title)) return true;
        String itemName = StringConverter.decoloredString(itemS.getItemMeta().getDisplayName());
        String changeBounce = StringConverter.decoloredString(gui.TITLE_COLOR+"Critical");

        if(itemName.equals(changeBounce)) {
            boolean bool = gui.getBoolean(gui.TITLE_COLOR+"Critical");
            gui.updateBoolean(gui.TITLE_COLOR+"Critical", !bool);
        }
        else return false;
        return true;
    }

}
