package com.gildorymrp.charactercards;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.gildorymclasses.CharacterClass;
import com.gildorymrp.gildorymclasses.GildorymClasses;

public class GildorymCharacterCards extends JavaPlugin {

	protected static final int level = 0;
	private Map<String, CharacterCard> characterCards = new HashMap<String, CharacterCard>();

	/**
	 * @return the characterCards
	 */
	public Map<String, CharacterCard> getCharacterCards() {
		return characterCards;
	}

	public void onEnable() {
		SaveDataManager.loadData(this);
		this.registerListeners(new Listener[] {
				new PlayerInteractEntityListener(this),
				new PlayerDeathListener(this),
				new EntityRegainHealthListener(),
		});
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					GildorymClasses gildorymClasses = (GildorymClasses) Bukkit.getServer().getPluginManager().getPlugin("GildorymClasses");

					if (gildorymClasses.levels.get(player.getName()) != null || gildorymClasses.classes.get(player.getName()) != null) {
						CharacterClass clazz = gildorymClasses.classes.get(player.getName());
						Integer level = gildorymClasses.levels.get(player.getName());

						if (GildorymCharacterCards.this.getCharacterCards().get(player.getName()) != null) {
							CharacterCard characterCard = getCharacterCards().get(player.getName());
							Race race = characterCard.getRace();
							Integer maxHealth = CharacterCard.calculateHealth(clazz, race, level);
							if (GildorymCharacterCards.this.getCharacterCards().get(player.getName()).getHealth() < maxHealth) {
								GildorymCharacterCards.this.getCharacterCards().get(player.getName()).setHealth(GildorymCharacterCards.this.getCharacterCards().get(player.getName()).getHealth() + 1);
							} else if (GildorymCharacterCards.this.getCharacterCards().get(player.getName()).getHealth() > maxHealth) {
								GildorymCharacterCards.this.getCharacterCards().get(player.getName()).setHealth(maxHealth);
							}
						}
					}
				}
			}

		}, 345600L, 345600L);
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					if (player.getHealth() < player.getMaxHealth() && player.isSleeping()) {
						player.setHealth(player.getHealth() + Math.min(Math.round((float) player.getMaxHealth() / 20F), player.getMaxHealth() - player.getHealth()));
						player.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "You feel a little more refreshed from sleep. +" + Math.min(Math.round((float) player.getMaxHealth() / 20F), player.getMaxHealth() - player.getHealth()) + "HP");
					}
				}
			}

		}, 2400L, 2400L);
		this.getCommand("setage").setExecutor(new SetAgeCommand(this));
		this.getCommand("setgender").setExecutor(new SetGenderCommand(this));
		this.getCommand("setrace").setExecutor(new SetRaceCommand(this));
		this.getCommand("setinfo").setExecutor(new SetInfoCommand(this));
		this.getCommand("addinfo").setExecutor(new AddInfoCommand(this));
		this.getCommand("char").setExecutor(new CharCommand(this));
		this.getCommand("takehit").setExecutor(new TakeHitCommand(this));
		this.getCommand("dealhit").setExecutor(new DealHitCommand(this));
		this.getCommand("healhit").setExecutor(new HealHitCommand(this));
		this.getCommand("viewhealth").setExecutor(new ViewHealthCommand(this));
	}

	public void onDisable() {
		SaveDataManager.saveData(this);
	}

	private void registerListeners(Listener... listeners) {
		for (Listener listener : listeners) {
			this.getServer().getPluginManager().registerEvents(listener, this);
		}
	}

}
