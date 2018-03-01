package de.uni_koeln.dh.lyra.data.albums;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Albums {

		private List<Album> albums = new ArrayList<Album>();
	
	@XmlElement(name = "album")
	public List<Album> getList() {
		return albums;
	}
	
	public void setList(List<Album> albums) {
		this.albums = albums;
	}

}
