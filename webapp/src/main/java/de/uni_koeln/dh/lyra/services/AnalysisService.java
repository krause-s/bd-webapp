package de.uni_koeln.dh.lyra.services;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

	// TODO johanna
	public void doSth(List<String> artists, List<String> years, boolean compilation, String count) {
		System.out.println(artists + "\n" + years + "\n" + compilation + "\n" + count + "\n-----");
	} 
	
}
