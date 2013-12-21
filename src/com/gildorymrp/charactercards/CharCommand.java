package com.gildorymrp.charactercards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gildorymrp.gildorymclasses.CharacterClass;
import com.gildorymrp.gildorymclasses.GildorymClasses;

public class CharCommand implements CommandExecutor {

	private GildorymCharacterCards plugin;

	public CharCommand(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		GildorymClasses gildorymClasses = (GildorymClasses) Bukkit.getServer().getPluginManager().getPlugin("GildorymClasses");
		
		if (plugin.getCharacterCards().get(sender.getName()) == null) {
			plugin.getCharacterCards().put(sender.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, gildorymClasses.levels.get(sender.getName()), gildorymClasses.classes.get(sender.getName())));
		}
		
		sender.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + ((Player) sender).getDisplayName() + ChatColor.BLUE + ChatColor.BOLD + "'s character card");
		
		CharacterCard characterCard = plugin.getCharacterCards().get(sender.getName());
		Race race = characterCard.getRace();
		CharacterClass clazz;
		Integer level;
		
		try {
			clazz = gildorymClasses.classes.get(sender.getName());
			level = gildorymClasses.levels.get(sender.getName());
		} catch (Exception ex) {
			clazz = null;
			level = 0;
		}
		
		Integer maxHealth = CharacterCard.calculateHealth(clazz, race, level);
		sender.sendMessage(ChatColor.GRAY + "Health: " + ChatColor.BLUE + characterCard.getHealth() + "/" + maxHealth);
		sender.sendMessage(ChatColor.GRAY + "Age: " + ChatColor.WHITE + characterCard.getAge());
		sender.sendMessage(ChatColor.GRAY + "Gender: " + ChatColor.WHITE + characterCard.getGender().toString());
		sender.sendMessage(ChatColor.GRAY + "Race: " + ChatColor.WHITE + characterCard.getRace().toString());
		sender.sendMessage(ChatColor.GRAY + "Description: " + ChatColor.WHITE + characterCard.getDescription());
		return true;
	}

}
