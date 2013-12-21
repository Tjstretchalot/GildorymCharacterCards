package com.gildorymrp.charactercards;

import java.io.Serializable;
import com.gildorymrp.gildorymclasses.CharacterClass;

public class CharacterCard implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer age;
	private Gender gender;
	private String description;
	private Race race;
	private Integer health;

	public CharacterCard(Integer age, Gender gender, String description, Race race, Integer level, CharacterClass clazz) {
		this.age = age;
		this.gender = gender;
		this.description = description;
		this.race = race;
		this.health = calculateHealth(clazz, race, level);
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
	
	public static Integer calculateHealth(CharacterClass clazz, Race race, Integer level) {
		Integer health;
		
		if (level == null) {
			level = 0;
		}
		
		if (clazz == null) {
			health = 5;
		} else {		
			switch (clazz) 
			{
			case BARBARIAN:
				health = 5 + (level / 3);
				break;

			case FIGHTER:
			case PALADIN:
				health = 5 + (level / 4); 
				break;

			case MONK:
			case CLERIC:
			case DRUID:
			case RANGER:
				health = 5 + (level / 5);
				break;

			case ROGUE:
			case BARD:
				health = 5 + (level / 6);
				break;

			case WIZARD:
			case SORCERER:
				health = 5 + (level / 7);
				break;

			default:
				health = 5;
				break;
			}	
		}

		if (race == Race.ELF) {
			health -= 1;
		} else if (race == Race.DWARF) {
			health += 1;
		} else if (race == Race.GNOME) {
			health += 1;
		}
		
		return health;
	}

}
