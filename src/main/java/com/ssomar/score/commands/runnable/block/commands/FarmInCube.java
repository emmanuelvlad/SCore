package com.ssomar.score.commands.runnable.block.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.SsomarDev;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.block.BlockCommand;
import com.ssomar.score.utils.ToolsListMaterial;
import com.ssomar.score.utils.safebreak.SafeBreak;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/* FARMINCUBE {radius} {ActiveDrop true or false} {onlyMaxAge true or false} {replant true or false}*/
public class FarmInCube extends BlockCommand {

    private static final boolean DEBUG = false;

    public static void destroyTheBlock(Block toDestroy, boolean onlyMaxAge, boolean drop, boolean replant, @Nullable Player p, boolean event, int slot) {


        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                BlockData data = toDestroy.getState().getBlockData().clone();
                Material bMat = toDestroy.getType();

                if (onlyMaxAge && data instanceof Ageable) {
                    Ageable ageable = (Ageable) data;
                    if (ageable.getAge() != ageable.getMaximumAge()) return;
                }

                if (ToolsListMaterial.getInstance().getPlantWithGrowth().contains(bMat)) {
                    UUID uuid = null;
                    if (p != null) uuid = p.getUniqueId();
                    SafeBreak.breakBlockWithEvent(toDestroy, uuid, slot, drop, event, true);
                    if (replant) replant(toDestroy, data, bMat, p);
                }

            }
        };
        runnable.runTask(SCore.plugin);
    }

    public static void replant(Block block, BlockData oldData, Material material, @Nullable Player player) {

        boolean needReplant = false;
        if (oldData instanceof Ageable) {
            Ageable ageable = (Ageable) oldData;

            Material required = ToolsListMaterial.getInstance().getRealMaterialOfBlock(material);

            if (player != null) {
                Inventory inv = player.getInventory();
                if (inv.contains(required) && inv.removeItem(new ItemStack(required)).isEmpty()) needReplant = true;
                else {
                    block.setType(Material.AIR);
                }
            } else needReplant = true;
            if (needReplant) {
                ageable.setAge(0);
                block.setType(material);
                block.setBlockData(oldData);
            }
        } else block.setType(Material.AIR);
    }

    @Override
    public void run(Player p, @NotNull Block block, Material oldMaterial, List<String> args, ActionInfo aInfo) {

        if (aInfo.isEventCallByMineInCube()) return;

        List<Material> validMaterial = ToolsListMaterial.getInstance().getPlantWithGrowth();

        try {
            int radius = Integer.parseInt(args.get(0));

            boolean drop = true;
            if (args.size() >= 2) drop = Boolean.parseBoolean(args.get(1));

            boolean onlyMaxAge = true;
            if (args.size() >= 3) onlyMaxAge = Boolean.parseBoolean(args.get(2));

            boolean replant = false;
            if (args.size() >= 4) replant = Boolean.parseBoolean(args.get(3));

            boolean event = false;
            if (args.size() >= 5) event = Boolean.parseBoolean(args.get(4));

            if (radius >= 10) radius = 9;
            for (int y = -radius; y < radius + 1; y++) {
                for (int x = -radius; x < radius + 1; x++) {
                    for (int z = -radius; z < radius + 1; z++) {

                        Block toDestroy = block.getWorld().getBlockAt(block.getX() + x, block.getY() + y, block.getZ() + z);

                        destroyTheBlock(toDestroy, onlyMaxAge, drop, replant, p, event, aInfo.getSlot());
                    }
                }
            }

            SsomarDev.testMsg("OldMaterial : " + oldMaterial.toString(), DEBUG);
            if (validMaterial.contains(oldMaterial) && replant) {
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(oldMaterial);
                        BlockData data = block.getState().getBlockData().clone();
                        replant(block, data, oldMaterial, p);
                    }
                };
                runnable.runTask(SCore.plugin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";
        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("FARMINCUBE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "FARMINCUBE {radius} [ActiveDrop true or false] [onlyMaxAge true or false] [replant true or false] [event true or false]";
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
