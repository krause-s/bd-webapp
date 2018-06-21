package de.uni_koeln.dh.lyra.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * processes geo data for reference strings by using nominatim
 *
 */
public class GeoTagger {

	private String nominatimJsonResponse;
	private Map<String, String> nominatimResponses = new HashMap<String, String>();
	private String filePath = "src/main/resources/nominatim/placeNamesCoordiantes.ser";

	public GeoTagger() {
		deserializeNominatimResponses();
	}

	/**
	 * searches for coordinates that nominatim provides for the given string. if
	 * nominatim don't give a result, method returns null
	 * 
	 * @param query
	 *            String to search in nominatim
	 * @return Array with lat[0] and lon[1]
	 * @throws IOException
	 */
	public Double[] findGeoData(String query) throws IOException {

		Double[] geoDates = new Double[2];
		
		if (nominatimResponses.containsKey(query)) {
			nominatimJsonResponse = nominatimResponses.get(query);
		} else {
			nominatimJsonResponse = Jsoup.connect(
					"http://nominatim.openstreetmap.org/search/" + query + "?format=json&addressdetails=1&limit=1")
					.ignoreContentType(true).execute().body();
			nominatimResponses.put(query, nominatimJsonResponse);
			serializePlaceCoordinates();
		}
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory
				.createParser(nominatimJsonResponse.substring(1, nominatimJsonResponse.length() - 1));

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

	/**
	 * searches for meta data that nominatim provides for the given string (e.g.
	 * country,...). if nominatim don't give a result, method returns empty
	 * string
	 * 
	 * @return
	 * @throws IOException
	 */
	public String findMetaData() throws IOException {
		String meta = "";
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory
				.createParser(nominatimJsonResponse.substring(1, nominatimJsonResponse.length() - 1));

		while (!parser.isClosed()) {
			JsonToken jsonToken = parser.nextToken();
			if (JsonToken.FIELD_NAME.equals(jsonToken)) {
				String fieldName = parser.getCurrentName();
				jsonToken = parser.nextToken();
				if ("country".equals(fieldName) || "county".equals(fieldName) || "state".equals(fieldName)
						|| "town".equals(fieldName)) {
					meta += parser.getText() + " ";
				}
			}
		}
		return meta;
	}
	
	
	/**
	 * serializes the found nominatim entries to avoid repeating the same requests to som-server
	 */
	public void serializePlaceCoordinates() {
		try {
			FileOutputStream fileOut = new FileOutputStream(filePath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(nominatimResponses);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void deserializeNominatimResponses() {
		if (new File(filePath).exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(filePath);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				nominatimResponses = (Map<String, String>) in.readObject();
				in.close();
				fileIn.close();
			} catch (IOException i) {
				i.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				c.printStackTrace();
				return;
			}
		}
	}


}
