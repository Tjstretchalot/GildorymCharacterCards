package com.gildorym.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gildorym.basicchar.BasicChar;

public class CharCommand implements CommandExecutor {
	
	private CharacterCards plugin;

	public CharCommand(CharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		BasicChar basicChar = (BasicChar) Bukkit.getServer().getPluginManager().getPlugin("BasicChar");
		if (plugin.getCharacterCards().get(sender.getName()) == null) {
			plugin.getCharacterCards().put(sender.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, basicChar.levels.get(sender.getName())));
		}
		sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + ((Player) sender).getDisplayName() + ChatColor.BLUE + ChatColor.BOLD + "'s character card");
		CharacterCard characterCard = plugin.getCharacterCards().get(sender.getName());
		Integer maxHealth = (int) (5 + Math.floor(basicChar.levels.get(sender.getName()) / 5));
		if (characterCard.getRace() == Race.ELF) {
			maxHealth -= 1;
		}
		if (characterCard.getRace() == Race.DWARF) {
			maxHealth += 1;
		}
		if (characterCard.getRace() == Race.GNOME) {
			maxHealth += 1;
		}
		sender.sendMessage(ChatColor.GRAY + "Health: " + ChatColor.BLUE + characterCard.getHealth() + "/" + maxHealth);
		sender.sendMessage(ChatColor.GRAY + "Age: " + ChatColor.WHITE + characterCard.getAge());
		sender.sendMessage(ChatColor.GRAY + "Gender: " + ChatColor.WHITE + characterCard.getGender().toString());
		sender.sendMessage(ChatColor.GRAY + "Race: " + ChatColor.WHITE + characterCard.getRace().toString());
		sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.WHITE + characterCard.getDescription());
		return true;
	}

}
