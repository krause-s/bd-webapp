package de.uni_koeln.dh.lyra.util;

/**
 * contains method to generate (random) color
 * 
 *
 */
public class ColorGenerator {

	/**
	 * @return
	 * generates a random string value that represents a color
	 */
	public static String randomColor() {
		    String[] letters = "0123456789ABCDEF".split("");
		    String color = "#";
		    for (int i = 0; i < 6; i++ ) {
		    	int currValue = (int) Math.floor(Math.random() * 16);
		        color += letters[currValue];
		    }
		    return color;
		}
	
}
