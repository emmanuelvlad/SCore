package com.ssomar.scoretestrecode.features.custom.conditions.entity.condition;

import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.entity.EntityCondition;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.scoretestrecode.features.types.ListUncoloredStringFeature;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IfHasTag extends EntityConditionFeature<ListUncoloredStringFeature, IfHasTag> {


    public IfHasTag(FeatureParentInterface parent) {
        super(parent, "ifHasTag", "If has tag", new String[]{}, Material.ANVIL, false);
    }

    @Override
    public boolean verifCondition(Entity entity, Optional<Player> playerOpt, SendMessage messageSender, Event event) {
        if (hasCondition()) {
            boolean notValid = false;
            for (String tag : getCondition().getValue()) {
                if (!entity.getScoreboardTags().contains(tag)) {
                    notValid = true;
                    break;
                }
            }
            if (notValid) {
                sendErrorMsg(playerOpt, messageSender);
                cancelEvent(event);
                return false;
            }
        }
        return true;
    }

    @Override
    public IfHasTag getValue() {
        return this;
    }

    @Override
    public void subReset() {
        setCondition(new ListUncoloredStringFeature(getParent(), "ifHasTag", new ArrayList<>(), "If has tag", new String[]{"&7&oThe whitelisted tags"}, Material.ANVIL, false, Optional.empty()));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().getValue().size() > 0;
    }

    @Override
    public IfHasTag getNewInstance() {
        return new IfHasTag(getParent());
    }
}
