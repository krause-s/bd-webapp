package de.uni_koeln.dh.bd.processing.featureEngineering;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uni_koeln.dh.bd.data.songs.Song;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class FeatureEngineering {
	
	Map<String,Integer> docFreq = new HashMap<String, Integer>();
	TokenizerME tokenizer;

	public List<Song> createFeatures(List<Song> songs) throws IOException {
		
		
		InputStream modelIn = null;

		modelIn = new FileInputStream("classifiers/en-token.bin");
		TokenizerModel model = new TokenizerModel(modelIn);
		tokenizer = new TokenizerME(model);

		//get tokens of every song
		for (Song song : songs) {
			String lyrics = song.getLyrics().replaceAll("\n<br />", "\n");
			Map<String, Integer> songFreqs = new HashMap<String, Integer>();
			String tokens[] = tokenizer.tokenize(lyrics);
			for (int i = 0; i < tokens.length; i++) {
				Integer freq = 0;
				if(songFreqs.containsKey(tokens[i]))
					freq = songFreqs.get(tokens[i]);
				songFreqs.put(tokens[i], freq + 1);
				
			}
			for(Map.Entry<String, Integer> e : songFreqs.entrySet()) {
//				System.out.println(e.getKey() + " - " + e.getValue());
				Integer freq = 0;
				if(docFreq.containsKey(e.getKey()))
					freq = docFreq.get(e.getKey());
				docFreq.put(e.getKey(), freq + 1);
			}
			
		}
		for (Map.Entry<String, Integer> e : docFreq.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
		//get freqs of whole corpus
		
		
		
		return songs;
	}

}
