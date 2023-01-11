package uk.ac.mmu.advprog.hackathon.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles database access from within your web service
 * @author You, Mainly!
 */
public class DB implements AutoCloseable {
	
	//allows us to easily change the database used
	private static final String JDBC_CONNECTION_STRING = "jdbc:sqlite:./data/NaPTAN.db";
	
	//allows us to re-use the connection between queries if desired
	private Connection connection = null;
	
	/**
	 * Creates an instance of the DB object and connects to the database
	 */
	public DB() {
		try {
			connection = DriverManager.getConnection(JDBC_CONNECTION_STRING);
		}
		catch (SQLException sqle) {
			error(sqle);
		}
	}
	
	/**
	 * Returns the number of entries in the database, by counting rows
	 * @return The number of entries in the database, or -1 if empty
	 */
	public int getNumberOfEntries() {
		int result = -1;
		try {
			Statement s = connection.createStatement();
			ResultSet results = s.executeQuery("SELECT COUNT(*) AS count FROM Stops");
			while(results.next()) { //will only execute once, because SELECT COUNT(*) returns just 1 number
				result = results.getInt(results.findColumn("count"));
			}
		}
		catch (SQLException sqle) {
			error(sqle);
			
		}
		return result;
	}
	/**
	 * Returns the number of stops in a given locality
	 * @param locality Given area
	 * @return Number of stops or 0 if locality does not exist
	 */
	public int getLocalityStopCount(String locality) {
		int result = -1;
		
		String sql = "SELECT COUNT(*) AS Number FROM Stops WHERE LocalityName=?";
		
		try {
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, locality);
			
			ResultSet results = ps.executeQuery();
			
			while(results.next()) { //will only execute once, because SELECT COUNT(*) returns just 1 number
				
				result = results.getInt(results.findColumn("Number"));
			}
		}
		catch (SQLException sqle) {
			error(sqle);
		}
		return result;
	}
	
	/**
	 * Returns all stops in a given locality and a specific stop type (Bus, Metro etc)
	 * @param locality Area to check
	 * @param stopType Stop type to check
	 * @return ResultSet to process
	 */
	public ResultSet getLocalityAndType(String locality, String stopType) {
		
		ResultSet rs = null;
		
		String sql = "SELECT * FROM Stops WHERE LocalityName=? AND StopType=?";
		
		try {
			
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, locality);
			ps.setString(2, stopType);
			
			rs = ps.executeQuery();
		}
		catch (SQLException sqle) {
			error(sqle);
			System.err.println(sqle.getErrorCode());
			System.err.println(sqle.getMessage());
		}
		return rs;
	}
	
	/**
	 * Returns the 5 nearest stops to a set latitude and longitude
	 * @param lat Latitude position
	 * @param lon Longitude position
	 * @param stopType Type of stop to search for 
	 * @return ResultSet for processing
	 */
	public ResultSet getNearestStops(float lat, float lon, String stopType) {
		
		ResultSet rs = null;
		
		String sql = "SELECT * FROM Stops WHERE StopType = ? AND Latitude IS NOT NULL AND Longitude IS NOT NULL ORDER BY " 
		+ "(((? - Latitude) * (? - Latitude)) + (0.595 * ((? - Longitude) * (? - Longitude)))) ASC LIMIT 5;";
		
		try {
			PreparedStatement ps = connection.prepareStatement(sql);
			
			//Set values for statement
			ps.setString(1, stopType);
			ps.setFloat(2, lat);
			ps.setFloat(3, lat);
			ps.setFloat(4, lon);
			ps.setFloat(5, lon);
			
			rs = ps.executeQuery();
		}
		catch(SQLException sqle) {
			error(sqle);
		}
		
		return rs;
	}
	
	/**
	 * Closes the connection to the database, required by AutoCloseable interface.
	 */
	@Override
	public void close() {
		try {
			if ( !connection.isClosed() ) {
				connection.close();
			}
		}
		catch(SQLException sqle) {
			error(sqle);
		}
	}

	/**
	 * Prints out the details of the SQL error that has occurred, and exits the programme
	 * @param sqle Exception representing the error that occurred
	 */
	private void error(SQLException sqle) {
		System.err.println("Problem Opening Database! " + sqle.getClass().getName());
		sqle.printStackTrace();
		System.exit(1);
	}
}
