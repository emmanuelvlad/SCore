package com.ssomar.score.commands.runnable.util.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.ssomar.score.commands.runnable.Command;

public class LoopStart implements Command{

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("LOOP START");
		return names;
	}

	@Override
	public String getTemplate() {
		// TODO Auto-generated method stub
		return "LOOP START: {number}";
	}

	@Override
	public ChatColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChatColor getExtraColor() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
