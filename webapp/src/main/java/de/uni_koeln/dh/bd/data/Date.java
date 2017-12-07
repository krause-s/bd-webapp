package de.uni_koeln.dh.bd.data;

import java.net.URL;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

// TODO convert URL to date
public class Date {

		@JacksonXmlProperty(isAttribute = true, localName = "ref")
		private URL url;
	
	protected URL getURL() {
		return url;
	}
	
}
