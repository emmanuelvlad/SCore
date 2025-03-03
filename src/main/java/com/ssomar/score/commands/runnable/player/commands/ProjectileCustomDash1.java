package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import com.ssomar.score.nofalldamage.NoFallDamageManager;
import com.ssomar.score.utils.Couple;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/* CUSTOMDASH1 {x} {y} {z} */
public class ProjectileCustomDash1 extends PlayerCommand {

    private static void pullEntityToLocation(Entity e, Location loc) {
        Location entityLoc = e.getLocation();
        entityLoc.setY(entityLoc.getY() + 0.5D);
        e.teleport(entityLoc);
        double g = -0.08D;
        double d = loc.distance(entityLoc);
        double t = d;
        double v_x = (1.0D + 0.07D * t) * (loc.getX() - entityLoc.getX()) / t;
        double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t - 0.5D * g * t;
        double v_z = (1.0D + 0.07D * t) * (loc.getZ() - entityLoc.getZ()) / t;
        Vector v = e.getVelocity();
        v.setX(v_x);
        v.setY(v_y);
        v.setZ(v_z);
        e.setVelocity(v);
    }

    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {

        boolean fallDamage = false;
        if (args.size() >= 1) {
            fallDamage = Boolean.valueOf(args.get(0));
        }

        Projectile proj2 = null;
        for (Entity e : receiver.getNearbyEntities(75, 75, 75)) {
            if (e instanceof Projectile) {
                Projectile proj = (Projectile) e;
                if (proj.getShooter() instanceof Player) {
                    Player owner = (Player) proj.getShooter();
                    if (owner.getUniqueId().equals(receiver.getUniqueId())) {
                        proj2 = proj;
                        if (p.getLocation().distance(proj2.getLocation()) < 2.5) return;
                        break;
                    } else continue;
                } else continue;
            } else continue;
        }

        if (proj2 == null) return;

        pullEntityToLocation(receiver, proj2.getLocation());

        UUID uuid = UUID.randomUUID();

        if (!fallDamage) {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    NoFallDamageManager.getInstance().removeNoFallDamage(p, uuid);
                }
            };
            BukkitTask task = runnable.runTaskLater(SCore.plugin, 300);

            NoFallDamageManager.getInstance().addNoFallDamage(receiver, new Couple<>(uuid, task));
        }

    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";

        if (args.size() >= 1) {
            ArgumentChecker ac = checkBoolean(args.get(0), isFinalVerification, getTemplate());
            if (!ac.isValid()) return Optional.of(ac.getError());
        }

        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("PROJECTILE_CUSTOMDASH1");
        return names;
    }

    @Override
    public String getTemplate() {
        return "PROJECTILE_CUSTOMDASH1 [fallDamage true or false]";
    }

    @Override
    public ChatColor getColor() {
        return null;
    }

    @Override
    public ChatColor getExtraColor() {
        return null;
    }
}
