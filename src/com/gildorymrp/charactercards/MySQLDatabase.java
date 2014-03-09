package com.gildorymrp.charactercards;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.gildorymrp.gildorymdb.CreatedCharacterInfo;
import com.gildorymrp.gildorymdb.GildorymCharacter;
import com.gildorymrp.gildorymclasses.CharacterBehavior;
import com.gildorymrp.gildorymclasses.CharacterClass;
import com.gildorymrp.gildorymclasses.CharacterMorality;
import com.gildorymrp.gildorymclasses.CharacterProfession;

public class MySQLDatabase {

	private static final String REPLACE_CHAR_STATEMENT =
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

	private static final String INSERT_CHAR_STATEMENT =
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
	
	private static final String SELECT_CHAR_STATEMENT = 
			"SELECT * FROM characters WHERE uid=?";
	
	private static final String REPLACE_PLAYER_CUR_CHAR = 
			"REPLACE INTO players (" +
			"minecraft_account_name," +
			"current_character_uid) VALUES (?, ?);";
	private static final String REPLACE_PLAYER_CUR_CHAR_CREATED_CHAR = 
			"REPLACE INTO players (" +
			"minecraft_account_name, " +
			"created_character_id, " +
			"current_character_uid) VALUES (?, ?, ?);";
	
	private static final String SELECT_CUR_CHAR_CREATED = 
			"SELECT * FROM players WHERE minecraft_account_name=?";
	
	private static final String SELECT_CREATED_CHARS =
			"SELECT * FROM created_characters WHERE id = ?;";

	private static final String INSERT_CREATED_CHAR = 
			"INSERT INTO created_characters (id, char_uid, created_utc, approved_by) " +
			"VALUES(?, ?, ?, ?);";

