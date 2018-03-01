package de.uni_koeln.dh.lyra.data;


//@XmlType(propOrder = {"title", "artist", "year", "tracklist"})
public class Album {
	
		private String title;
		private int year;
	
	public Album(String title, int year) {
			this.title = title;
			this.year = year;
		}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}	
		
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	
}
