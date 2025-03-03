package com.ssomar.score.newprojectiles.features;

import com.ssomar.score.utils.FixedMaterial;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.types.ColorIntegerFeature;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Arrays;
import java.util.Optional;

/* For Arrow and potion color */
public class ColorFeature extends ColorIntegerFeature implements SProjectileFeatureInterface {

    public ColorFeature(FeatureParentInterface parent) {
        super(parent, "color", Optional.empty(), "Color", new String[]{}, FixedMaterial.getMaterial(Arrays.asList("RED_DYE", "INK_SACK")), false);
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher, Material materialLaunched) {
        if (e instanceof Arrow && getValue().isPresent()) {
            ((Arrow) e).setColor(Color.fromRGB(getValue().get()));
        } else if (e instanceof ThrownPotion) {
            ThrownPotion lp = (ThrownPotion) e;

            try {
                ItemStack item = lp.getItem();
                if (materialLaunched.equals(Material.LINGERING_POTION)) item.setType(Material.LINGERING_POTION);
                else item.setType(Material.SPLASH_POTION);
                PotionMeta pMeta = (PotionMeta) item.getItemMeta();
                if (getValue().isPresent())
                    pMeta.setColor(Color.fromRGB(getValue().get()));
                item.setItemMeta(pMeta);
                lp.setItem(item);
            } catch (NoSuchMethodError exception) {
            }
        }
    }
}
