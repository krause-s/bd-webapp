package de.uni_koeln.dh.bd.processing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_koeln.dh.bd.data.songs.Song;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class NER {
	
	// TODO logger in preproc 
	//private Logger logger = LoggerFactory.getLogger(getClass());

	private TokenizerME tokenizer;
	private Set<String> places = new HashSet<String>(); //TODO Map (value: geokoordinaten)
	
	public Set<String> getPlaces(){
		return places;
	}

	public List<Song> detectNamedEntities(List<Song> songs) throws IOException {
		System.out.println("detect named entities");
		InputStream modelIn = null;

		modelIn = new FileInputStream("classifiers/en-token.bin");
		TokenizerModel model = new TokenizerModel(modelIn);
		tokenizer = new TokenizerME(model);

		for (Song song : songs) {
			String lyrics = song.getLyrics().replaceAll("\n<br />", "\n");
			String annotated = annotatePlaces(lyrics);
//			System.out.println(annotated);
			song.setAnnotated(annotated);
		}

		modelIn.close();

		System.out.println(places);
		
		return songs;
	}
	
	private String annotatePlaces(String lyrics) throws IOException {
		
		if (lyrics.equals(""))
			return lyrics;
//		System.out.println("-----------------------------");
//		System.out.println(lyrics);
		String tokens[] = null;
		tokens = tokenizer.tokenize(lyrics);
//		System.out.println(tokens.length + " Tokens");

		InputStream modelIn1 = new FileInputStream("classifiers/en-ner-location.bin");
		TokenNameFinderModel NFmodel = new TokenNameFinderModel(modelIn1);
		NameFinderME nameFinder = new NameFinderME(NFmodel);

		Span[] nameSpans = nameFinder.find(tokens);
		// TODO Schwellwert NER

		for (Span s : nameSpans) {
//			System.out.println(s.getStart() + " " + s.getEnd());
			String toReplace = "";
			for (int index = s.getStart(); index < s.getEnd(); index++) {
				toReplace += (tokens[index] + " ");
			}
			toReplace = toReplace.trim();
			places.add(toReplace.toLowerCase());
//			System.out.println(toReplace);
			String replacement = "<place>" + toReplace + "</place>";
			lyrics = lyrics.replaceAll(toReplace, replacement);
		}
		
		return lyrics;
	}



}
