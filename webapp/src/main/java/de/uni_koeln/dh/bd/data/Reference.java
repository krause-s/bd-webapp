package de.uni_koeln.dh.bd.data;

import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;

public class Reference {
	
		private String id;
		private URL url;
	
	@XmlAttribute	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlAttribute(name = "ref")
	public URL getURL() {
		return url;
	}
	
	public void setURL(URL url) {
		this.url = url;
	}
		
}
