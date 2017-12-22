package de.uni_koeln.dh.bd.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

	public static String getTitle(String info) {
//		System.out.println(info);
		String title = "";
		title = info.split("\\(")[0];
		title = title.replaceAll("<br>", "");
		title = title.trim();
//		System.out.println(title);
		return title;
	}

	public static int getYear(String info) {
//		System.out.println(info);
		Pattern pattern = Pattern.compile("\\(\\d{4}");
		
		Matcher match = pattern.matcher(info);
		if (match.find())
			return Integer.parseInt(info.substring(match.start() + 1, match.end()));
		else
			return 0;

	}
	
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
