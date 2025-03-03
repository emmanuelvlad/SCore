package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import com.ssomar.score.commands.runnable.player.events.StunEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StunDisable extends PlayerCommand {

    @SuppressWarnings("deprecation")
    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {
        StunEvent.stunPlayers.remove(receiver.getUniqueId());
        receiver.setGliding(false);
    }


    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";
        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("STUN_DISABLE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "STUN_DISABLE";
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