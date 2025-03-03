package com.ssomar.score.commands.runnable.player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.CommandsHandler;
import com.ssomar.score.commands.runnable.RunCommand;
import com.ssomar.score.commands.runnable.SCommand;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class PlayerRunCommand extends RunCommand {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UUID launcherUUID;

    private UUID receiverUUID;

    private boolean silenceOutput;

    @Getter
    private boolean runOffline;

    public PlayerRunCommand(String brutCommand, int delay, ActionInfo aInfo) {
        super(brutCommand, delay, aInfo);
        this.initRunOffline(brutCommand);
    }

    public PlayerRunCommand(String brutCommand, long runTime, ActionInfo aInfo) {
        super(brutCommand, runTime, aInfo);
        this.initRunOffline(brutCommand);
    }

    public void initRunOffline(String brutCommand) {
        if (brutCommand.contains("[<OFFLINE>]")) {
            runOffline = true;
            this.setBrutCommand(brutCommand.replaceAll("\\[<OFFLINE>\\]", ""));
        } else runOffline = false;
    }

    @Override
    public void pickupInfo() {
        ActionInfo aInfo = this.getaInfo();

        launcherUUID = aInfo.getLauncherUUID();

        receiverUUID = aInfo.getReceiverUUID();

        silenceOutput = aInfo.isSilenceOutput();
    }

    @Override
    public void runGetManager() {
        this.runCommand(PlayerCommandManager.getInstance());
    }

    @Override
    public void runCommand(SCommand command, List<String> args) {
        PlayerSCommand pCommand = (PlayerSCommand) command;

        Player launcher = Bukkit.getPlayer(launcherUUID);
        Player receiver = Bukkit.getPlayer(receiverUUID);

        pCommand.run(launcher, receiver, args, this.getaInfo());
    }


    @Override
    public void insideDelayedCommand() {
        Player receiver = Bukkit.getPlayer(receiverUUID);

        if ((receiver != null && receiver.isOnline()) || runOffline) {
            runCommand(PlayerCommandManager.getInstance());
        }
        //else {
        //ADD THE COMMAND IN THE DB
        /* No need >> onPlayerQuitEvent its auto delete and save in the DB */
        //}
        CommandsHandler.getInstance().removeDelayedCommand(getUuid(), receiverUUID);
    }

    public UUID getLauncherUUID() {
        return launcherUUID;
    }

    public void setLauncherUUID(UUID launcherUUID) {
        this.launcherUUID = launcherUUID;
    }

    public UUID getReceiverUUID() {
        return receiverUUID;
    }

    public void setReceiverUUID(UUID receiverUUID) {
        this.receiverUUID = receiverUUID;
    }

    public boolean isSilenceOutput() {
        return silenceOutput;
    }

    public void setSilenceOutput(boolean silenceOutput) {
        this.silenceOutput = silenceOutput;
    }
}
