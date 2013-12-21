package com.gildorymrp.charactercards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.gildorymrp.gildorymclasses.CharacterClass;
import com.gildorymrp.gildorymclasses.GildorymClasses;

public class PlayerInteractEntityListener implements Listener {
	
	private GildorymCharacterCards plugin;
	
	public PlayerInteractEntityListener(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getPlayer().isSneaking()) {
			if (event.getRightClicked() instanceof Player) {
				Player player = (Player) event.getRightClicked();
				GildorymClasses gildorymClasses = (GildorymClasses) plugin.getServer().getPluginManager().getPlugin("GildorymClasses");
				if (plugin.getCharacterCards().get(player.getName()) == null) {
					plugin.getCharacterCards().put(player.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, gildorymClasses.levels.get(player.getName()), gildorymClasses.classes.get(player.getName())));
				}
				event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.BLUE + ChatColor.BOLD + "'s character card");
				CharacterCard characterCard = plugin.getCharacterCards().get(player.getName());
				CharacterClass clazz = gildorymClasses.classes.get(player.getName());
				Integer level = gildorymClasses.levels.get(player.getName());
				Race race = characterCard.getRace();

				Integer maxHealth = CharacterCard.calculateHealth(clazz, race, level);
				
				event.getPlayer().sendMessage(ChatColor.GRAY + "Health: " + ChatColor.BLUE + characterCard.getHealth() + "/" + maxHealth);
				event.getPlayer().sendMessage(ChatColor.GRAY + "Age: " + ChatColor.WHITE + characterCard.getAge());
				event.getPlayer().sendMessage(ChatColor.GRAY + "Gender: " + ChatColor.WHITE + characterCard.getGender().toString());
				event.getPlayer().sendMessage(ChatColor.GRAY + "Race: " + ChatColor.WHITE + characterCard.getRace().toString());
				event.getPlayer().sendMessage(ChatColor.GRAY + "Description: " + ChatColor.WHITE + characterCard.getDescription());
			}
		}
	}

}
