package de.uni_koeln.dh.lyra.data;

/**
 * represents an album (only used for crawling/data generator)
 * 
 * @author Johanna
 *
 */
public class Album {

	private String title;
	private int year;

	/**
	 * creates an ablum with the given title and the given year
	 * 
	 * @param title
	 * @param year
	 */
	public Album(String title, int year) {
		this.title = title;
		this.year = year;
	}

	/**
	 * 
	 * @return title of the album
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title title of the album
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return year of the album
	 */
	public int getYear() {
		return year;
	}

	/**
	 * 
	 * @param year year of the album
	 */
	public void setYear(int year) {
		this.year = year;
	}

}
