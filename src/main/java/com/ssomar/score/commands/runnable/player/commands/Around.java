package com.ssomar.score.commands.runnable.player.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.CommandsExecutor;
import com.ssomar.score.commands.runnable.player.PlayerCommand;
import com.ssomar.score.commands.runnable.player.PlayerRunCommandsBuilder;
import com.ssomar.score.configs.messages.Message;
import com.ssomar.score.configs.messages.MessageMain;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* AROUND {distance} {true or false} {Your commands here} */
public class Around extends PlayerCommand {

    public static void aroundExecution(Entity receiver, List<String> args, ActionInfo aInfo) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    double distance = Double.parseDouble(args.get(0));
                    int cpt = 0;

                    for (Entity e : receiver.getNearbyEntities(distance, distance, distance)) {
                        if (e instanceof Player) {
                            Player target = (Player) e;
                            if (target.hasMetadata("NPC") || target.equals(receiver)) continue;

                            ActionInfo aInfo2 = aInfo.clone();
                            aInfo2.setReceiverUUID(target.getUniqueId());

                            StringPlaceholder sp = new StringPlaceholder();
                            sp.setAroundTargetPlayerPlcHldr(target.getUniqueId());

                            /* regroup the last args that correspond to the commands */
                            StringBuilder prepareCommands = new StringBuilder();
                            for (String s : args.subList(2, args.size())) {
                                prepareCommands.append(s);
                                prepareCommands.append(" ");
                            }
                            prepareCommands.deleteCharAt(prepareCommands.length() - 1);

                            String buildCommands = prepareCommands.toString();
                            String[] tab;
                            if (buildCommands.contains("<+>")) tab = buildCommands.split("\\<\\+\\>");
                            else {
                                tab = new String[1];
                                tab[0] = buildCommands;
                            }
                            List<String> commands = new ArrayList<>();
                            for (int m = 0; m < tab.length; m++) {
                                String s = tab[m];
                                while (s.startsWith(" ")) {
                                    s = s.substring(1);
                                }
                                while (s.endsWith(" ")) {
                                    s = s.substring(0, s.length() - 1);
                                }
                                if (s.startsWith("/")) s = s.substring(1);

                                s = sp.replacePlaceholder(s);
                                commands.add(s);
                            }
                            PlayerRunCommandsBuilder builder = new PlayerRunCommandsBuilder(commands, aInfo2);
                            CommandsExecutor.runCommands(builder);
                            cpt++;
                        }
                    }
                    if (cpt == 0 && Boolean.parseBoolean(args.get(1)) && receiver instanceof Player)
                        sm.sendMessage(receiver, MessageMain.getInstance().getMessage(SCore.plugin, Message.NO_PLAYER_HIT));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        runnable.runTask(SCore.plugin);
    }

    @Override
    public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {
        aroundExecution(receiver, args, aInfo);
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String error = "";

        String around = "AROUND {distance} {DisplayMsgIfNoPlayer true or false} {Your commands here}";
        if (args.size() < 3) error = notEnoughArgs + around;
        else if (args.size() > 3) {
            try {
                Double.valueOf(args.get(0));

                if (Boolean.valueOf(args.get(1)) == null)
                    error = invalidBoolean + args.get(1) + " for command: " + around;

            } catch (NumberFormatException e) {
                error = invalidDistance + args.get(0) + " for command: " + around;
            }
        }

        return error.isEmpty() ? Optional.empty() : Optional.of(error);
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("AROUND");
        return names;
    }

    @Override
    public String getTemplate() {
        return "AROUND {distance} {DisplayMsgIfNoPlayer true or false} {Your commands here}";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public ChatColor getExtraColor() {
        return ChatColor.DARK_PURPLE;
    }

}
