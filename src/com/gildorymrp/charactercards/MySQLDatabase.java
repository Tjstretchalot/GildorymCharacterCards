package com.gildorymrp.charactercards;

import java.sql.*;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.gildorym.GildorymCharacter;
import com.gildorymrp.gildorymclasses.CharacterProfession;

public class MySQLDatabase {

	private static final String REPLACE_STATEMENT =
			"REPLACE INTO characters (" +
			"uid, " +
			"char_name, " +
			"minecraft_account_name, " +
			"age, " +
			"gender, " +
			"description, " +
			"race, " +
			"health, " +
			"`class`, " +
			"professions, " +
			"`level`, " +
			"experience, " +
			"stamina, " +
			"magical_stamina, " +
			"morality, " +
			"behavior, " +
			"x, " +
			"y, " +
			"z, " +
			"world) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private static final String INSERT_STATEMENT =
			"INSERT INTO characters (" +
			"char_name, " +
			"minecraft_account_name, " +
			"age, " +
			"gender, " +
			"description, " +
			"race, " +
			"health, " +
			"`class`, " +
			"professions, " +
			"`level`, " +
			"experience, " +
			"stamina, " +
			"magical_stamina, " +
			"morality, " +
			"behavior, " +
			"x, " +
			"y, " +
			"z, " +
			"world) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	
	private final String HOSTNAME;
	private final String PORT;
	private final String DATABASE;
	private final String USERNAME;
	private final String PASSWORD;
	private JavaPlugin plugin;
	private Connection conn;
	
	public MySQLDatabase(JavaPlugin plugin, String hostname, String port, String database, String username, String password) {
		HOSTNAME = hostname;
		PORT = port;
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		this.plugin = plugin;
		System.out.println(REPLACE_STATEMENT);
		System.out.println(INSERT_STATEMENT);
		conn = null;
	}

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to establish MySQL connection to " + HOSTNAME + ":" + PORT + "/" + DATABASE);
			ex.printStackTrace();
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to detect JDBC drivers for MySQL!");
			ex.printStackTrace();
		}
	}

	/**
	 * Initializes the database as necessary.
	 */
	public void initDatabase() {
		if(!isConnected()) {
			throw new NullPointerException("Not connected to the database");
		}
		try {
			Statement statement = conn.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS `characters` (" +
					"`uid` int(11) NOT NULL AUTO_INCREMENT," +
					"`char_name` text," +
					"`minecraft_account_name` text," +
					"`age` int(11) DEFAULT NULL," +
					"`gender` varchar(10) DEFAULT NULL," +
					"`description` text," +
					"`race` varchar(20) DEFAULT NULL," +
					"`health` int(11) DEFAULT NULL," +
					"`class` text," +
					"`professions` text," +
					"`level` int(11) DEFAULT NULL," +
					"`experience` int(11) DEFAULT NULL," +
					"`stamina` int(11) DEFAULT NULL," +
					"`magical_stamina` int(11) DEFAULT NULL," +
					"`morality` varchar(10) DEFAULT NULL," +
					"`behavior` varchar(10) DEFAULT NULL," +
					"`x` double DEFAULT NULL," +
					"`y` double DEFAULT NULL," +
					"`z` double DEFAULT NULL," +
					"`world` varchar(20) DEFAULT 'world', " +
					"PRIMARY KEY (`uid`)" +
					");");
			
			statement.execute("CREATE TABLE IF NOT EXISTS `players` (" +
					"`minecraft_account_name` text NOT NULL," +
					"`character_uid` int(11) DEFAULT NULL" +
					");");
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return (conn != null);
	}
	
	public void disconnect() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, "Unable to disconnect MySQL connection to " + HOSTNAME + ":" + PORT + "/" + DATABASE);
				ex.printStackTrace();
			}
		}
	}
	
	
	public void saveCharacter(GildorymCharacter gChar) {
		try {
			int posMod = 0; // In the event that we are including ID, then we must increase these
			// numbers by this amount.
			PreparedStatement statement = null;
			if(gChar.getUid() != -1) {
				statement = conn.prepareStatement(REPLACE_STATEMENT);
				
				statement.setInt(1, gChar.getUid());
				posMod = 1;
				
			}else {
				statement = conn.prepareStatement(INSERT_STATEMENT);
			}
			
			statement.setString(1 + posMod, gChar.getName());
			statement.setString(2 + posMod, gChar.getMcName());
			statement.setInt(3 + posMod, gChar.getCharCard().getAge());
			statement.setString(4 + posMod, gChar.getCharCard().getGender().name());
			statement.setString(5 + posMod, gChar.getCharCard().getDescription());
			statement.setString(6 + posMod, gChar.getCharCard().getRace().name());
			statement.setInt(7 + posMod, gChar.getCharCard().getHealth());
			statement.setString(8 + posMod, gChar.getCharClass() != null ? gChar.getCharClass().name() : null);
			statement.setString(9 + posMod, csv(gChar.getProfessions()));
			statement.setInt(10 + posMod, gChar.getLevel());
			statement.setInt(11 + posMod, gChar.getExperience());
			statement.setInt(12 + posMod, gChar.getStamina());
			statement.setInt(13 + posMod, gChar.getMagicalStamina());
			statement.setString(14 + posMod, gChar.getMorality() != null ? gChar.getMorality().name() : null);
			statement.setString(15 + posMod, gChar.getBehavior() != null ? gChar.getBehavior().name() : null);
			statement.setDouble(16 + posMod, gChar.getX());
			statement.setDouble(17 + posMod, gChar.getY());
			statement.setDouble(18 + posMod, gChar.getZ());
			statement.setString(19 + posMod, gChar.getWorld());
			statement.execute();
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to save character " + gChar + "!");
			e.printStackTrace();
		}
	}
	public ResultSet query(String queryString) {
		if (!isConnected()) {
			connect();
		}
		
		Statement queryStatement = null;
		ResultSet results = null;
		
		try {
			queryStatement = conn.createStatement();
			results = queryStatement.executeQuery(queryString);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to query database at " + HOSTNAME + ":" + PORT + "/" + DATABASE);
		}
		
		return results;
	}
	

	
	private String csv(CharacterProfession[] professions) {
		if(professions == null || professions.length == 0 || professions[0] == null)
			return null;
		StringBuilder result = new StringBuilder(professions[0].name());
		
		for(int i = 1; i < professions.length; i++) {
			result.append(",").append(professions[i].name());
		}
		
		return result.toString();
	}

}
