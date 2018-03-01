package de.uni_koeln.dh.lyra.processing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.lyra.data.Location;
import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class SongPreprocessor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private TokenizerME tokenizer;
	/**
	 * Set that contains all found places in the corpus
	 */
	private Map<String, List<Song>> places = new HashMap<String, List<Song>>();
	/**
	 * Set that contains all lyrics that are already processed by NER
	 */
	private Map<String, List<String>> lyricsWithPlaces = new HashMap<String, List<String>>();

	/**
	 * searches in every song for places (with NER) and gets the latitude and
	 * longitude for the found places
	 * 
	 * @param songs
	 * @return map with places and list of songs that refer to the place
	 * @throws IOException
	 */
	public Map<Place, List<Song>> detectNamedEntities(List<Song> songs) throws IOException {

		logger.info("detect named entities");
		InputStream modelIn = null;

		modelIn = new FileInputStream("src/main/resources/nlp/classifiers/en-token.bin");
		TokenizerModel model = new TokenizerModel(modelIn);
		tokenizer = new TokenizerME(model);

		for (Song song : songs) {
			searchPlaces(song);
		}
		
		// TEST
				logger.info("analyze token freq");

				LyricsAnalyzer analyzer = new LyricsAnalyzer();
				songs = analyzer.getWeights(songs);
				List<Entry<String, Double>> weightsOfYear = analyzer.getWeightsForYear(songs, 1980);
				logger.info(1980 + "");
				for (int i = 0; i < 10; i++) {
					Double weight = weightsOfYear.get(i).getValue();
					String feature = weightsOfYear.get(i).getKey();
					logger.info(feature + " " + weight);

				}
				logger.info(1990 + "");
				weightsOfYear = analyzer.getWeightsForYear(songs, 1990);

				for (int i = 0; i < 10; i++) {
					Double weight = weightsOfYear.get(i).getValue();
					String feature = weightsOfYear.get(i).getKey();
					logger.info(feature + " " + weight);

				}

				System.exit(0);
				// ENDE


		GeoTagger tagger = new GeoTagger();
		try {
			Map<Place, List<Song>> tagged = tagger.getGeoDatesFromList(places);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		// TODO create return map
		return null;
	}

	private void searchPlaces(Song song) throws IOException {
		String lyrics = song.getLyrics();

		if (lyrics.equals(""))
			return;

		if (lyricsWithPlaces.containsKey(lyrics)) { //lyrics are already processed with NER

			String tokens[] = null;
			tokens = tokenizer.tokenize(lyrics);
			// TODO stemmer, satzzeichen entfernen,....
			song.setTokens(tokens);

			List<String> annotatedPlaces = lyricsWithPlaces.get(lyrics);
			for (String placeName : annotatedPlaces) {
				List<Song> currentSongs = new ArrayList<Song>();
				if (places.containsKey(placeName))
					currentSongs = places.get(placeName);
				currentSongs.add(song);
				places.put(placeName, currentSongs);
			}
		} else { //process lyrics with NER
			
			List<String> annotatedPlaces = new ArrayList<String>();
			
			String tokens[] = null;
			tokens = tokenizer.tokenize(lyrics);
			song.setTokens(tokens);

			InputStream modelIn = new FileInputStream("src/main/resources/nlp/classifiers/en-ner-location.bin");
			TokenNameFinderModel NFmodel = new TokenNameFinderModel(modelIn);
			NameFinderME nameFinder = new NameFinderME(NFmodel);

			Span[] nameSpans = nameFinder.find(tokens);

			for (Span s : nameSpans) {
				String placeName = "";

				if (s.getProb() < 0.75) // TODO evaluate threshold
					continue;
				for (int index = s.getStart(); index < s.getEnd(); index++) {
					placeName += (tokens[index] + " ");
				}
				// TODO index of place name

				// logger.info(placeName + " - " + s.getProb());
				placeName = placeName.trim().toLowerCase();
				
				List<Song> currentSongs = new ArrayList<Song>();
				if (places.containsKey(placeName))
					currentSongs = places.get(placeName);
				currentSongs.add(song);
				places.put(placeName, currentSongs);
				
				annotatedPlaces.add(placeName);

				// for(int i = 0; i < lines.length; i++) {
				// if(lines[i].toLowerCase().contains(placeName)) {
				// Citation citation = new Citation(song, lines[i]);
				// List<Citation> currentCitation = new ArrayList<Citation>();
				// if(places.containsKey(placeName))
				// currentCitation = places.get(placeName);
				// currentCitation.add(citation);
				// places.put(placeName, currentCitation);
				// }
				// }

			}
			lyricsWithPlaces.put(lyrics, annotatedPlaces);
		}

		
//		logger.info(lyricsWithPlaces.size() + " unique Lyrics");
	}

}
