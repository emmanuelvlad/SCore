package com.ssomar.score.commands.runnable.block;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.CommandsHandler;
import com.ssomar.score.commands.runnable.RunCommand;
import com.ssomar.score.commands.runnable.SCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class BlockRunCommand extends RunCommand {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UUID launcherUUID;

    private int blockX;
    private int blockY;
    private int blockZ;
    private UUID blockWorld;

    private Material oldBlockMaterial;

    private boolean silenceOutput;

    public BlockRunCommand(String brutCommand, int delay, ActionInfo aInfo) {
        super(brutCommand, delay, aInfo);
    }

    public BlockRunCommand(String brutCommand, long runTime, ActionInfo aInfo) {
        super(brutCommand, runTime, aInfo);
    }

    @Override
    public void pickupInfo() {
        ActionInfo aInfo = this.getaInfo();

        launcherUUID = aInfo.getLauncherUUID();

        blockX = aInfo.getBlockLocationX();
        blockY = aInfo.getBlockLocationY();
        blockZ = aInfo.getBlockLocationZ();
        blockWorld = aInfo.getBlockLocationWorld();

        oldBlockMaterial = Material.valueOf(aInfo.getOldBlockMaterialName());

        silenceOutput = aInfo.isSilenceOutput();
    }

    @Override
    public void runGetManager() {
        this.runCommand(BlockCommandManager.getInstance());
    }

    @Override
    public void runCommand(SCommand command, List<String> args) {
        BlockSCommand bCommand = (BlockSCommand) command;

        Player launcher = Bukkit.getPlayer(launcherUUID);
        World world = Bukkit.getServer().getWorld(blockWorld);
        Location loc = new Location(world, blockX, blockY, blockZ);
        Block block = loc.getBlock();

        bCommand.run(launcher, block, oldBlockMaterial, args, this.getaInfo());
    }


    @Override
    public void insideDelayedCommand() {
        runCommand(BlockCommandManager.getInstance());
        CommandsHandler.getInstance().removeDelayedCommand(getUuid(), null);
    }

    public UUID getLauncherUUID() {
        return launcherUUID;
    }

    public void setLauncherUUID(UUID launcherUUID) {
        this.launcherUUID = launcherUUID;
    }

    public int getBlockX() {
        return blockX;
    }

    public void setBlockX(int blockX) {
        this.blockX = blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public void setBlockY(int blockY) {
        this.blockY = blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public void setBlockZ(int blockZ) {
        this.blockZ = blockZ;
    }

    public UUID getBlockWorld() {
        return blockWorld;
    }

    public void setBlockWorld(UUID blockWorld) {
        this.blockWorld = blockWorld;
    }

    public Material getOldBlockMaterial() {
        return oldBlockMaterial;
    }

    public void setOldBlockMaterial(Material oldBlockMaterial) {
        this.oldBlockMaterial = oldBlockMaterial;
    }

    public boolean isSilenceOutput() {
        return silenceOutput;
    }

    public void setSilenceOutput(boolean silenceOutput) {
        this.silenceOutput = silenceOutput;
    }
}
