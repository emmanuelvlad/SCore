package com.ssomar.score.commands.runnable.util.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;

import com.ssomar.score.commands.runnable.Command;

public class DelayTick implements Command{

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("DELAYTICK");
		return names;
	}

	@Override
	public String getTemplate() {
		// TODO Auto-generated method stub
		return "DELAYTICK {number}";
	}

	@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getExtraColor() {
		// TODO Auto-generated method stub
		return null;
	}
}
