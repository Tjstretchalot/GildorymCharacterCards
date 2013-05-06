package com.gildorymrp.charactercards;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TakeHitCommand implements CommandExecutor {
	
	private GildorymCharacterCards plugin;
	
	public TakeHitCommand(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		plugin.getCharacterCards().get(sender.getName()).setHealth(plugin.getCharacterCards().get(sender.getName()).getHealth() - 1);
		sender.sendMessage(ChatColor.RED + "Took a hit!");
		return true;
	}

}
