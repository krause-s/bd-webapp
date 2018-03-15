package de.uni_koeln.dh.lyra.processing;

import java.io.IOException;
import org.jsoup.Jsoup;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * processes geo data for reference strings by using
 * nominatim
 *
 */
public class GeoTagger {

	private String nominatimJsonResponse;

	/**
	 * searches for coordinates that nominatim provides for the given string. if
	 * nominatim don't give a result, method returns null
	 * @param query String to search in nominatim
	 * @return Array with lat[0] and lon[1]
	 * @throws IOException
	 */
	public Double[] findGeoData(String query) throws IOException {

		Double[] geoDates = new Double[2];
		nominatimJsonResponse = Jsoup
				.connect("http://nominatim.openstreetmap.org/search/" + query + "?format=json&addressdetails=1&limit=1")
				.ignoreContentType(true).execute().body();

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
	 * searches for meta data that nominatim provides for the given string (e.g. country,...). 
	 * if nominatim don't give a result, method returns empty string
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
				if ("country".equals(fieldName) || "county".equals(fieldName) || "state".equals(fieldName)  || "town".equals(fieldName)) {
					meta += parser.getText() + " ";
				}
			}
		}
		return meta;
	}

}
