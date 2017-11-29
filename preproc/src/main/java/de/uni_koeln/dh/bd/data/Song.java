package de.uni_koeln.dh.bd.data;

public class Song {
	private String lyrics;
	private String copyright;
	private String title;
	private String credits;
	private String url;
	
	public Song(String url, String title, String lyrics, String copyright, String credits){

		//title enthält kein html und kann übernommen werden
				this.title = title;
				this.url = url;
				lyrics = lyrics.replaceAll("<br>", "<br />");
				this.lyrics = lyrics.split("<p ")[0];
				this.copyright = copyright;
				
				this.credits = credits.replaceAll("Written by: ", "");
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
	
	public String getURL(){
		return url;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title + "\n" + lyrics + "\n"	+ credits + copyright;
	}
}
