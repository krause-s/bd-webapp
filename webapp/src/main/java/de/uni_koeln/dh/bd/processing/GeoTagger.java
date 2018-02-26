package de.uni_koeln.dh.bd.processing;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import de.uni_koeln.dh.bd.data.Location;
import de.uni_koeln.dh.bd.data.Song;

public class GeoTagger {

	// TODO unterschiedliche Reference Strings auf gleiche Koordinaten werden zu
	// einem Ort zusammengeführt

	private Logger logger = LoggerFactory.getLogger(getClass());

	HashMap<String, Double[]> geoDatesPlacesMap = new HashMap<String, Double[]>();

	public Map<Location, List<Song>> getGeoDatesFromList(Map<String, List<Song>> places)
			throws InterruptedException, IOException {

		// Set<Location> locationsSet = new HashSet<Location>();
		Map<Location, List<Song>> locationsMap = new HashMap<Location, List<Song>>();
		for (Map.Entry<String, List<Song>> e : places.entrySet()) {
			String currentToken = e.getKey();
			Double[] latLon;
			if (geoDatesPlacesMap.containsKey(currentToken)) { // placename has already been queried
				latLon = geoDatesPlacesMap.get(currentToken);
			} else { // query placename
				latLon = findGeoData(currentToken);
				if (latLon == null) //if query didn't deliver a result
					continue;
				
				if (geoDatesPlacesMap.containsValue(latLon)) {
					
					logger.info("Coordinates are already listed: " + currentToken + " - " + latLon[0] + " - " + latLon[1]);
				}
				
				geoDatesPlacesMap.put(currentToken, latLon);
				Thread.sleep(1000);
			}

			logger.info(currentToken + " - " + latLon[0] + " - " + latLon[1]);
			locationsMap.put(new Location(currentToken, latLon[0], latLon[1]), e.getValue());

		}

		return locationsMap;
	}

	public HashMap<String, Double[]> getGeoDatesFromString(String text) throws InterruptedException, IOException {
		Scanner scan = new Scanner(text);
		HashMap<String, Double[]> geoDatesPlacesMap = new HashMap<String, Double[]>();
		while (scan.hasNext()) {
			String currentToken = scan.next().toLowerCase().replaceAll("[^a-zöäü]", "");
			if (!geoDatesPlacesMap.containsKey(currentToken)) {
				Double[] latLon = findGeoData(currentToken);
				if (latLon != null) {
					System.out.println(currentToken + " " + latLon[0] + latLon[1]);
					geoDatesPlacesMap.put(currentToken, latLon);
				}
				Thread.sleep(1000);
			}
		}
		scan.close();
		return geoDatesPlacesMap;
	}

	public Double[] findGeoData(String query) throws IOException {

		Double[] geoDates = new Double[2];
		String jsonResponse = Jsoup
				.connect("http://nominatim.openstreetmap.org/search/" + query + "?format=json&addressdetails=1&limit=1")
				.ignoreContentType(true).execute().body();

		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createParser(jsonResponse.substring(1, jsonResponse.length() - 1));

		while (!parser.isClosed()) {
			JsonToken jsonToken = parser.nextToken();

			if (JsonToken.FIELD_NAME.equals(jsonToken)) {
				String fieldName = parser.getCurrentName();
				jsonToken = parser.nextToken();
				if ("lat".equals(fieldName)) {
					geoDates[0] = parser.getValueAsDouble();
				} else if ("lon".equals(fieldName)) {
					geoDates[1] = parser.getValueAsDouble();
					return geoDates;
				}
			}
		}
		return null;
	}

}
