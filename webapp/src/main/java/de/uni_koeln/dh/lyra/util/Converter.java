package de.uni_koeln.dh.lyra.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Converter {

	public static LinkedHashMap<String, Integer> sortMapByValue(HashMap<String, Integer> unsortedMap) {
		return unsortedMap.entrySet().stream()
				.sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (e1, e2) -> e1, LinkedHashMap::new));
	}
	
}
