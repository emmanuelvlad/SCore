package com.ssomar.score.newprojectiles.features;

import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.types.BooleanFeature;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class BounceFeature extends BooleanFeature implements SProjectileFeatureInterface {

    public BounceFeature(FeatureParentInterface parent) {
        super(parent, "bounce", false, "Bounce", new String[]{}, Material.SLIME_BLOCK, false, false);
    }

    public void transformTheProjectile(Entity e, Player launcher, Material materialLaunched) {
        if (e instanceof Projectile) {
            ((Projectile) e).setBounce(getValue());
        }
    }
}
