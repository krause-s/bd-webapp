package de.uni_koeln.dh.lyra.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to process strings
 * @author Johanna
 *
 */
public class Strings {

	/**
	 * cuts the title of a song out of html 
	 * @param info
	 * @return title of a song
	 */
	public static String getTitle(String info) {
//		System.out.println(info);
		String title = "";
		title = info.split("\\(")[0];
		title = title.replaceAll("<br>", "");
		title = title.trim();
//		System.out.println(title);
		return title;
	}

	/**
	 * cuts the year of a song out of html
	 * @param info
	 * @return year of a song
	 */
	public static int getYear(String info) {
//		System.out.println(info);
		Pattern pattern = Pattern.compile("\\(\\d{4}");
		
		Matcher match = pattern.matcher(info);
		if (match.find())
			return Integer.parseInt(info.substring(match.start() + 1, match.end()));
		else
			return 0;

	}
	
	/**
	 * searches for a hyperlink to a concert detail page
	 * @param html
	 * @return url
	 */
	public static String findConcertLink(String html) {

		String regex = "(http:\\/\\/www\\.[a-z\\d-]+\\.com\\/date\\/[a-z\\d%-]+\\/)";
		Pattern pattern = Pattern.compile(regex);

		Matcher match = pattern.matcher(html);
		if (match.find())
			return html.substring(match.start(), match.end());
		else
			return "";
	}

}
