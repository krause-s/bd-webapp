package de.uni_koeln.dh.bd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Deprecated
public class Tokenizer {
	
	public List<String> stopwords = new ArrayList<String>();
	private static TokenizerME tokenizer;
	
	public Tokenizer() {
		File stopwordsFile = new File("src/main/resources/stopwords.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(stopwordsFile));
			String line = "";
			while ( (line = br.readLine()) != null) {
				stopwords.add(line.trim().toLowerCase());
			}
			br.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> tokenize(String lyrics) throws IOException{
		//TODO stopword filter
		//TODO delete .," .....
		InputStream modelIn = null;

		modelIn = new FileInputStream("NOclassifiers/en-token.bin");
		TokenizerModel model = new TokenizerModel(modelIn);
		tokenizer = new TokenizerME(model);
		
		String[] tokenArray = tokenizer.tokenize(lyrics);
		List<String> tokenList = new ArrayList<String>();
		
		for (int i = 0; i < tokenArray.length; i++) {
			String token = tokenArray[i].toLowerCase();
			token = token.replaceAll("[,.'’\"”-]", "");
//			System.out.println(token);
			if(!token.equals(""))
				tokenList.add(token);
		}
		tokenList.removeAll(stopwords);
		
		return tokenList;
	}

}