	private static final String CLEAR_CREATED_CHARS = 
			"DELETE FROM created_characters WHERE id = ?;";
	
	
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
					"`created_characters_id` int(11) DEFAULT NULL," + 
					"`current_character_uid` int(11) DEFAULT NULL" +
					");");
			
			statement.execute("CREATE TABLE IF NOT EXISTS `created_characters` (" +
					"`id` int(11) NOT NULL, " +
					"`char_uid` int(11) NOT NULL DEFAULT -1," +
					"`created_utc` BIGINT NOT NULL DEFAULT -1, " +
					"`approved_by` TEXT DEFAULT NULL" +
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
	
	/**
	 * Saves the specified gildorym character into the 'characters' table,
	 * doing an insert if the characters uid
	 * @param gChar
	 * @return success
	 */
	public boolean saveCharacter(GildorymCharacter gChar) {
		try {
			int posMod = 0; // In the event that we are including ID, then we must increase these
			// numbers by this amount.
			PreparedStatement statement = null;
			if(gChar.getUid() != -1) {
				statement = conn.prepareStatement(REPLACE_CHAR_STATEMENT);
				
				statement.setInt(1, gChar.getUid());
				posMod = 1;
				
			}else {
				statement = conn.prepareStatement(INSERT_CHAR_STATEMENT);
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
			
			boolean res = statement.execute();
			
			if(gChar.getUid() == -1) {
				statement = conn.prepareStatement("SELECT LAST_INSERT_ID()");
				ResultSet results = statement.executeQuery();
				
				results.next();
				int uid = results.getInt(1);
				results.close();
				gChar.setUid(uid);
			}
			return res;
			
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to save character " + gChar + "!");
			e.printStackTrace();
		}
		return false;
	}
	
	public GildorymCharacter loadCharacter(int uid) {
		PreparedStatement statement;
		try {
			statement = conn.prepareStatement(SELECT_CHAR_STATEMENT);
			statement.setInt(1, uid);
			
			ResultSet results = statement.executeQuery();
			
			results.next();
			
			GildorymCharacter result = new GildorymCharacter(uid);
			result.setName(results.getString(2));
			result.setMcName(results.getString(3));
			result.setCharCard(new CharacterCard(results.getInt(4), 
					Gender.valueOf(results.getString(5)),
					results.getString(6),
					Race.valueOf(results.getString(7)),
					results.getInt(8), 
					CharacterClass.valueOf(results.getString(9))));
			result.setCharClass(CharacterClass.valueOf(results.getString(9)));
			result.setProfessions(csv(results.getString(10)));
			result.setLevel(results.getInt(11));
			result.setExperience(results.getInt(12));
			result.setStamina(results.getInt(13));
			result.setMagicalStamina(results.getInt(14));
			result.setMorality(results.getString(15) != null ? CharacterMorality.valueOf(results.getString(15)) : CharacterMorality.NEUTRAL);
			result.setBehavior(results.getString(16) != null ? CharacterBehavior.valueOf(results.getString(16)) : CharacterBehavior.NEUTRAL);
			result.setX(results.getDouble(17));
			result.setY(results.getDouble(18));
			result.setZ(results.getDouble(19));
			result.setWorld(results.getString(20));
			
			results.close();
			
			
			return result;
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to load character with uid " + uid + "!");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean setCurrentCharacter(String playerName, int uid) {
		try {
			PreparedStatement statement = conn.prepareStatement(REPLACE_PLAYER_CUR_CHAR);
			statement.setString(1, playerName);
			statement.setInt(2, uid);
			return statement.execute();
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to set active character of " + playerName + " to " + uid + "!");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * createdId ought to match with the players first created characters uid, although this 
	 * is not required
	 * 
	 * @param playerName the players minecraft account name
	 * @param createdId the id associated with characters created by this player
	 * @param uid the active characters unique identifier
	 */
	public boolean setPlayerCharactersCreatedAndActive(String playerName, int createdId, int uid) {
		try {
			PreparedStatement statement = conn.prepareStatement(REPLACE_PLAYER_CUR_CHAR_CREATED_CHAR);
			statement.setString(1, playerName);
			statement.setInt(2, createdId);
			statement.setInt(3, uid);
			return statement.execute();
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to set active character of " + playerName + " to " + uid + "!");
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Returns the active character uid and the list of created
	 * character ids, such that the first (0) position in the array
	 * is the active and the second (1) position in the array is
	 * the id to find all of the created characters by this player
	 * 
	 * @param playerName
	 * @return active character uid, created characters id or null
	 */
	public int[] getActive(String playerName) {
		PreparedStatement statement;
		try {
			statement = conn.prepareStatement(SELECT_CUR_CHAR_CREATED);
			statement.setString(1, playerName);
			
			ResultSet results = statement.executeQuery();
			
			if(!results.next()) {
				results.close();
				return null;
			}
			
			int curr = results.getInt("current_character_uid");
			int cre = results.getInt("created_characters_id");
			
			results.close();
			return new int[]{curr, cre};
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to get player information about " + playerName);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns a list of created characters from the specified id (
	 * which can be retrieved per-player name using getActive(playerName))
	 * 
	 * @param createdId the id corresponding with the characters
	 * @return a list of created characters (potentially empty) or null if an error occurs
	 */
	public List<CreatedCharacterInfo> getCreatedCharacterInfo(int createdId) {
		try {
			PreparedStatement statement = conn.prepareStatement(SELECT_CREATED_CHARS);
			
			statement.setInt(1, createdId);
			
			ResultSet resultSet = statement.executeQuery();
			
			List<CreatedCharacterInfo> result = new ArrayList<>();
			
			while(resultSet.next()) {
				CreatedCharacterInfo cci = new CreatedCharacterInfo(createdId);
				cci.setCharUid(resultSet.getInt("char_uid"));
				cci.setCreatedUTC(resultSet.getLong("created_utc"));
				cci.setApprovedBy(resultSet.getString("approved_by"));
				result.add(cci);
			}
			
			resultSet.close();
			return result;
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retrieve created character info for id " + createdId);
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean addCreatedCharacterInfo(CreatedCharacterInfo cci) {
		try {
			PreparedStatement statement = conn.prepareStatement(INSERT_CREATED_CHAR);
			
			statement.setInt(1, cci.getId());
			statement.setInt(2, cci.getCharUid());
			statement.setLong(3, cci.getCreatedUTC());
			statement.setString(4, cci.getApprovedBy());
			
			return statement.execute();
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to add created character info " + cci);
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean clearCreatedCharacterInfo(int id) {
		try {
			PreparedStatement statement = conn.prepareStatement(CLEAR_CREATED_CHARS);
			
			statement.setInt(1, id);
			
			return statement.execute();
		}catch(SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retrieve clear created character info for id " + id);
			e.printStackTrace();
			return false;
		}
	}

	private String csv(Enum<?>[] e) {
		if(e == null || e.length == 0 || e[0] == null)
			return null;
		StringBuilder result = new StringBuilder(e[0].name());
		
		for(int i = 1; i < e.length; i++) {
			result.append(",").append(e[i].name());
		}
		
		return result.toString();
	}
	
	private CharacterProfession[] csv(String string) {
		if(string == null) 
			return new CharacterProfession[]{};
		String[] spl = string.split(",");
		CharacterProfession[] result = new CharacterProfession[spl.length];
		
		for(int i = 0; i < spl.length; i++) {
			result[i] = CharacterProfession.valueOf(spl[i]);
		}
		
		return result;
	}

}
