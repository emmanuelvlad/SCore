package com.ssomar.score.sobject.sactivator.conditions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.ssomar.score.utils.messages.MessageDesign;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.google.common.base.Charsets;
import com.ssomar.score.sobject.SObject;
import com.ssomar.score.sobject.sactivator.SActivator;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringCalculation;

@Getter @Setter
public class WorldConditions extends Conditions{
	
	//STORM, RAIN, CLEAR
	private List<String> ifWeather;
	private static final String IF_WEATHER_MSG = " &cThe weather must change to active the activator: &6%activator% &cof this item!";
	private String ifWeatherMsg;
	
	private String ifWorldTime;
	private static final String IF_WORLD_TIME_MSG = " &cThe world time is not valid to active the activator: &6%activator% &cof this item!";
	private String ifWorldTimeMsg;
	
	@Override
	public void init() {
		this.ifWeather = new ArrayList<>();
		this.ifWeatherMsg = IF_WEATHER_MSG;
		
		this.ifWorldTime = "";
		this.ifWorldTimeMsg = IF_WORLD_TIME_MSG;
	}
	
	public boolean verifConditions(World world, @Nullable Player p) {
		
		if(this.hasIfWorldTime()) {
			if(!StringCalculation.calculation(this.ifWorldTime, world.getTime())) {
				if(p != null) this.getSm().sendMessage(p, this.getIfWorldTimeMsg());
				return false;
			}
		}
		if(this.hasIfWeather()) {
			String currentW = "";
			if(world.isThundering()) currentW = "STORM";

			else if(world.hasStorm()) currentW = "RAIN";
			else currentW = "CLEAR";
			
			if(!this.ifWeather.contains(currentW)) {
				if(p != null) this.getSm().sendMessage(p, this.getIfWeatherMsg());
				return false;
			}
		}
		return true;
	}
	
	public static WorldConditions getWorldConditions(ConfigurationSection worldCdtSection, List<String> errorList, String pluginName) {

		WorldConditions wCdt = new WorldConditions();

		wCdt.setIfWeather(worldCdtSection.getStringList("ifWeather"));
		wCdt.setIfWeatherMsg(worldCdtSection.getString("ifWeatherMsg", MessageDesign.ERROR_CODE_FIRST+pluginName+IF_WEATHER_MSG));

		wCdt.setIfWorldTime(worldCdtSection.getString("ifWorldTime", ""));
		wCdt.setIfWorldTimeMsg(worldCdtSection.getString("ifWorldTimeMsg", MessageDesign.ERROR_CODE_FIRST+pluginName+IF_WORLD_TIME_MSG));

		return wCdt;

	}
	
	/*
	 *  @param sPlugin The plugin of the conditions
	 *  @param sObject The object
	 *  @param sActivator The activator that contains the conditions
	 *  @param pC the player conditions object
	 */
	public static void saveWorldConditions(SPlugin sPlugin, SObject sObject, SActivator sActivator, WorldConditions wC, String detail) {

		if(!new File(sObject.getPath()).exists()) {
			sPlugin.getPlugin().getLogger().severe("[ExecutableItems] Error can't find the file in the folder ! ("+sObject.getId()+".yml)");
			return;
		}
		File file = new File(sObject.getPath());
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		ConfigurationSection activatorConfig = config.getConfigurationSection("activators."+sActivator.getID());
		activatorConfig.set("conditions."+detail+".ifWorldTime", "<=0");

		String pluginName = sPlugin.getNameDesign();

		ConfigurationSection wCConfig = config.getConfigurationSection("activators."+sActivator.getID()+".conditions."+detail);

		if(wC.hasIfWeather()) wCConfig.set("ifWeather", wC.getIfWeather()); 
		else wCConfig.set("ifWeather", null);
		if(wC.getIfWeatherMsg().contains(wC.IF_WEATHER_MSG)) wCConfig.set("ifWeatherMsg", null);
		else wCConfig.set("ifWeatherMsg", wC.getIfWeatherMsg());

		if(wC.hasIfWorldTime()) wCConfig.set("ifWorldTime", wC.getIfWorldTime()); 
		else wCConfig.set("ifWorldTime", null);
		if(wC.getIfWorldTimeMsg().contains(wC.IF_WORLD_TIME_MSG)) wCConfig.set("ifWorldTimeMsg", null);
		else wCConfig.set("ifWorldTimeMsg", wC.getIfWorldTimeMsg());

		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);

			try {
				writer.write(config.saveToString());
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean hasIfWeather() {
		return ifWeather != null && ifWeather.size() != 0;
	}

	public boolean hasIfWorldTime() {
		return ifWorldTime != null && ifWorldTime.length() != 0;
	}
}
