package de.uni_koeln.dh.bd.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class IO {

	private static BufferedReader getReader(File file) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
		return new BufferedReader(isr);
	}
	
	public static String getContent(File file) throws IOException {
		BufferedReader bfr = getReader(file);

		StringBuilder sb = new StringBuilder();
		String curLine;
		
		while ((curLine = bfr.readLine()) != null) 
			sb.append(curLine);
	
		bfr.close();
		return sb.toString();
	}
	
}
