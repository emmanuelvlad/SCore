package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* SUDO {command} */
public class Sudo extends PlayerCommand {

    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {
        StringBuilder command2 = new StringBuilder();
        for (String s : args) {
            command2.append(s).append(" ");
        }
        command2 = new StringBuilder(command2.substring(0, command2.length() - 1));

		/*if(command2.startsWith("//")) {
			command2 = command2.substring(1, command2.length());
		}
		else */
        if (command2.toString().startsWith("/")) {
            command2 = new StringBuilder(command2.substring(1, command2.length()));
        }
        receiver.chat("/" + command2);
        //p.performCommand(finalCommand);
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";

        String sudo = "SUDO {command}";
        if (args.size() < 1) error = notEnoughArgs + sudo;

        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("SUDO");
        return names;
    }

    @Override
    public String getTemplate() {
        return "SUDO {command}";
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
