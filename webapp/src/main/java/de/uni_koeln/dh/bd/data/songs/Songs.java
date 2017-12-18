package de.uni_koeln.dh.bd.data.songs;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Songs {

		private List<Song> songs = new ArrayList<Song>();
	
	@XmlElement(name = "song")
	public List<Song> getList() {
		return songs;
	}
	
	public void setList(List<Song> songs) {
		this.songs = songs;
	}
	
}
