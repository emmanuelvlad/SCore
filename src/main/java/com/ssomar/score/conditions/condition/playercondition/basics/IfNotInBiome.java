package com.ssomar.score.conditions.condition.playercondition.basics;

import com.ssomar.score.conditions.condition.ConditionType;
import com.ssomar.score.conditions.condition.playercondition.PlayerCondition;
import com.ssomar.score.utils.SendMessage;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IfNotInBiome extends PlayerCondition<List<Biome>> {


    public IfNotInBiome() {
        super(ConditionType.LIST_BIOME, "ifNotInBiome", "If not in biome", new String[]{}, new ArrayList<>(), " &cYou aren't in the good biome to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(Player player, Optional<Player> playerOpt, SendMessage messageSender) {
        if (getCondition() != null && !getCondition().isEmpty()) {
            boolean notValid = false;
            for (Biome b : getCondition()) {
                if (player.getLocation().getBlock().getBiome().equals(b)) {
                    notValid = true;
                    break;
                }
            }
            if (notValid) {
                sendErrorMsg(playerOpt, messageSender);
                return false;
            }
        }
        return true;
    }
}
