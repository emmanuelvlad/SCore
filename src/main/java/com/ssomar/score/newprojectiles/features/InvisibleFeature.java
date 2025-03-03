package com.ssomar.score.newprojectiles.features;

/* public class InvisibleFeature extends DecorateurCustomProjectiles {

    boolean isInvisible;

    public InvisibleFeature(CustomProjectile cProj) {
        super.cProj = cProj;
        isInvisible = false;
    }

    @Override
    public boolean loadConfiguration(String filePath, FileConfiguration projConfig, boolean showError) {
        isInvisible = projConfig.getBoolean("invisible", false);
        return cProj.loadConfiguration(filePath, projConfig, showError) && true;
    }

    @Override
    public void saveConfiguration(FileConfiguration config) {
        config.set("invisible", isInvisible);
        cProj.saveConfiguration(config);
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if (isInvisible && SCore.hasProtocolLib) {
            PacketContainer entityPacketContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            if (!SCore.is1v17Plus()) entityPacketContainer.getIntegerArrays().write(0, new int[]{e.getEntityId()});
            else entityPacketContainer.getIntLists().write(0, Arrays.asList(e.getEntityId()));
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
    public SimpleGUI loadConfigGUI(SProjectiles sProj) {
        SimpleGUI gui = cProj.loadConfigGUI(sProj);
        Material glass;
        if (SCore.is1v12Less()) glass = Material.valueOf("GLASS");
        else glass = Material.GLASS_PANE;
        if (SCore.hasProtocolLib) {
            gui.addItem(glass, 1, GUI.TITLE_COLOR + "Invisible", false, false, GUI.CLICK_HERE_TO_CHANGE, "&7actually: ");
            gui.updateBoolean(GUI.TITLE_COLOR + "Invisible", isInvisible);
        } else gui.addItem(glass, 1, GUI.TITLE_COLOR + "Invisible", false, false, "&c&oREQUIRE PROTOCOLIB PLUGIN");

        return gui;
    }

    @Override
    public boolean interactionConfigGUI(GUI gui, Player player, ItemStack itemS, String title) {
        if (cProj.interactionConfigGUI(gui, player, itemS, title)) return true;
        String itemName = StringConverter.decoloredString(itemS.getItemMeta().getDisplayName());
        String changeBounce = StringConverter.decoloredString(GUI.TITLE_COLOR + "Invisible");

        if (itemName.equals(changeBounce) && SCore.hasProtocolLib) {
            boolean bool = gui.getBoolean(GUI.TITLE_COLOR + "Invisible");
            gui.updateBoolean(GUI.TITLE_COLOR + "Invisible", !bool);
        } else return false;
        return true;
    }

    @Override
    public void extractInfosGUI(GUI gui) {
        cProj.extractInfosGUI(gui);
        if (SCore.hasProtocolLib) isInvisible = gui.getBoolean(GUI.TITLE_COLOR + "Invisible");
    }
}*/
