package com.gildorym.charactercards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.gildorym.basicchar.BasicChar;

public class PlayerInteractEntityListener implements Listener {
	
	private CharacterCards plugin;
	
	public PlayerInteractEntityListener(CharacterCards plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if (event.getPlayer().isSneaking()) {
			if (event.getRightClicked() instanceof Player) {
				Player player = (Player) event.getRightClicked();
				BasicChar basicChar = (BasicChar) plugin.getServer().getPluginManager().getPlugin("BasicChar");
				if (plugin.getCharacterCards().get(player.getName()) == null) {
					plugin.getCharacterCards().put(player.getName(), new CharacterCard(0, Gender.UNKNOWN, "", Race.UNKNOWN, basicChar.levels.get(player.getName())));
				}
				event.getPlayer().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + player.getDisplayName() + ChatColor.BLUE + ChatColor.BOLD + "'s character card");
				CharacterCard characterCard = plugin.getCharacterCards().get(player.getName());
				Integer maxHealth = (int) (5 + Math.floor(basicChar.levels.get(player.getName()) / 5));
				if (characterCard.getRace() == Race.ELF) {
					maxHealth -= 1;
				}
				if (characterCard.getRace() == Race.DWARF) {
					maxHealth += 1;
				}
				if (characterCard.getRace() == Race.GNOME) {
					maxHealth += 1;
				}
				event.getPlayer().sendMessage(ChatColor.GRAY + "Health: " + ChatColor.BLUE + characterCard.getHealth() + "/" + maxHealth);
				event.getPlayer().sendMessage(ChatColor.GRAY + "Age: " + ChatColor.WHITE + characterCard.getAge());
				event.getPlayer().sendMessage(ChatColor.GRAY + "Gender: " + ChatColor.WHITE + characterCard.getGender().toString());
				event.getPlayer().sendMessage(ChatColor.GRAY + "Race: " + ChatColor.WHITE + characterCard.getRace().toString());
				event.getPlayer().sendMessage(ChatColor.GRAY + "Description: " + ChatColor.WHITE + characterCard.getDescription());
			}
		}
	}

}
