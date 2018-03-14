package de.uni_koeln.dh.lyra.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.uni_koeln.dh.lyra.data.Artist;
import de.uni_koeln.dh.lyra.processing.LyricsAnalyzer;

@Service
public class AnalysisService {
	
	private LyricsAnalyzer analyzer;
	
	@Autowired
	private CorpusService corpusService;
	
	

	/**
	 * computes the frequencies of the most relevant tokens in the chosen time sequence and artist
	 * @param artists contains two values, for each result one artist name ("(All)" for all artists
	 * @param years contains two values, time frequence is seperated by comma
	 * @param compilation true if compilation songs shall count in
	 * @param count number of tokens to give back for each
	 * @return list contains two maps (for each result one) key=absolute frequency, value=tokens with freq
	 */
	public List<Map<Integer, Set<String>>> getFrequencies(List<String> artists, List<String> years, boolean compilation, String count) {

//		System.out.println(artists + "\n" + years + "\n" + compilation + "\n" + count + "\n-----");	
		analyzer = new LyricsAnalyzer(corpusService.getAllSongs());
		analyzer.getWeights();
		
		String[] firstYears = years.get(0).split(",");
		String[] secondYears = years.get(1).split(",");
		
		int firstFrom = Integer.parseInt(firstYears[0]);
		int firstTo = Integer.parseInt(firstYears[1]);
		int secondFrom = Integer.parseInt(secondYears[0]);
		int secondTo = Integer.parseInt(secondYears[1]);
		
		int numOfTokens = Integer.parseInt(count);	
		
		List<Map<Integer, Set<String>>> result = new ArrayList<Map<Integer, Set<String>>>();
				
		List<String> firstArtists = createRequestArtistsList(artists.get(0));	
		Map<Integer, Set<String>> firstResult = analyzer.getMostRelevantTokens(firstFrom, firstTo,
				compilation, numOfTokens, firstArtists);
		result.add(firstResult);
				
		List<String> secondArtists = createRequestArtistsList(artists.get(1));
		Map<Integer, Set<String>> secondResult = analyzer.getMostRelevantTokens(secondFrom, secondTo,
				compilation, numOfTokens, secondArtists);
		result.add(secondResult);
		
		return result;
	}



	private List<String> createRequestArtistsList(String artists) {
		List<String> toReturn = new ArrayList<String>();
		if (artists.equals("(All)")) {
			List<Artist> allArtists = corpusService.getArtistList();
			for(Artist a : allArtists) {
				toReturn.add(a.getName());
			}
		} else
			toReturn.add(artists);
		return toReturn;
	} 
	
}
