package com.gildorymrp.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gildorymrp.gildorymclasses.GildorymClasses;

public class SetRaceCommand implements CommandExecutor {

	private GildorymCharacterCards plugin;

	public SetRaceCommand(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		GildorymClasses gildorymClasses = (GildorymClasses) Bukkit.getServer().getPluginManager().getPlugin("GildorymClasses");
		if (plugin.getCharacterCards().get(sender.getName()) == null) {
			plugin.getCharacterCards().put(sender.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, gildorymClasses.levels.get(sender.getName())));
		}
		if (args.length >= 1) {
			try {
				plugin.getCharacterCards().get(sender.getName()).setRace(Race.valueOf(args[0].toUpperCase()));
				sender.sendMessage(ChatColor.GREEN + "Set race to " + Race.valueOf(args[0].toUpperCase()).toString());
			} catch (IllegalArgumentException exception) {
				sender.sendMessage(ChatColor.RED + "That race does not exist!");
				sender.sendMessage(ChatColor.YELLOW + "If the race is a subrace, you may want to choose the main race.");
				sender.sendMessage(ChatColor.YELLOW + "If the race is a special case, such as an event, you may want to choose OTHER or UNKNOWN.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You need to specify a race!");
		}
		return true;
	}

}
