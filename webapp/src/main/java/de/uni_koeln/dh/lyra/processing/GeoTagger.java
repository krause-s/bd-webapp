package de.uni_koeln.dh.lyra.processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni_koeln.dh.lyra.data.Place;
import de.uni_koeln.dh.lyra.data.PopUp;

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
	@SuppressWarnings("deprecation")
	public Double[] findGeoData(String query) throws IOException {
		if (nominatimResponses.containsKey(query)) {
			nominatimJsonResponse = nominatimResponses.get(query);
		} else {
			nominatimJsonResponse = Jsoup.connect(
					"http://nominatim.openstreetmap.org/search/" + query + "?format=json&addressdetails=1&limit=5")
					.ignoreContentType(true).validateTLSCertificates(false).execute().body();		// TODO remove deprecation (call required for connecting)
			nominatimResponses.put(query, nominatimJsonResponse);
			serializePlaceCoordinates();
		}
		Double[] geoDates = parseJSON(nominatimJsonResponse, 0);
		return geoDates;
	}

	public List<Place> getAlternativePlaces(String placeName) throws JsonParseException, IOException {
		List<Place> places = new ArrayList<Place>();
		for (int i = 0; i < 4; i++) {
			if (parseJSON(nominatimResponses.get(placeName), i) != null) {
				Double[] currCoordinates = parseJSON(nominatimResponses.get(placeName), i);
				if (currCoordinates[0] != null && currCoordinates[1] != null && currCoordinates[0] != 0
						&& currCoordinates[1] != 0) {
					Place currPlace = new Place(currCoordinates[0], currCoordinates[1]);
					currPlace.setMeta(findMetaData(placeName, i));
					PopUp popUp = new PopUp(placeName, "");
					currPlace.addPopUp(popUp );
					places.add(currPlace);
				}
			}
		}
		return places;
	}

	private Double[] parseJSON(String jsonToParse, int jsonArrayIndex) throws JsonParseException, IOException {
		Double[] geoDates = new Double[2];
		Map<String, String>[] jsonMapMap;
		ObjectMapper objectMapper = new ObjectMapper();
		jsonMapMap = (Map<String, String>[]) objectMapper.readValue(jsonToParse, HashMap[].class);
		if (jsonMapMap.length > jsonArrayIndex) {
			if (jsonMapMap[jsonArrayIndex].get("lat") != null) {
				geoDates[0] = Double.valueOf(jsonMapMap[jsonArrayIndex].get("lat"));
			}
			if (jsonMapMap[jsonArrayIndex].get("lon") != null) {
				geoDates[1] = Double.valueOf(jsonMapMap[jsonArrayIndex].get("lon"));
			}
			return geoDates;
		} else {
			return null;
		}
	}

	/**
	 * searches for meta data that nominatim provides for the given string (e.g.
	 * country,...). if nominatim don't give a result, method returns empty
	 * string
	 * 
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String findMetaData(String placeName, int jsonArrayIndex) throws IOException {
		String nominatimJsonResponse = nominatimResponses.get(placeName);
		String meta = "";
		Map<String, LinkedHashMap<String, String>>[] jsonMap;
		ObjectMapper objectMapper = new ObjectMapper();
		jsonMap = (Map<String, LinkedHashMap<String, String>>[]) objectMapper.readValue(nominatimJsonResponse,
				HashMap[].class);
		if (jsonMap.length > jsonArrayIndex) {
			Map<String, String> addressMap = jsonMap[jsonArrayIndex].get("address");
			if (addressMap.get("country") != null) {
				meta += addressMap.get("country");
			}
			if (addressMap.get("county") != null) {
				meta += addressMap.get("county");
			}
			if (addressMap.get("state") != null) {
				meta += addressMap.get("state");
			}
			if (addressMap.get("town") != null) {
				meta += addressMap.get("town");
			}
			return meta;
		} else {
			return "";
		}
	}

	/**
	 * serializes the found nominatim entries to avoid repeating the same
	 * requests to som-server
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
