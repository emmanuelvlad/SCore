package com.ssomar.score.projectiles.features;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.ssomar.executableitems.ExecutableItems;
import com.ssomar.score.SCore;
import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class InvisibleFeature extends DecorateurCustomProjectiles {

    boolean isInvisible;

    public InvisibleFeature(CustomProjectile cProj){
        super.cProj = cProj;
        isInvisible = false;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        isInvisible = projConfig.getBoolean("invisilbe", false);
        return  cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if(isInvisible && SCore.hasProtocolLib){
            PacketContainer entityPacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            entityPacketContainer.getIntLists().write(0, Arrays.asList(e.getEntityId()));
            Bukkit.getOnlinePlayers().forEach(p -> {
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(p, entityPacketContainer);
                } catch (InvocationTargetException err) {
                    err.printStackTrace();
                }
            });
        }
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        return gui;
    }
}
