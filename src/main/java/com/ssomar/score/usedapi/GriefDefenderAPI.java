package com.ssomar.score.usedapi;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.ClaimManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GriefDefenderAPI {

    public static boolean playerIsInHisClaim(@NotNull Player p, Location location) {

        ClaimManager cM = GriefDefender.getCore().getClaimManager(location.getWorld().getUID());

        Claim claim = cM.getClaimAt((int) location.getX(), (int) location.getY(), (int) location.getZ());

        if (claim.isWilderness() || claim.getOwnerUniqueId() == null) return false;

        return claim.getOwnerUniqueId().equals(p.getUniqueId()) || claim.getUserTrusts().contains(p.getUniqueId());
    }

}
