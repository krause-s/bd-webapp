package de.uni_koeln.dh.bd.processing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class GeoTagger {

	public HashMap<String, Double[]> getGeoDatesFromList(List<String> placesList) throws InterruptedException, IOException {
		HashMap<String, Double[]> geoDatesPlacesMap = new HashMap<String, Double[]>();
		for(String currentToken : placesList) {
			if(!geoDatesPlacesMap.containsKey(currentToken)){
				Double[] latLon = findGeoData(currentToken);
				if(latLon != null){
					System.out.println(currentToken + " " + latLon[0] + latLon[1]);
				geoDatesPlacesMap.put(currentToken, latLon);
				}
				Thread.sleep(1000);
			}
		}
		return geoDatesPlacesMap;
	}
	
	public HashMap<String, Double[]> getGeoDatesFromString(String text) throws InterruptedException, IOException {
		Scanner scan = new Scanner(text);
		HashMap<String, Double[]> geoDatesPlacesMap = new HashMap<String, Double[]>();
		while (scan.hasNext()) {
			String currentToken = scan.next().toLowerCase().replaceAll("[^a-zöäü]", "");
			if(!geoDatesPlacesMap.containsKey(currentToken)){
				Double[] latLon = findGeoData(currentToken);
				if(latLon != null){
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
