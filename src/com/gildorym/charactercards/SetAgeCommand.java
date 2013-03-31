package com.gildorym.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gildorym.basicchar.BasicChar;

public class SetAgeCommand implements CommandExecutor {

	private CharacterCards plugin;
	
	public SetAgeCommand(CharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		BasicChar basicChar = (BasicChar) Bukkit.getServer().getPluginManager().getPlugin("BasicChar");
		if (plugin.getCharacterCards().get(sender.getName()) == null) {
			plugin.getCharacterCards().put(sender.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, basicChar.levels.get(sender.getName())));
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
