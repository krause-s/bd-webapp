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
	private Map<String, Integer> yearFrequency = new HashMap<String,Integer>();
	private Map<String, Integer> tokenFreq = new HashMap<String, Integer>();
	
	private List<String> featureUnitOrder;
	private Map<String, Integer> docFrequencies = new HashMap<String, Integer>();
	
	/**
	 * max term frequency of the current song
	 */
	private int maxTF;
	
	/**
	 * creates a list of all features in the corpus
	 * @param songs
	 * @return
	 */
	private List<String> getFeatureUnits(List<Song> songs) {
		
		Set<String> tokenSet = new HashSet<String>();
		
		for (Song song : songs) {

			String[] tokens = song.getTokens();
			for (int i = 0; i < tokens.length; i++) {
				tokenSet.add(tokens[i].trim().toLowerCase());
			}			
		}
		
		return new ArrayList<String>(tokenSet);
	}
	
	
	public List<Song> getWeights (List<Song> songs) {
		
		if(featureUnitOrder == null)
			featureUnitOrder = getFeatureUnits(songs);
		
		docFrequencies = getDocFrequencies(songs);
		for (Song song : songs) {
			Map<String, Integer> termFrequencies = getTermFrequencies(song.getTokens());
			double[] vector = new double[this.featureUnitOrder.size()];
			int i = 0;
			for (String featureUnit : this.featureUnitOrder) {
				double tfidf = 0;
				if (termFrequencies.containsKey(featureUnit)) {
					double ntf = ((double)(termFrequencies.get(featureUnit)) / maxTF);
					double idf =  (double)Math.log((double)(songs.size())/(docFrequencies.get(featureUnit)));
					tfidf = ntf * idf;
				}
				vector[i++] = tfidf;
			}

			song.setWeights(vector);
		}
		
		return songs;
	}
	
	/**
	 * counts term frequency of each given term. computes the frequency of the
	 * most frequently term (used to norm frequencies afterwards)
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


	private Map<String, Integer> getDocFrequencies(List<Song> songs) {
		
		Map<String, Integer> toReturn = new HashMap<String, Integer>();
		
		for (Song song : songs) {
			String[] featureUnits = song.getTokens();
			for (int i = 0; i < featureUnits.length; i++) {
				String fu = featureUnits[i];
				int freq = 0;
				if(toReturn.containsKey(fu))
					freq = toReturn.get(fu);
				toReturn.put(fu, freq + 1);
			}
		}
		
		return toReturn;
	}


//	public Map<Integer, Map<String, Double>> getWeightsByYear(List<Song> songs, boolean includeCompilations){
//		
//		if(featureUnitOrder == null)
//			featureUnitOrder = getFeatureUnits(songs);
//		logger.info(featureUnitOrder.size() + " unique Tokens");
//		
//		for (Song song : songs) {
//			if(!includeCompilations && song.isCompilation())  //if compilations aren't included and the song is a compilation
//					continue;
//			
//			String[] tokens = song.getTokens();
//			Integer year = song.getYear();
//			
//			if(tokens == null) {
//				//TODO tokenize lyrics
//			}
//			
//			Map<String, Integer> currentFreqsByYear = new HashMap<String, Integer>();
//			if(tokenFreqByYear.containsKey(year))
//				currentFreqsByYear = tokenFreqByYear.get(year);
//			
//			for (int i = 0; i < tokens.length; i++) {
//				String token = tokens[i].toLowerCase().trim();
//				
//				if(!featureUnitOrder.contains(token))
//					featureUnitOrder.add(token);
//				
//				Integer freqByYear = 0;
//				if (currentFreqsByYear.containsKey(token))
//					freqByYear = currentFreqsByYear.get(token);
//				currentFreqsByYear.put(token, freqByYear + 1);
//				
//				Integer totalFreq = 0;
//				if (tokenFreq.containsKey(token))
//					totalFreq = tokenFreq.get(token);
//				tokenFreq.put(token, totalFreq);
//					
//			}
//			
//			tokenFreqByYear.put(year, currentFreqsByYear);
//		}
//		
//		//TODO tfidf year vs all
//		
//		//build year frequency
//		for (Map.Entry<Integer, Map<String,Integer>> e : tokenFreqByYear.entrySet()) {
//			//proofs which tokens appear in the current year
//			for (String token : featureUnitOrder) {
//				
//				Integer currentFreq = yearFrequency.get(token);
//				if(currentFreq == null)
//					currentFreq = 0;
//				
//				if(e.getValue().containsKey(token))
//					yearFrequency.put(token, currentFreq + 1);
//				
//			}
//		}
//		
//		logger.info(tokenFreq.size() + " token freq");
//		
//		for (Map.Entry<String, Integer> e : yearFrequency.entrySet()) {
//			if (e.getValue() < 10)
//				logger.info(e.getKey() + " - " + e.getValue());
//		}
//		
//		return null;
//	}


	public List<Map.Entry<String, Double>> getWeightsForYear(List<Song> songs, int year) {
		
		Map<String,Double> toReturn = new HashMap<String,Double>();
		int songCount = 0;
		double[] sum = new double[featureUnitOrder.size()];
		
		// gewichte aufaddieren ...
		for (Song song : songs) {
			if(song.getYear() == year) {
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
//			logger.info(norm + " - " + featureUnitOrder.get(i));
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

}
