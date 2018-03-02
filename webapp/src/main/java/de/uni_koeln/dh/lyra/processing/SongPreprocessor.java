package de.uni_koeln.dh.lyra.processing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.lyra.data.Song;
import de.uni_koeln.dh.lyra.model.place.Place;
import de.uni_koeln.dh.lyra.model.place.PopUp;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class SongPreprocessor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private TokenizerME tokenizer;
	private NameFinderME nameFinder;
	private GeoTagger tagger;

	/**
	 * contains already annotated pop ups for the given lyrics
	 */
	private Map<String, List<PopUp>> lyricsWithPopUps = new HashMap<String, List<PopUp>>();
	
	private List<Place> placesToEvaluate = new ArrayList<Place>();
	private Map<String, Double[]> placeNameCoordinates = new HashMap<String, Double[]>();
	
	
	public SongPreprocessor() {
		tagger = new GeoTagger();
		
		InputStream modelIn = null;
		TokenizerModel model = null;

		try {
			modelIn = new FileInputStream("src/main/resources/nlp/classifiers/en-token.bin");
			model = new TokenizerModel(modelIn);
			
			InputStream modelIn1 = new FileInputStream("src/main/resources/nlp/classifiers/en-ner-location.bin");
			TokenNameFinderModel NFmodel = new TokenNameFinderModel(modelIn1);
			nameFinder = new NameFinderME(NFmodel);			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}			
		tokenizer = new TokenizerME(model);		
	}
	
	public List<Place> getPlacesToEvaluate() {
		return placesToEvaluate;
	}
	
	public Song tokenizeSong(Song song) {		
		String lyrics = song.getLyrics();
		if (lyrics.equals(""))
			return null;
		
		String tokens[] = null;
		tokens = tokenizer.tokenize(lyrics);
		song.setTokens(tokens);
		return song;
	}
		
//		// TEST
//				logger.info("analyze token freq");
//
//				LyricsAnalyzer analyzer = new LyricsAnalyzer();
//				songs = analyzer.getWeights(songs);
//				List<Entry<String, Double>> weightsOfYear = analyzer.getWeightsForYear(songs, 1980);
//				logger.info(1980 + "");
//				for (int i = 0; i < 10; i++) {
//					Double weight = weightsOfYear.get(i).getValue();
//					String feature = weightsOfYear.get(i).getKey();
//					logger.info(feature + " " + weight);
//
//				}
//				logger.info(1990 + "");
//				weightsOfYear = analyzer.getWeightsForYear(songs, 1990);
//
//				for (int i = 0; i < 10; i++) {
//					Double weight = weightsOfYear.get(i).getValue();
//					String feature = weightsOfYear.get(i).getKey();
//					logger.info(feature + " " + weight);
//
//				}
//
//				System.exit(0);
//				// ENDE
	
	public void addPlaces(Song song) {
		List<PopUp> foundPopUps = createPopUps(song);
		
		//proofs for each pop up if nominatim founds coordinates
		for (PopUp popUp : foundPopUps) {
			String placeName = popUp.getPlaceName();
			Double[] latLon = new Double[2];
			if(placeNameCoordinates.containsKey(placeName))
				latLon = placeNameCoordinates.get(placeName);
			else {
				try {
					latLon = tagger.findGeoData(placeName);
					placeNameCoordinates.put(placeName, latLon);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(latLon == null)
				continue;
			Place currPlace = new Place(latLon[0], latLon[1]); //TODO right order?
			if(placesToEvaluate.contains(currPlace)) {
				currPlace = placesToEvaluate.get(placesToEvaluate.indexOf(currPlace));
				currPlace.addPopUp(popUp);
			} else {
				currPlace.addPopUp(popUp);
				placesToEvaluate.add(currPlace);
			}								
		}	
	}

	/**
	 * searches in the lyrics with NER for place-references,
	 * creates for each reference a popUp
	 * @param lyrics
	 * @return
	 */
	public List<PopUp> createPopUps(Song song) {
		String lyrics = song.getLyrics();

		if (lyrics.equals(""))
			return null;
		String tokens[] = song.getTokens();
		
		List<PopUp> annotatedPopUps;
		
		if (lyricsWithPopUps.containsKey(lyrics)) { //lyrics are already processed with NER
			annotatedPopUps = lyricsWithPopUps.get(lyrics);
		} else { //process lyrics with NER
			
			annotatedPopUps = new ArrayList<PopUp>();
	
			Span[] nameSpans = nameFinder.find(tokens);
			
			//iterate over all found named entities
			for (Span s : nameSpans) {
				String placeName = "";

				if (s.getProb() < 0.75) // TODO evaluate threshold
					continue;
				for (int index = s.getStart(); index < s.getEnd(); index++) {
					placeName += (tokens[index] + " ");
				}

				placeName = placeName.trim().toLowerCase();

				int startQuote = s.getStart() - 2;
				if(startQuote < 0)
					startQuote = 0;
				int endQuote = s.getEnd() + 3;
				if (endQuote > (tokens.length))
					endQuote = tokens.length;
				
				StringBuilder sb = new StringBuilder();
				for (int i = startQuote; i < endQuote; i++) {
					sb.append(tokens[i] + " ");
				}
				String quote = sb.toString();
				PopUp popUp = new PopUp(placeName, quote, song);
//				logger.info(placeName + ": " + quote);
				annotatedPopUps.add(popUp);
			}
		lyricsWithPopUps.put(lyrics, annotatedPopUps);
		}
		
		return annotatedPopUps;
		
	}

}
