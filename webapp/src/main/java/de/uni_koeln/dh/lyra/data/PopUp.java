package de.uni_koeln.dh.lyra.data;

import java.io.Serializable;

public class PopUp implements Serializable{

	private static final long serialVersionUID = -158703385862088096L;

	
	/**
	 * place name from bio data or lyrics (Reference String)
	 */
	private String placeName;
	/**
	 * lyrics snippet or description of bio data
	 */
	private String content;
	/**
	 * if popUp stores lyrics snippet: referred song object
	 */
	private Song referredSong;
	
	/**
	 * used to generate lyrics quote
	 * @param placeName referred in the lyrics
	 * @param content lyrics snippet
	 * @param referredSong song that contains the lyrics
	 */
	public PopUp(String placeName, String content, Song referredSong) {
		this.placeName = placeName;
		this.content = content;
		this.referredSong = referredSong;
	}

	/**
	 * used to generate pop up for bio data
	 * @param placeName
	 * @param content
	 */
	public PopUp(String placeName, String content) {
		this.placeName = placeName;
		this.content = content;
	}
	
	public Song getReferredSong() {
		return referredSong;
	}

	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((placeName == null) ? 0 : placeName.hashCode());
		result = prime * result + ((referredSong == null) ? 0 : referredSong.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PopUp other = (PopUp) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (placeName == null) {
			if (other.placeName != null)
				return false;
		} else if (!placeName.equals(other.placeName))
			return false;
		if (referredSong == null) {
			if (other.referredSong != null)
				return false;
		} else if (!referredSong.equals(other.referredSong))
			return false;
		return true;
	}
	
}
