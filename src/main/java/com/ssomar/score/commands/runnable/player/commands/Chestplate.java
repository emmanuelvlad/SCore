package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/* HEAD */
public class Chestplate extends PlayerCommand {

    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {

        PlayerInventory inv = receiver.getInventory();
        ItemStack item = inv.getItemInMainHand();
        ItemStack headItem = inv.getChestplate();
        if (!item.getType().equals(Material.AIR)) {
            if (headItem != null) {
                Map<Enchantment, Integer> enchants = headItem.getEnchantments();
                if (enchants.containsKey(Enchantment.BINDING_CURSE)) return;
            }
            inv.setChestplate(item);
            inv.setItemInMainHand(headItem);
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
        names.add("CHESTPLATE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "CHESTPLATE";
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
