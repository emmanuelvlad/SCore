package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.RunConsoleCommand;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import com.ssomar.score.usedapi.WorldGuardAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* REPLACEBLOCK {material} */
public class ReplaceBlock extends PlayerCommand {

    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {

        Block block = receiver.getTargetBlock(null, 5);
        if (block.getType() != Material.AIR) {
            if (Material.matchMaterial(args.get(0).toUpperCase()) != null) {
                if (SCore.hasWorldGuard) {
                    if (new WorldGuardAPI().canBuild(receiver, new Location(block.getWorld(), block.getX(), block.getY(), block.getZ()))) {
                        block.setType(Material.valueOf(args.get(0).toUpperCase()));
                    }
                } else {
                    block.setType(Material.valueOf(args.get(0).toUpperCase()));
                }
            } else {
                RunConsoleCommand.runConsoleCommand("execute at " + receiver.getName() + " run setblock " + block.getX() + " " + block.getY() + " " + block.getZ() + " " + args.get(0).toLowerCase(), aInfo.isSilenceOutput());
            }
        }
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";

        if (args.size() < 1) return Optional.of(notEnoughArgs + getTemplate());

        ArgumentChecker ac = checkMaterial(args.get(0), isFinalVerification, getTemplate());
        if (!ac.isValid()) return Optional.of(ac.getError());

        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("REPLACEBLOCK");
        return names;
    }

    @Override
    public String getTemplate() {
        return "REPLACEBLOCK {material}";
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
