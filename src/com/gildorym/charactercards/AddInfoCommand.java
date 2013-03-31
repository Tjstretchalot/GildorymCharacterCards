package com.gildorym.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gildorym.basicchar.BasicChar;

public class AddInfoCommand implements CommandExecutor {

	private CharacterCards plugin;

	public AddInfoCommand(CharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		BasicChar basicChar = (BasicChar) Bukkit.getServer().getPluginManager().getPlugin("BasicChar");
		if (plugin.getCharacterCards().get(sender.getName()) == null) {
			plugin.getCharacterCards().put(sender.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, basicChar.levels.get(sender.getName())));
		}
		if (args.length >= 1) {
			String info = "";
			for (String arg : args) {
				info += arg + " ";
			}
			plugin.getCharacterCards().get(sender.getName()).setDescription(plugin.getCharacterCards().get(sender.getName()).getDescription() + info);
			sender.sendMessage(ChatColor.GREEN + "Added to description.");
		} else {
			sender.sendMessage(ChatColor.RED + "You need to specify some information!");
		}
		return true;
	}

}
