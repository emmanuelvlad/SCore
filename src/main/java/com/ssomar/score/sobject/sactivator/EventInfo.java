package com.ssomar.score.sobject.sactivator;

import com.ssomar.score.utils.DetailedClick;
import com.ssomar.score.utils.DetailedInteraction;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Optional;

@Getter
@Setter
public class EventInfo {

    private Event eventSource;

    private Optional<Player> player;

    private Optional<Player> targetPlayer;

    private Optional<Entity> targetEntity;

    private Optional<Block> targetBlock;

    private Optional<Material> oldMaterialBlock;

    private Optional<String> oldStatesBlock;

    private Optional<String> blockface;

    private Optional<Projectile> projectile;

    private Optional<String> projectileProvenance;

    private Optional<DetailedClick> detailedClick;

    private Optional<DetailedInteraction> detailedInteraction;

    private Optional<EntityDamageEvent.DamageCause> damageCause;

    private Optional<Vector> velocity;

    private boolean isEventCallByMineinCube;

    private Optional<String> command;

    private boolean mainHand;

    private boolean forceMainHand;

    private boolean mustDoNothing;

    private Optional<Integer> slot;

    public EventInfo(Event eventSource) {
        super();
        this.eventSource = eventSource;
        this.player = Optional.empty();
        this.targetPlayer = Optional.empty();
        this.targetEntity = Optional.empty();
        this.targetBlock = Optional.empty();
        this.oldMaterialBlock = Optional.empty();
        this.oldStatesBlock = Optional.empty();
        this.blockface = Optional.empty();
        this.projectile = Optional.empty();
        this.projectileProvenance = Optional.empty();
        this.detailedClick = Optional.empty();
        this.detailedInteraction = Optional.empty();
        this.damageCause = Optional.empty();
        this.velocity = Optional.empty();
        this.command = Optional.empty();
        this.slot = Optional.empty();
    }

    public EventInfo clone() {
        EventInfo eInfo = new EventInfo(eventSource);
        eInfo.setPlayer(player);
        eInfo.setTargetPlayer(targetPlayer);
        eInfo.setTargetEntity(targetEntity);
        eInfo.setTargetBlock(targetBlock);
        eInfo.setOldMaterialBlock(oldMaterialBlock);
        eInfo.setOldStatesBlock(oldStatesBlock);
        eInfo.setBlockface(blockface);
        eInfo.setProjectile(projectile);
        eInfo.setProjectileProvenance(projectileProvenance);
        eInfo.setDetailedClick(detailedClick);
        eInfo.setDetailedInteraction(detailedInteraction);
        eInfo.setDamageCause(damageCause);
        eInfo.setVelocity(velocity);
        eInfo.setEventCallByMineinCube(isEventCallByMineinCube);
        eInfo.setCommand(command);
        eInfo.setMainHand(mainHand);
        eInfo.setForceMainHand(forceMainHand);
        eInfo.setMustDoNothing(mustDoNothing);
        eInfo.setSlot(slot);

        return eInfo;
    }
}
