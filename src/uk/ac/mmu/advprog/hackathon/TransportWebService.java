package uk.ac.mmu.advprog.hackathon;
import static spark.Spark.*;
import static spark.Spark.port;

import java.sql.ResultSet;

import spark.Request;
import spark.Response;
import spark.Route;
import uk.ac.mmu.advprog.hackathon.utils.DB;
import uk.ac.mmu.advprog.hackathon.utils.JSONWriter;
import uk.ac.mmu.advprog.hackathon.utils.Tools;
import uk.ac.mmu.advprog.hackathon.utils.XMLWriter;

/**
 * Handles the setting up and starting of the web service
 * You will be adding additional routes to this class, and it might get quite large
 * Feel free to distribute some of the work to additional child classes, like I did with DB
 * @author You, Mainly!
 */
public class TransportWebService {

	/**
	 * Main program entry point, starts the web service
	 * @param args not used
	 */
	public static void main(String[] args) {		
		port(8088);
		enableCORS("*", "*", "*");
		
		//Simple route so you can check things are working...
		//Accessible via http://localhost:8088/test in your browser
		get("/test", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try (DB db = new DB()) {
					return "Number of Entries: " + db.getNumberOfEntries();
				}
			}			
		});
		
		//Returns all stops in a given locality
		get("/stopcount", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try (DB db = new DB()) {
					
					response.type("text/plain");
					
					String locality = request.queryParams("locality");
					
					if (!Tools.checkParams(locality)) {
						return "Invalid Request";
					}
					
					//Query db with given data and return
					return db.getLocalityStopCount(locality);
				}
			}			
		});
		
		//Returns all stops in a given locality and a specific stop type
		get("/stops", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try (DB db = new DB()) {
					
					//Initiate new JSONWriter
					JSONWriter stops = new JSONWriter();
					
					//Set content-type header 
					response.type("application/json");
					
					String locality = request.queryParams("locality");
					String stopType = request.queryParams("type");
					
					//Check that given parameters are valid
					if (!Tools.checkParams(locality, stopType)) {
						response.type("text/plain");
						return "Invalid Request";
					}
					
					//Query DB with data
					ResultSet stopsSet = db.getLocalityAndType(locality, stopType);
					
					//Return JSON
					return stops.writeStops(stopsSet);
				}
			}			
		});
		
		//Returns the 5 nearest stops to a given latitude and longitude and a specific stop type
		get("/nearest", new Route() {
			@Override
			public Object handle(Request request, Response response) throws Exception {
				try (DB db = new DB()) {
					
					JSONWriter stops = new JSONWriter();
					
					//Set content-type to application/json
					response.type("application/json");
					
					//Get params from request
					String stopType = request.queryParams("type");
					String lat = request.queryParams("latitude");
					String lon = request.queryParams("longitude");
					
					//Check params are valid
					if(!Tools.checkParams(lat, lon, stopType))
					{
						response.type("text/plain");
						return "Invalid Request";
					}
					
					//Query db with given values
					ResultSet stopsSet = db.getNearestStops(Float.parseFloat(lat), Float.parseFloat(lon), stopType);
					
					//Return JSON
					return stops.writeNearestStops(stopsSet);
				}
			}			
		});
		
		System.out.println("Server up! Don't forget to kill the program when done!");
	}
	
	private static void enableCORS(final String origin, final String methods, final String headers) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	    });
	}

}
