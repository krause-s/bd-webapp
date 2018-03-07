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

	private Map<Integer, Map<String, Integer>> tokenFreqByYear = new HashMap<Integer, Map<String, Integer>>();
	/**
	 * counts in how many years a token appears
	 */
	private Map<String, Integer> yearFrequency = new HashMap<String, Integer>();
	private Map<String, Integer> tokenFreq = new HashMap<String, Integer>();

	private List<String> featureUnitOrder;
	private Map<String, Integer> docFrequencies = new HashMap<String, Integer>();
	private List<Song> corpus = new ArrayList<Song>();

	/**
	 * max term frequency of the current song
	 */
	private int maxTF;

	/**
	 * creates a list of all features in the corpus
	 * 
	 * @param corpus
	 * @return
	 */
	private List<String> getFeatureUnits(/*List<Song> corpus*/) {

		Set<String> tokenSet = new HashSet<String>();

		for (Song song : corpus) {

			String[] tokens = song.getTokens();
			for (int i = 0; i < tokens.length; i++) {
				tokenSet.add(tokens[i].trim().toLowerCase());
			}
		}

		return new ArrayList<String>(tokenSet);
	}

	public List<Song> getWeights(List<Song> songs) {
		this.corpus = songs;

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
	 * searches for all songs in the given time sections and computes the
	 * 10 most popular tokens in each time section
	 * @param section1from first section from
	 * @param section1to first section to
	 * @param section2from second section from
	 * @param section2to second section to
	 * @return Map: key=time section, value=popular token and absolute
	 * frequency in the time section
	 */
	public Map<Integer[], Map<String, Integer>> getMostRelevantTokens(int section1from, int section1to,
			int section2from, int section2to) {
		
		Map<Integer[], Map<String, Integer>> toReturn = new HashMap<Integer[], Map<String, Integer>>();

		Integer[] section1 = new Integer[] { section1from, section1to };
		int songCount1 = 0;
		List<Song> songs1 = new ArrayList<Song>();
		double[] sum1 = new double[featureUnitOrder.size()];
		Map<String, Double> weightSum1 = new HashMap<String, Double>();

		List<Integer> years1 = new ArrayList<Integer>();
		for (int i = section1[0]; i < section1[1]; i++) {
			years1.add(i);
		}

		Integer[] section2 = new Integer[] { section2from, section2to };
		int songCount2 = 0;
		List<Song> songs2 = new ArrayList<Song>();
		double[] sum2 = new double[featureUnitOrder.size()];
		Map<String, Double> weightSum2 = new HashMap<String, Double>();

		List<Integer> years2 = new ArrayList<Integer>();
		for (int i = section2[0]; i < section2[1]; i++) {
			years2.add(i);
		}

		for (Song song : corpus) {
			
			// gewichte für ersten Zeitraum aufaddieren
			if (years1.contains(song.getYear())) {
				songs1.add(song);
				songCount1++;
				double[] currentWeights = song.getWeights();
				for (int i = 0; i < sum1.length; i++) {
					sum1[i] += currentWeights[i];
				}
			}

			// gewichte für zweiten Zeitraum aufaddieren
			if (years2.contains(song.getYear())) {
				songs2.add(song);
				songCount2++;
				double[] currentWeights = song.getWeights();
				for (int i = 0; i < sum2.length; i++) {
					sum2[i] += currentWeights[i];
				}
			}
		}

		// ...und arithmetisches Mittel für ersten Zeitraum bilden
		for (int i = 0; i < sum1.length; i++) {
			Double norm = sum1[i] / (double) songCount1;
			weightSum1.put(featureUnitOrder.get(i), norm);
			// logger.info(norm + " - " + featureUnitOrder.get(i));
		}

		// ...und arithmetisches Mittel für zweiten Zeitraum bilden
		for (int i = 0; i < sum2.length; i++) {
			Double norm = sum2[i] / (double) songCount2;
			weightSum2.put(featureUnitOrder.get(i), norm);
			// logger.info(norm + " - " + featureUnitOrder.get(i));
		}
		
		List<String> popularTokens1 = get10MostPopular(weightSum1);
		Map<String, Integer> tokenFreq1 = new HashMap<String, Integer>();
		//collect frequencies of most popular tokens
		for (Song song : songs1) {
			Map<String, Integer> currentFreqs = song.getTermFreqs();
			for (String token : popularTokens1) {
				Integer currF = 0;
				//if song contains popular token
				if(currentFreqs.containsKey(token)) {
					currF = currentFreqs.get(token);
					Integer freqSum = 0;
					if(tokenFreq1.containsKey(token))
						freqSum = tokenFreq1.get(token);					
					tokenFreq1.put(token, (freqSum + currF));
				}					
			}
		}
		
		toReturn.put(section1, tokenFreq1);
		
		
		List<String> popularTokens2 = get10MostPopular(weightSum2);
		Map<String, Integer> tokenFreq2 = new HashMap<String, Integer>();
		//collect frequencies of most popular tokens
		for (Song song : songs2) {
			Map<String, Integer> currentFreqs = song.getTermFreqs();
			for (String token : popularTokens2) {
				Integer currF = 0;
				//if song contains popular token
				if(currentFreqs.containsKey(token)) {
					currF = currentFreqs.get(token);
					Integer freqSum = 0;
					if(tokenFreq2.containsKey(token))
						freqSum = tokenFreq2.get(token);					
					tokenFreq2.put(token, (freqSum + currF));
				}					
			}
		}
		
		toReturn.put(section2, tokenFreq2);
		
		return toReturn;
	}
	
	private List<String> get10MostPopular(Map<String, Double> weights) {
		
		List<String> mostPopularTokens = new ArrayList<String>();
		
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(weights.entrySet());
		Collections.sort(list, comp);
		
		for (int i = 0; i < 10; i++) {
			mostPopularTokens.add(list.get(i).getKey());
		}
		return mostPopularTokens;
	}

	public List<Map.Entry<String, Double>> getWeightsForYear(List<Song> songs, int year) {

		Map<String, Double> toReturn = new HashMap<String, Double>();
		int songCount = 0;
		double[] sum = new double[featureUnitOrder.size()];

		// gewichte aufaddieren ...
		for (Song song : songs) {
			if (song.getYear() == year) {
				songCount++;
				double[] currentWeights = song.getWeights();
				for (int i = 0; i < sum.length; i++) {
					sum[i] += currentWeights[i];
				}
			}
		}

		// ...und arithmetisches Mittel bilden
		for (int i = 0; i < sum.length; i++) {
			Double norm = sum[i] / (double) songCount;
			toReturn.put(featureUnitOrder.get(i), norm);
			// logger.info(norm + " - " + featureUnitOrder.get(i));
		}

		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(toReturn.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
				// TODO Auto-generated method stub
				return e2.getValue().compareTo(e1.getValue());
			}
		});

		return list;
	}

	private Comparator<Map.Entry<String, Double>> comp = new Comparator<Map.Entry<String, Double>>() {

		@Override
		public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
			return e2.getValue().compareTo(e1.getValue());
		}
	};

}
