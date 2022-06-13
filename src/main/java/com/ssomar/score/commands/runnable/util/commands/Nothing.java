package com.ssomar.score.commands.runnable.util.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.ssomar.score.commands.runnable.SCommand;

public class Nothing extends SCommand{

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("NOTHING");
		return names;
	}

	@Override
	public String getTemplate() {
		return "NOTHING*{number}";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.YELLOW;
	}

	@Override
	public ChatColor getExtraColor() {
		return ChatColor.GOLD;
	}
}
