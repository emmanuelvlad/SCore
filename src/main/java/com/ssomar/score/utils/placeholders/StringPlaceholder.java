package com.ssomar.score.utils.placeholders;

import com.ssomar.executableitems.features.variables.VariableReal;
import com.ssomar.score.SCore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;

@Getter
@Setter
public class StringPlaceholder extends PlaceholdersInterface implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /* placeholders of the player */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final PlayerPlaceholders playerPlch = new PlayerPlaceholders();

    /* placeholders of the target player */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final TargetPlaceholders targetPlch = new TargetPlaceholders();

    /* placeholders of the owner */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final OwnerPlaceholders ownerPlch = new OwnerPlaceholders();

    /* placeholders of the owner */
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final ProjectilePlaceholders projectilePlch = new ProjectilePlaceholders();

    /* placeholders of the time */
    private final TimePlaceholders timePlch = new TimePlaceholders();
    /* placeholders of the target entity */
    private final EntityPlaceholders entityPlch = new EntityPlaceholders();
    /* placeholders of the block */
    private final BlockPlaceholders blockPlch = new BlockPlaceholders();
    /* placeholders of the target block */
    private final TargetBlockPlaceholders targetBlockPlch = new TargetBlockPlaceholders();
    /* placeholders of the around target player */
    AroundPlayerTargetPlaceholders aroundPlayerTargetPlch = new AroundPlayerTargetPlaceholders();
    /* placeholders of the around target entity */
    AroundEntityTargetPlaceholders aroundEntityTargetPlch = new AroundEntityTargetPlaceholders();
    /* placeholders of the item */
    private String activator = "";
    private String item = "";
    private String quantity = "";
    private String usage = "";
    private String usageLimit = "";
    private String maxUsePerDayItem = "";
    private String maxUsePerDayActivator = "";
    /* placeholders tools */
    private String launcher = "";
    private String blockface = "";
    /* placeholders of the cooldown */
    private String cooldown = "";
    private List<VariableReal> variables = new ArrayList<>();
    private Map<String, String> extraPlaceholders = new HashMap<>();

    public static String replaceRandomPlaceholders(String s) {
        String result = s;
        if (result.contains("%rand:")) {
            int part1;
            int part2;
            String[] decompRand = result.split("\\%rand:");
            boolean cont = true;
            for (String strRand : decompRand) {
                if (cont) {
                    cont = false;
                    continue;
                }

                if (strRand.contains("%")) {
                    String[] decomp = strRand.split("\\%");
                    if ((decomp.length >= 2 || (strRand.endsWith("%") && decomp.length == 1)) && decomp[0].contains("|")) {
                        decomp = decomp[0].split("\\|");

                        try {
                            part1 = Integer.parseInt(decomp[0]);
                            part2 = Integer.parseInt(decomp[1]);
                        } catch (Exception e) {
                            continue;
                        }

                        if (part1 < part2) {
                            int random = part1 + (int) (Math.random() * ((part2 - part1) + 1));
                            result = result.replace("%rand:" + part1 + "|" + part2 + "%", random + "");
                        }
                    }
                }
            }
        }

        return result;
    }

    public void setPlayerPlcHldr(UUID uuid) {
        playerPlch.setPlayerPlcHldr(uuid);
    }

    public void setPlayerPlcHldr(UUID uuid, int fixSlot) {
        playerPlch.setPlayerPlcHldr(uuid, fixSlot);
    }

    public void setTargetPlcHldr(UUID uuid) {
        targetPlch.setPlayerPlcHldr(uuid);
    }

    public void setOwnerPlcHldr(UUID uuid) {
        ownerPlch.setPlayerPlcHldr(uuid);
    }

    public void setProjectilePlcHldr(Projectile proj, String blockFace) {
        projectilePlch.setProjectilePlcHldr(proj, blockFace);
    }

    public void setEntityPlcHldr(UUID uuid) {
        entityPlch.setEntityPlcHldr(uuid);
    }

    public void setBlockPlcHldr(Block block) {
        blockPlch.setBlockPlcHldr(block);
    }

    public void setBlockPlcHldr(Block block, Material fixType) {
        blockPlch.setBlockPlcHldr(block, fixType);
    }

    public void setTargetBlockPlcHldr(Block block) {
        targetBlockPlch.setTargetBlockPlcHldr(block);
    }

    public void setTargetBlockPlcHldr(Block block, Material fixType) {
        targetBlockPlch.setTargetBlockPlcHldr(block, fixType);
    }

    public void setAroundTargetPlayerPlcHldr(UUID uuid) {
        aroundPlayerTargetPlch.setPlayerPlcHldr(uuid);
    }

    public void setAroundTargetEntityPlcHldr(UUID uuid) {
        aroundEntityTargetPlch.setEntityPlcHldr(uuid);
    }

    public void reloadAllPlaceholders() {
        playerPlch.reloadPlayerPlcHldr();
        targetPlch.reloadPlayerPlcHldr();
        ownerPlch.reloadPlayerPlcHldr();
        entityPlch.reloadEntityPlcHldr();
        blockPlch.reloadBlockPlcHldr();
        targetBlockPlch.reloadTargetBlockPlcHldr();
        aroundPlayerTargetPlch.reloadPlayerPlcHldr();
        aroundEntityTargetPlch.reloadEntityPlcHldr();
        /* delayed command with old version has this to null */
        if (projectilePlch != null) projectilePlch.reloadProjectilePlcHldr();
    }

    public String replacePlaceholder(String str) {
        return replacePlaceholder(str, true);
    }

    public String replacePlaceholder(String str, boolean withPAPI) {
        this.reloadAllPlaceholders();
        String s = str;

        if (str.trim().length() == 0) return "";

        s = replaceRandomPlaceholders(s);

        if (this.hasActivator()) {
            s = s.replaceAll("%activator%", this.getActivator());
        }
        if (this.hasItem()) {
            s = s.replaceAll("%item%", Matcher.quoteReplacement(this.getItem()));
        }
        if (this.hasQuantity()) {
            s = replaceCalculPlaceholder(s, "%quantity%", quantity, true);
            s = replaceCalculPlaceholder(s, "%amount%", quantity, true);
        }
        if (this.hasCoolodwn()) {
            s = s.replaceAll("%cooldown%", this.getCooldown());
        }
        if (this.hasBlockFace()) {
            s = s.replaceAll("%blockface%", this.getBlockface());
        }
        if (this.hasUsage()) {
            s = replaceCalculPlaceholder(s, "%usage%", usage, true);
        }
        if (this.hasMaxUsePerDayActivator()) {
            s = s.replaceAll("%max_use_per_day_activator%", this.getMaxUsePerDayActivator());
        }
        if (this.hasMaxUsePerDayItem()) {
            s = s.replaceAll("%max_use_per_day_item%", this.getMaxUsePerDayItem());
        }

        if (variables != null) {
            for (VariableReal var : variables) {
                s = var.replaceVariablePlaceholder(s);
            }
        }

        s = playerPlch.replacePlaceholder(s);

        s = targetPlch.replacePlaceholder(s);

        s = ownerPlch.replacePlaceholder(s);

        s = entityPlch.replacePlaceholder(s);

        s = blockPlch.replacePlaceholder(s);

        s = targetBlockPlch.replacePlaceholder(s);

        s = aroundPlayerTargetPlch.replacePlaceholder(s);

        s = aroundEntityTargetPlch.replacePlaceholder(s);

        s = timePlch.replacePlaceholder(s);

        if (projectilePlch != null) s = projectilePlch.replacePlaceholder(s);

        if (extraPlaceholders != null && !extraPlaceholders.isEmpty()) {
            for (String key : extraPlaceholders.keySet()) {
                s = s.replaceAll(key, extraPlaceholders.get(key));
            }
        }

        if (withPAPI) return replacePlaceholderOfPAPI(s);
        else return s;
    }

    public String replacePlaceholderOfPAPI(String s) {
        String replace = s;
        UUID uuid;
        if ((uuid = playerPlch.getPlayerUUID()) != null) {
            Player p;
            //SsomarDev.testMsg("REPLACE PLACE 2 : "+((p = Bukkit.getPlayer(UUID.fromString(playerUUID)))!=null)+ " &&&&&&& "+ExecutableItems.hasPlaceholderAPI());
            if ((p = Bukkit.getPlayer(uuid)) != null && SCore.hasPlaceholderAPI)
                replace = PlaceholderAPI.setPlaceholders(p, replace);
        }
        return replace;
    }

    //	public static void main(String[] args) {
    //		 StringPlaceholder sp = new StringPlaceholder();
    //		 sp.blockXInt = "10";
    //		 sp.blockYInt = "11";
    //		 sp.blockZInt = "12";
    //
    //		 String base = "SENDMESSAGE oops %block_x_int%,%block_y_int%+1,%block_z_int% youhouu";
    //
    //		 base = sp.replacePlaceholder(base);
    //
    //		 System.out.println(base);
    //	}

    public boolean hasActivator() {
        return activator.length() != 0;
    }

    public boolean hasItem() {
        return item.length() != 0;
    }

    public boolean hasQuantity() {
        return quantity.length() != 0;
    }

    public boolean hasCoolodwn() {
        return cooldown.length() != 0;
    }

    public boolean hasUsageLimit() {
        return usageLimit.length() != 0;
    }

    public boolean hasBlockFace() {
        return this.blockface.length() != 0;
    }

    public boolean hasUsage() {
        return this.usage.length() != 0;
    }

    public boolean hasMaxUsePerDayItem() {
        return maxUsePerDayItem.length() != 0;
    }

    public boolean hasMaxUsePerDayActivator() {
        return maxUsePerDayActivator.length() != 0;
    }
}
