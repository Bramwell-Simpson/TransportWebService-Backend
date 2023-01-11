package uk.ac.mmu.advprog.hackathon.utils;

import java.sql.*;
import java.util.Set;


public class Tools {
	
	/**
	 * Set of valid stop types
	 */
	private static final Set<String> STOPTYPES = Set.of("BUS", "RLW", "MET", "FER", "AIR", "TXR");
	
	/**
	 * Checks if a column is null and returns an empty string, otherwise returns the value
	 * @param rs ResultSet to use
	 * @param strColName Column name to check against
	 * @return Returns either an empty string or value
	 * @throws SQLException
	 */
    static public String checkString(ResultSet rs, String strColName) throws SQLException {
        String value = rs.getString(strColName);
        return rs.wasNull() ? "" : value;
    }
    
    /**
     * Checks if a column is null and returns an empty string, otherwise returns the value
     * @param rs ResultSet to use
     * @param intCol Column index to check against
     * @return Returns either an empty string or value
     * @throws SQLException
     */
    static public String checkString(ResultSet rs, int intCol) throws SQLException {
        String value = rs.getString(intCol);
        return rs.wasNull() ? "" : value;
    }
    
    /**
     * Checks if a column is null and returns an empty string, otherwise returns the value
     * @param rs ResultSet to use
     * @param intCol Column index to check against
     * @return Returns either an empty string or value
     * @throws SQLException
     */
    static public String checkFloat(ResultSet rs, int intCol) throws SQLException {
        String value = Float.toString(rs.getFloat(intCol));
        return rs.wasNull() ? "" : value;
    }
    
    /**
     * Checks if given stop type is valid
     * @param stopType Stop Type to check
     * @return Returns false if given stop type isn't valid
     */
    public static boolean checkStopType(String stopType) {
    	
    	return STOPTYPES.contains(stopType)? true : false;
    }
    
    /**
     * Checks a single parameter for validity
     * @param local Parameter to check
     * @return returns true if the param is valid, otherwise false
     */
    public static boolean checkParams(String local) {
    	
    	if(local == null || local == "") {
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Checks a single parameter and the stop given
     * @param local Parameter to check
     * @param stop Stop type to check
     * @return returns true if params are valid, otherwise false
     */
    public static boolean checkParams(String local, String stop) {
    	
    	boolean local1 = true;
    	boolean stopType = true;
    	
    	if(local == null || local == "") {
    		local1 = false;
    	}
    	
    	if(stop == null || stop == "" ) {
    		stopType = false;
    	}
    	
    	if(!local1 || !stopType || !checkStopType(stop))
    	{
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Overload of checkParams, allows 2 params along with stop type given
     * @param local Parameter to check
     * @param local2 Second parameter to check
     * @param stop Stop type to check
     * @return returns true if params are valid, otherwise false
     */
    public static boolean checkParams(String local, String local2, String stop) {
    	
    	boolean check1 = true;
    	boolean check2 = true;
    	boolean stopType = true;
    	
    	if(local == null || local == "") {
    		check1 = false;
    	}
    	
    	if(local2 == null || local2 == "") {
    		check2 = false;
    	}
    	
    	if(stop == null || stop == "" ) {
    		stopType = false;
    	}
    	
    	if(!check1 || !check2 || !stopType || !checkStopType(stop))
    	{
    		return false;
    	}
    	
    	return true;
    }
}
