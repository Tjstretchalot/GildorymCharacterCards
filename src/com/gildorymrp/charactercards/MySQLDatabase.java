package com.gildorymrp.charactercards;

import java.sql.*;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class MySQLDatabase {

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
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdcb:mysql//" + HOSTNAME + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to establish MySQL connection to " + HOSTNAME + ":" + PORT + "/" + DATABASE);
			ex.printStackTrace();
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to detect JDBC drivers for MySQL!");
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
	
	public void update(String updateString) {
		if (!isConnected()) {
			connect();
		}
		Statement updateStatement = null;
		
		try {
			updateStatement = conn.createStatement();
			updateStatement.executeUpdate(updateString);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to update database at " + HOSTNAME + ":" + PORT + "/" + DATABASE);
			ex.printStackTrace();
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
	
	
}
