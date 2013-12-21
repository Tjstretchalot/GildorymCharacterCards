package com.gildorymrp.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gildorymrp.gildorymclasses.GildorymClasses;

public class SetAgeCommand implements CommandExecutor {

	private GildorymCharacterCards plugin;
	
	public SetAgeCommand(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		GildorymClasses gildorymClasses = (GildorymClasses) Bukkit.getServer().getPluginManager().getPlugin("GildorymClasses");
		if (plugin.getCharacterCards().get(sender.getName()) == null) {
			plugin.getCharacterCards().put(sender.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, gildorymClasses.levels.get(sender.getName()), gildorymClasses.classes.get(sender.getName())));
		}
		if (args.length >= 1) {
			try {
				plugin.getCharacterCards().get(sender.getName()).setAge(Integer.parseInt(args[0]));
				sender.sendMessage(ChatColor.GREEN + "Set age to " + args[0]);
			} catch (NumberFormatException exception) {
				sender.sendMessage(ChatColor.RED + "You need to specify a number!");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You need to specify an age!");
		}
		return true;
	}

}
