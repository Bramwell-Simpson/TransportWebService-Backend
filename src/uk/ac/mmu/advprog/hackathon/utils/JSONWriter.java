package uk.ac.mmu.advprog.hackathon.utils;

import org.json.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JSONWriter {
	
	/**
	 * Takes a ResultSet and writes it into a JSON Array
	 * @param rs Computed Stops ResultsSet
	 * @return JSON Array
	 */
	public JSONArray writeStops(ResultSet rs) {
		
		JSONArray jsonStops = new JSONArray();
		
		try {
			while(rs.next()) {
				
				//Create stop object
				JSONObject stop = new JSONObject();
				
				//Write name and locality to stop
				stop.put("name", Tools.checkString(rs, 2));
				stop.put("locality", Tools.checkString(rs, 7));
				
				//create location sub-object
				JSONObject location = new JSONObject();
				
				location.put("indicator", Tools.checkString(rs, 5)); //5
				location.put("bearing", Tools.checkString(rs, 6)); //6
				location.put("street", Tools.checkString(rs, 4)); //4
				location.put("landmark", Tools.checkString(rs, 3)); //3
				
				//write final location to stop
				stop.put("location", location);
				
				stop.put("type", Tools.checkString(rs, 10));
				
				//write stop object to array
				jsonStops.put(stop); 
				
			}
		}
		catch(SQLException sqle) {
			System.err.println(sqle.getSQLState());
		}
		
		return jsonStops;
	}
	
	public JSONArray writeNearestStops(ResultSet rs) {
		JSONArray jsonStops = new JSONArray();
		
		try {
			while(rs.next()) {
				JSONObject stop = new JSONObject();
				
				stop.put("name", Tools.checkString(rs, 2));
				stop.put("locality", Tools.checkString(rs, 7));
				stop.put("code", Tools.checkString(rs, 1));
				
				JSONObject location = new JSONObject();
				
				location.put("street", Tools.checkString(rs, 4));
				location.put("landmark", Tools.checkString(rs, 3));
				location.put("latitude", Tools.checkString(rs, 9));
				location.put("longitude", Tools.checkString(rs, 8));
				
				stop.put("location", location);
				jsonStops.put(stop);
			}
		}
		catch(SQLException sqle) {
			System.err.println(sqle.getSQLState());
		}
		
		return jsonStops;
	}
}
