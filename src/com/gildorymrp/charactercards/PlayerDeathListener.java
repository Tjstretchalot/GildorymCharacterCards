package com.gildorymrp.charactercards;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
	
	private GildorymCharacterCards plugin;
	
	public PlayerDeathListener(GildorymCharacterCards plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		plugin.getCharacterCards().get(event.getEntity().getName()).setHealth(plugin.getCharacterCards().get(event.getEntity().getName()).getHealth() - 1);
	}

}
