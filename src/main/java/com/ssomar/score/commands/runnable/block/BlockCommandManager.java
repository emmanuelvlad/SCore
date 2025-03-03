package com.ssomar.score.commands.runnable.block;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.CommandManager;
import com.ssomar.score.commands.runnable.SCommand;
import com.ssomar.score.commands.runnable.block.commands.*;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import org.bukkit.ChatColor;

import java.util.*;

public class BlockCommandManager implements CommandManager {

    private static BlockCommandManager instance;

    private List<BlockCommand> commands;

    public BlockCommandManager() {
        List<BlockCommand> references = new ArrayList<>();
        references.add(new ApplyBoneMeal());
        references.add(new SetBlockPos());
        references.add(new SetBlock());
        references.add(new SetExecutableBlock());
        references.add(new SendMessage());
        references.add(new Explode());
        references.add(new Break());
        references.add(new Launch());
        references.add(new DropItem());
        references.add(new DropExecutableItem());
        references.add(new MineInCube());
        references.add(new RemoveBlock());
        references.add(new Around());
        references.add(new MobAround());
        references.add(new VeinBreaker());
        references.add(new SilkSpawner());
        references.add(new DrainInCube());
        /* No BlockData in 1.12 and less */
        if (!SCore.is1v12Less()) {
            references.add(new FarmInCube());
            references.add(new FertilizeInCube());
        }
        if (!SCore.is1v11Less()) {
            references.add(new ParticleCommand());
        }
        this.commands = references;
    }

    public static BlockCommandManager getInstance() {
        if (instance == null) instance = new BlockCommandManager();
        return instance;
    }

    /*
     *  return "" if no error else return the error
     */
    public Optional<String> verifArgs(BlockCommand bC, List<String> args) {
        return bC.verify(args, false);
    }

    public boolean isValidBlockCommands(String entry) {
        for (BlockCommand blockCommands : commands) {
            for (String name : blockCommands.getNames()) {
                if (entry.toUpperCase().startsWith(name.toUpperCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getBlockCommands(SPlugin sPlugin, List<String> commands, List<String> errorList, String id) {

        List<String> result = new ArrayList<>();

        for (String s : commands) {

            String command = StringConverter.coloredString(s);

            /*
             * if (command.contains("\\{")) command= command.replaceAll("\\{", ""); if
             * (command.contains("\\}")) command= command.replaceAll("\\}", "");
             */

            if (this.isValidBlockCommands(s) && !s.contains("//") && !s.contains("+++")) {
                BlockCommand bc = (BlockCommand) this.getCommand(command);
                List<String> args = this.getArgs(command);

                Optional<String> error = this.verifArgs(bc, args);
                if (error.isPresent()) {
                    errorList.add(StringConverter.decoloredString(sPlugin.getNameDesign() + " " + error.get() + " for item: " + id));
                }
            }
            result.add(command);
        }
        return result;
    }

    public Optional<String> verifCommand(String command) {

        command = StringConverter.coloredString(command);

        /*
         * if (command.contains("\\{")) command= command.replaceAll("\\{", ""); if
         * (command.contains("\\}")) command= command.replaceAll("\\}", "");
         */

        if (this.isValidBlockCommands(command) && !command.contains("//") && !command.contains("+++")) {
            BlockCommand bc = (BlockCommand) this.getCommand(command);
            List<String> args = this.getArgs(command);

            Optional<String> error = this.verifArgs(bc, args);
            if (error.isPresent()) {
                return Optional.of("&4&lINVALID COMMAND &c" + " " + error.get());
            }
        }
        return Optional.empty();
    }

    public List<BlockCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<BlockCommand> commands) {
        this.commands = commands;
    }

    public Map<String, String> getCommandsDisplay() {
        Map<String, String> result = new HashMap<>();
        for (SCommand c : this.commands) {

            ChatColor extra = c.getExtraColor();
            if (extra == null) extra = ChatColor.DARK_PURPLE;

            ChatColor color = c.getColor();
            if (color == null) color = ChatColor.LIGHT_PURPLE;

            result.put(extra + "[" + color + "&l" + c.getNames().get(0) + extra + "]", c.getTemplate());
        }
        return result;
    }

    @Override
    public SCommand getCommand(String brutCommand) {
        for (BlockCommand blockCommands : commands) {
            for (String name : blockCommands.getNames()) {
                if (brutCommand.toUpperCase().startsWith(name.toUpperCase())) {
                    return blockCommands;
                }
            }
        }
        return null;
    }

    @Override
    public List<String> getArgs(String command) {
        List<String> args = new ArrayList<>();
        boolean first = true;
        for (String s : command.split(" ")) {
            if (first) {
                first = false;
                continue;
            }
            args.add(s);
        }
        return args;
    }
}
