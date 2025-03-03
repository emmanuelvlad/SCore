package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* TELEPORTONCURSOR {range}:Integer {acceptAir}:boolean */
public class UnsafeTeleportOnCursor extends PlayerCommand {

    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {
        int amount = 200;
        if (args.size() >= 1) {
            amount = Double.valueOf(args.get(0)).intValue();
            if (amount <= 0) amount = 200;
        }
        try {

            Location eyeLoc = receiver.getEyeLocation();
            Vector eyeVec = eyeLoc.getDirection();
            Vector checkVec = eyeVec.clone().multiply(2);
            Location checkLoc = eyeLoc.clone().add(checkVec);

            int cpt = 2;
            /* Invalid if its a cheat movement */
            Block lastAirBlock = null;
            while (cpt < amount && isAirBlock((checkLoc = eyeLoc.clone().add(eyeVec.clone().multiply(cpt))).getBlock())) {
                if (!isAirBlock(checkLoc.clone().getBlock())) {
                    break;
                } else lastAirBlock = checkLoc.clone().getBlock();
                cpt++;
            }

            if (lastAirBlock != null && TeleportOnCursorManager.getInstance().canTp(receiver.getUniqueId())) {
                Location toTeleport = lastAirBlock.getLocation();
                toTeleport.setPitch(receiver.getLocation().getPitch());
                toTeleport.setYaw(receiver.getLocation().getYaw());
                receiver.teleport(toTeleport);
            }

        } catch (Exception ignored) {
        }
    }

    public boolean isAirBlock(Block block) {
        Material mat = block.getType();
        return (SCore.is1v16Plus() && mat.isAir()) || (!SCore.is1v16Plus() && mat.equals(Material.AIR));
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";

        if (args.size() >= 1) {
            ArgumentChecker ac2 = checkInteger(args.get(1), isFinalVerification, getTemplate());
            if (!ac2.isValid()) return Optional.of(ac2.getError());
        }

        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("UNSAFE_TELEPORTONCURSOR");
        return names;
    }

    @Override
    public String getTemplate() {
        return "UNSAFE_TELEPORTONCURSOR [maxRange]";
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
