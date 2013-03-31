package com.gildorym.charactercards;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
	
	private CharacterCards plugin;
	
	public PlayerDeathListener(CharacterCards plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		plugin.getCharacterCards().get(event.getEntity().getName()).setHealth(plugin.getCharacterCards().get(event.getEntity().getName()).getHealth() - 1);
	}

}
