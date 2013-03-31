package com.gildorym.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gildorym.basicchar.BasicChar;

public class SetGenderCommand implements CommandExecutor {

	private CharacterCards plugin;

	public SetGenderCommand(CharacterCards plugin) {
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
				plugin.getCharacterCards().get(sender.getName()).setGender(Gender.valueOf(args[0].toUpperCase()));
				sender.sendMessage(ChatColor.GREEN + "Set gender to " + Gender.valueOf(args[0].toUpperCase()).toString());
			} catch (IllegalArgumentException exception) {
				sender.sendMessage(ChatColor.RED + "That gender does not exist!");
				sender.sendMessage(ChatColor.YELLOW + "If the gender is not standard, i.e. MALE or FEMALE, you may want to choose OTHER or UNKNOWN.");
			}
		} else {
			sender.sendMessage(ChatColor.RED + "You need to specify a gender!");
		}
		return true;
	}

}
