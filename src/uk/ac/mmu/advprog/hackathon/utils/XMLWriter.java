package uk.ac.mmu.advprog.hackathon.utils;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLWriter {
	
	public XMLWriter()
	{
		//empty
	}
	
	/**
	 * Writes the given ResultSet to an XML Document
	 * @param rs ResultSet to write
	 * @return Returns a Writer object
	 */
	public Writer writeStops (ResultSet rs) {
		
		Document stops = null;
		Writer output = null;
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbf.newDocumentBuilder(); 
			stops = dBuilder.newDocument();
			
			Element nearestStop = stops.createElement("NearestStops");
			stops.appendChild(nearestStop);
			
			while(rs.next()) {
				
				Element stop = stops.createElement("Stop");
				nearestStop.appendChild(stop);
				
				Attr naptanCode = stops.createAttribute("code");
				naptanCode.setValue(Tools.checkString(rs, 1));
				stop.setAttributeNode(naptanCode);
				
				Element name = stops.createElement("Name");
				name.setTextContent(Tools.checkString(rs, 2));
				
				Element locality = stops.createElement("Locality");
				locality.setTextContent(Tools.checkString(rs, 7));
				
				stop.appendChild(name);
				stop.appendChild(locality);
				
				// --- LOCATION INNER OBJECT ---
				Element location = stops.createElement("Location");
				stop.appendChild(location);
				
				Element street = stops.createElement("Street");
				street.setTextContent(Tools.checkString(rs, 4));
				
				Element landmark = stops.createElement("Landmark");
				landmark.setTextContent(Tools.checkString(rs, 3));
				
				Element lat = stops.createElement("latitude");
				lat.setTextContent(Tools.checkFloat(rs, 9));
				
				Element lon = stops.createElement("Longitude");
				lon.setTextContent(Tools.checkFloat(rs, 8));
				
				location.appendChild(street);
				location.appendChild(landmark);
				location.appendChild(lat);
				location.appendChild(lon);
			}
			
			//Output to string 
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			output = new StringWriter();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			DOMSource source = new DOMSource(stops);
			StreamResult result = new StreamResult(output);
			transformer.transform(source, new StreamResult(output));
		}
		catch (ParserConfigurationException | SQLException | TransformerException ioe) {
			System.err.println("Error creating XML: " + ioe);
		}
		
		return output;
	}
}
