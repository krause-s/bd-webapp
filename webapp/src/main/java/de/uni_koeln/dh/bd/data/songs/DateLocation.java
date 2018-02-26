package de.uni_koeln.dh.bd.data.songs;

import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;

// TODO convert URL
@Deprecated
public class DateLocation {

		private URL url;
	
	@XmlAttribute(name = "ref")
	public URL getURL() {
		return url;
	}
	
	public void setURL(URL url) {
		this.url = url;
	}
	
}
