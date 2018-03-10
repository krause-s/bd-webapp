package de.uni_koeln.dh.lyra.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.processing.LyricsAnalyzer;

@Service
public class AnalysisService {
	
	private LyricsAnalyzer analyzer;
	
	@Autowired
	private CorpusService corpusService;
	
	

	// TODO johanna
	public List<Map<String, Integer>> doSth(List<String> artists, List<String> years, boolean compilation, String count) {
		System.out.println(artists + "\n" + years + "\n" + compilation + "\n" + count + "\n-----");	
		analyzer = new LyricsAnalyzer(corpusService.getAllSongs());
		analyzer.getWeights();
		
		String[] firstYears = years.get(0).split(",");
		String[] secondYears = years.get(1).split(",");
		
		int firstFrom = Integer.parseInt(firstYears[0]);
		int firstTo = Integer.parseInt(firstYears[1]);
		int secondFrom = Integer.parseInt(secondYears[0]);
		int secondTo = Integer.parseInt(secondYears[1]);
		
		int numOfTokens = Integer.parseInt(count);
		
		List<String> firstArtists = new ArrayList<String>();
		String first = artists.get(0);
		if (first.equals("(All)")) {
			List<Artist> allArtists = corpusService.getArtistList();
			for(Artist a : allArtists) {
				firstArtists.add(a.getName());
			}
		} else
			firstArtists.add(first);
		
		List<String> secondArtists = new ArrayList<String>();
		String second = artists.get(1);
		if (second.equals("(All)")) {
			List<Artist> allArtists = corpusService.getArtistList();
			for(Artist a : allArtists) {
				secondArtists.add(a.getName());
			}
		} else
			secondArtists.add(second);
		
		Map<String, Integer> firstResult = analyzer.getMostRelevantTokens(firstFrom, firstTo,
				compilation, numOfTokens, firstArtists);
		
		Map<String, Integer> secondResult = analyzer.getMostRelevantTokens(secondFrom, secondTo,
				compilation, numOfTokens, secondArtists);
			
		System.out.println(firstFrom + " - " + firstTo + " (" + firstArtists + ")");
		for (Map.Entry<String, Integer> t : firstResult.entrySet()) {
			System.out.println(t.getKey() + ": " + t.getValue());
		}
		System.out.println("----------------------------");
		
		System.out.println(secondFrom + " - " + secondTo + " (" + secondArtists + ")");
		for (Map.Entry<String, Integer> t : secondResult.entrySet()) {
			System.out.println(t.getKey() + ": " + t.getValue());
		}
		System.out.println("----------------------------");
		
		List<Map<String, Integer>> result = new ArrayList<Map<String, Integer>>();
		result.add(firstResult);
		result.add(secondResult);
		
		return result;
	} 
	
}
