package com.gildorymrp.charactercards;

import java.io.Serializable;

public class CharacterCard implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer age;
	private Gender gender;
	private String description;
	private Race race;
	private Integer health;
	
	public CharacterCard(Integer age, Gender gender, String description, Race race, Integer level) {
		this.age = age;
		this.gender = gender;
		this.description = description;
		this.race = race;
		this.health = 5 + (int) Math.floor(level / 5);
		if (race == Race.ELF) {
			this.health -= 1;
		}
		if (race == Race.DWARF) {
			this.health += 1;
		}
		if (race == Race.GNOME) {
			this.health += 1;
		}
	}
	
	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	
	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}
	
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the race
	 */
	public Race getRace() {
		return race;
	}
	
	/**
	 * @param race the race to set
	 */
	public void setRace(Race race) {
		this.race = race;
	}

	/**
	 * @return the health
	 */
	public Integer getHealth() {
		return health;
	}

	/**
	 * @param health the health to set
	 */
	public void setHealth(Integer health) {
		this.health = health;
	}

}
