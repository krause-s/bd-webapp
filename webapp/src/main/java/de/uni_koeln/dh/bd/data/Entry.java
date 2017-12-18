package de.uni_koeln.dh.bd.data;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Entry {

		private String id;
	
	@XmlAttribute	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	///////////
	
		private String value;
		
	@XmlValue	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "[" + id + "]\t" + value;
	}
	
}
