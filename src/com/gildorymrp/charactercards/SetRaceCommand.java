package com.gildorymrp.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gildorymrp.gildorymclasses.GildorymClasses;

public class SetRaceCommand implements CommandExecutor {

	private GildorymCharacterCards plugin;

	public SetRaceCommand(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		GildorymClasses gildorymClasses = (GildorymClasses) Bukkit.getServer().getPluginManager().getPlugin("GildorymClasses");
		
		Player player = null;
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED
					+ "You need to specify a race!");
			return true;
		} else if (args.length == 1) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(ChatColor.RED
						+ "Only a player can perform this command!");
				return true;
			}
		} else {
			if (!sender.hasPermission("gildorym.setraceother")) {
				sender.sendMessage(ChatColor.RED
						+ "You do not have permission to change another player's race!");
			}
			player = sender.getServer().getPlayer(args[1]);
			if (player == null) {
				sender.sendMessage(ChatColor.RED
						+ "That player does not exist!");
			}
		}
		
		if (plugin.getCharacterCards().get(player.getName()) == null) {
			plugin.getCharacterCards().put(player.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, gildorymClasses.levels.get(sender.getName()), gildorymClasses.classes.get(sender.getName())));
		} else if (plugin.getCharacterCards().get(player.getName()).getRace() != Race.UNKNOWN) {
			if (!sender.hasPermission("gildorym.setraceother")) {
				sender.sendMessage(ChatColor.RED
						+ "You have already set your race!");
			}
		}
		
		try {
			plugin.getCharacterCards().get(player.getName()).setRace(Race.valueOf(args[0].toUpperCase()));
			sender.sendMessage(ChatColor.GREEN + "Set race to " + Race.valueOf(args[0].toUpperCase()).toString());
		} catch (IllegalArgumentException exception) {
			sender.sendMessage(ChatColor.RED + "That race does not exist!");
			sender.sendMessage(ChatColor.YELLOW + "If the race is a subrace, you may want to choose the main race.");
			sender.sendMessage(ChatColor.YELLOW + "If the race is a special case, such as an event, you may want to choose OTHER or UNKNOWN.");
		}
		return true;
	}

}
