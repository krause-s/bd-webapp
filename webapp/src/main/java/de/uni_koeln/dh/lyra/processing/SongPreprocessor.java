package de.uni_koeln.dh.lyra.processing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_koeln.dh.lyra.data.Place;
import de.uni_koeln.dh.lyra.data.PopUp;
import de.uni_koeln.dh.lyra.data.Song;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 * class manages song preprocessing in terms of tokenization and name entity
 * recognition
 * 
 * @author Johanna
 *
 */
public class SongPreprocessor {

	private TokenizerME tokenizer;
	private NameFinderME nameFinder;
	private GeoTagger tagger;

	/**
	 * contains already annotated pop ups for the given lyrics
	 */
	private Map<String, List<PopUp>> lyricsWithPopUps = new HashMap<String, List<PopUp>>();

	/**
	 * contains all created places objects
	 */
	private List<Place> placesToEvaluate = new ArrayList<Place>();

	/**
	 * contains all queried strings and their results
	 */
	private Map<String, Double[]> placeNameCoordinates = new HashMap<String, Double[]>();
	// TODO serialize map for all lyra users

	/**
	 * initializes geo tagger, tokenizer and NER finder
	 */
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

	/**
	 * 
	 * @return all (non-evaluated) found places
	 */
	public List<Place> getPlacesToEvaluate() {
		return placesToEvaluate;
	}

	/**
	 * searches for coordinates to the given place name. if name has already
	 * been queried, gives back the stored coordinates
	 * 
	 * @param placeName
	 * @return GPS coordinates ( [0] = lat, [1] = lon )
	 */
	public Double[] getCoordinates(String placeName) {
		Double[] toReturn = new Double[2];
		if (placeNameCoordinates.containsKey(placeName))
			toReturn = placeNameCoordinates.get(placeName);
		else
			try {
				toReturn = tagger.findGeoData(placeName);
				placeNameCoordinates.put(placeName, toReturn);
			} catch (IOException e) {
				e.printStackTrace();
			}

		return toReturn;
	}

	/**
	 * tokenizes the lyrics of a song and sets the tokens for the given song
	 * object
	 * 
	 * @param song
	 * @return
	 */
	public Song tokenizeSong(Song song) {
		String lyrics = song.getLyrics();
		if (lyrics.equals(""))
			return null;

		String tokens[] = null;
		tokens = tokenizer.tokenize(lyrics);
		song.setTokens(tokens);
		return song;
	}

	/**
	 * searches for places in the lyrics, searches for coordinates for the found
	 * named entities. adds the found place references and coordinates to a
	 * placesToEvaluate list
	 * 
	 * @param song
	 */
	public void addPlaces(Song song) {
		List<PopUp> foundPopUps = createPopUps(song);

		// proofs for each pop up if nominatim founds coordinates
		for (PopUp popUp : foundPopUps) {
			String placeName = popUp.getPlaceName();
			Double[] latLon = getCoordinates(placeName);
			String meta = "";
			try {
				meta = tagger.findMetaData();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (latLon == null)
				continue;
			Place currPlace = new Place(latLon[0], latLon[1]);
			currPlace.setMeta(meta);
			if (placesToEvaluate.contains(currPlace)) {
				currPlace = placesToEvaluate.get(placesToEvaluate.indexOf(currPlace));
				currPlace.addPopUp(popUp);
			} else {
				currPlace.addPopUp(popUp);
				placesToEvaluate.add(currPlace);
			}
		}
	}

	/**
	 * searches in the lyrics with NER for place-references, creates for each
	 * reference a popUp. if one place name is references several times in the
	 * lyrics, the quotes are contained in one pop up
	 * 
	 * @param song
	 * @return
	 */
	public List<PopUp> createPopUps(Song song) {
		String lyrics = song.getLyrics();

		if (lyrics.equals(""))
			return null;
		String tokens[] = song.getTokens();

		List<PopUp> annotatedPopUps;

		if (lyricsWithPopUps.containsKey(song.getUuid())) { // lyrics are
															// already
			// processed with NER
			annotatedPopUps = lyricsWithPopUps.get(song.getUuid());
		} else { // process lyrics with NER

			annotatedPopUps = new ArrayList<PopUp>();

			Span[] nameSpans = nameFinder.find(tokens);

			// iterate over all found named entities
			for (Span s : nameSpans) {
				String placeName = "";

				if (s.getProb() < 0.75) // TODO evaluate threshold
					continue;
				for (int index = s.getStart(); index < s.getEnd(); index++) {
					placeName += (tokens[index] + " ");
				}

				placeName = placeName.trim().toLowerCase();

				int startQuote = s.getStart() - 2;
				if (startQuote < 0)
					startQuote = 0;
				int endQuote = s.getEnd() + 3;
				if (endQuote > (tokens.length))
					endQuote = tokens.length;

				StringBuilder sb = new StringBuilder();
				for (int i = startQuote; i < endQuote; i++) {
					sb.append(tokens[i] + " ");
				}
				String quote = sb.toString();
				PopUp pu;
				if ((pu = placeAlreadyMentionedInSong(annotatedPopUps, placeName)) != null) {
					pu.setContent(pu.getContent() + "[...]" + quote);
				} else {
					PopUp popUp = new PopUp(placeName, quote, song);
					annotatedPopUps.add(popUp);
				}
				// logger.info(placeName + ": " + quote);

			}
			lyricsWithPopUps.put(song.getUuid(), annotatedPopUps);
		}

		return annotatedPopUps;

	}

	private PopUp placeAlreadyMentionedInSong(List<PopUp> annotatedPopUps, String placeName) {
		for (PopUp pu : annotatedPopUps) {
			if (pu.getPlaceName().equals(placeName)) {
				return pu;
			}
		}
		return null;
	}

}
