package de.uni_koeln.dh.bd.data;

public class Song {
	private String lyrics;
	private String copyright;
	private String title;
	private String credits;
	private String url;
	private String firstPlayed;
	private String lastPlayed;
	private String timesPlayed;

	public Song(String url, String title, String lyrics, String copyright, String credits, String fp, String lp,
			String tp) {

		// title enthält kein html und kann übernommen werden
		this.title = title;
		this.url = url;
		lyrics = lyrics.replaceAll("<br>", "<br />");
		this.lyrics = lyrics.split("<p ")[0];
		this.copyright = copyright;
		firstPlayed = fp;
		lastPlayed = lp;
		timesPlayed = tp;

		this.credits = credits.replaceAll("Written by: ", "");
	}

	public String getFirstPlayed() {
		return firstPlayed;
	}

	public String getLastPlayed() {
		return lastPlayed;
	}

	public String getTimesPlayed() {
		return timesPlayed;
	}

	public String getLyrics() {
		return lyrics;
	}

	public String getCopyright() {
		return copyright;
	}

	public String getTitle() {
		return title;
	}

	public String getCredits() {
		return credits;
	}

	public String getURL() {
		return url;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title + "\n" + lyrics + "\n" + credits + copyright;
	}
}
