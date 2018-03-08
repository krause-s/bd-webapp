package de.uni_koeln.dh.lyra.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uni_koeln.dh.lyra.data.Song;

public class LyricsAnalyzer {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private List<String> featureUnitOrder;
	private Map<String, Integer> docFrequencies = new HashMap<String, Integer>();
	private List<Song> corpus = new ArrayList<Song>();

	/**
	 * max term frequency of the current song
	 */
	private int maxTF;

	private Comparator<Map.Entry<String, Double>> comp = new Comparator<Map.Entry<String, Double>>() {

		@Override
		public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
			return e2.getValue().compareTo(e1.getValue());
		}
	};

	public LyricsAnalyzer(List<Song> allSongs) {
		this.corpus = allSongs;
	}

	public List<Song> getWeights() {

		if (featureUnitOrder == null)
			featureUnitOrder = getFeatureUnits();

		docFrequencies = getDocFrequencies();
		for (Song song : corpus) {
			Map<String, Integer> termFrequencies = getTermFrequencies(song.getTokens());
			song.setTermFreqs(termFrequencies);
			double[] vector = new double[this.featureUnitOrder.size()];
			int i = 0;
			for (String featureUnit : this.featureUnitOrder) {
				double tfidf = 0;
				if (termFrequencies.containsKey(featureUnit)) {
					double ntf = ((double) (termFrequencies.get(featureUnit)) / maxTF);
					double idf = (double) Math.log((double) (corpus.size()) / (docFrequencies.get(featureUnit)));
					tfidf = ntf * idf;
				}
				vector[i++] = tfidf;
			}

			song.setWeights(vector);
		}

		return corpus;
	}

	/**
	 * counts term frequency of each given term. computes the frequency of the most
	 * frequently term (used to norm frequencies afterwards)
	 * 
	 * @param tokens
	 * @return
	 */
	private Map<String, Integer> getTermFrequencies(String[] tokens) {
		Map<String, Integer> tfs = new TreeMap<String, Integer>();
		maxTF = 1;

		for (int i = 0; i < tokens.length; i++) {
			String featureUnit = tokens[i];
			if (tfs.containsKey(featureUnit)) {
				int tf = tfs.get(featureUnit) + 1;
				tfs.put(featureUnit, tf);
				if (tf > maxTF) {
					maxTF = tf;
				}
			} else {
				tfs.put(featureUnit, 1);
			}
		}
		return tfs;
	}

	/**
	 * creates a list of all features in the corpus
	 * 
	 * @param corpus
	 * @return
	 */
	private List<String> getFeatureUnits() {

		Set<String> tokenSet = new HashSet<String>();

		for (Song song : corpus) {

			String[] tokens = song.getTokens();
			for (int i = 0; i < tokens.length; i++) {
				tokenSet.add(tokens[i].trim().toLowerCase());
			}
		}

		return new ArrayList<String>(tokenSet);
	}

	private Map<String, Integer> getDocFrequencies(/* List<Song> songs */) {

		Map<String, Integer> toReturn = new HashMap<String, Integer>();

		for (Song song : corpus) {
			String[] featureUnits = song.getTokens();
			for (int i = 0; i < featureUnits.length; i++) {
				String fu = featureUnits[i];
				int freq = 0;
				if (toReturn.containsKey(fu))
					freq = toReturn.get(fu);
				toReturn.put(fu, freq + 1);
			}
		}

		return toReturn;
	}

	/**
	 * searches for all songs in the given time section and computes the X most
	 * popular tokens in each time section
	 * @param sectionFrom inclusive
	 * @param sectionTo inclusive
	 * @param useCompilations songs of compilations are count in
	 * @param numOfTokens number of relevant Tokens to give back
	 * @param artists artists to count in
	 * @return
	 */
	public Map<String, Integer> getMostRelevantTokens(int sectionFrom, int sectionTo, boolean useCompilations,
			int numOfTokens, List<String> artists) {

		List<Song> songs = new ArrayList<Song>();
		double[] sum = new double[featureUnitOrder.size()];
		Map<String, Double> weightSum = new HashMap<String, Double>();

		for (Song song : corpus) {
			if (!useCompilations && song.isCompilation())
				continue;
			if (!artists.contains(song.getArtist()))
				continue;
			// gewichte für Zeitraum aufaddieren
			if (song.getYear() >= sectionFrom && song.getYear() <= sectionTo) {
				songs.add(song);
				double[] currentWeights = song.getWeights();
				for (int i = 0; i < sum.length; i++) {
					sum[i] += currentWeights[i];
				}
			}
		}

		// ...und arithmetisches Mittel für ersten Zeitraum bilden
		for (int i = 0; i < sum.length; i++) {
			Double norm = sum[i] / (double) songs.size();
			weightSum.put(featureUnitOrder.get(i), norm);
			// logger.info(norm + " - " + featureUnitOrder.get(i));
		}

		List<String> popularTokens = getXMostPopular(weightSum, numOfTokens);
		Map<String, Integer> tokenFreq = new HashMap<String, Integer>();
		// collect frequencies of most popular tokens
		for (Song song : songs) {
			Map<String, Integer> currentFreqs = song.getTermFreqs();
			for (String token : popularTokens) {
				Integer currF = 0;
				// if song contains popular token
				if (currentFreqs.containsKey(token)) {
					currF = currentFreqs.get(token);
					Integer freqSum = 0;
					if (tokenFreq.containsKey(token))
						freqSum = tokenFreq.get(token);
					tokenFreq.put(token, (freqSum + currF));
				}
			}
		}
		return tokenFreq;
	}

	private List<String> getXMostPopular(Map<String, Double> weights, int numOfSongs) {

		List<String> mostPopularTokens = new ArrayList<String>();

		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(weights.entrySet());
		Collections.sort(list, comp);

		for (int i = 0; i < numOfSongs; i++) {
			mostPopularTokens.add(list.get(i).getKey());
		}
		return mostPopularTokens;
	}

}
