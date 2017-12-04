package de.uni_koeln.dh.bd.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import de.uni_koeln.dh.bd.data.Song;
import de.uni_koeln.dh.bd.util.IO;
import de.uni_koeln.dh.bd.util.OS;

@Service
public class CorpusService {
	
		private Logger logger = LoggerFactory.getLogger(getClass());
	
		private File file;
	
	public CorpusService() {
		// TODO project dependency
		this.file = new File(".." + OS.SEP + "preproc", "export.xml");
	}
	
	public List<Song> getSongs() {
		if (file.exists()) {
			logger.info("Deserializing XML...");
			
			try {
				/*
				 *  TODO deletion of invalid tags required: <lyrics> contains HTML
				 */
				String content = IO.getContent(file);
				content = content.replaceAll("<br />", "");
	
				ObjectMapper mapper = new XmlMapper();
				
				List<Song> songs = mapper.readValue(content, new TypeReference<List<Song>>(){});
				return songs;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 		

		return null;
	}
	
}
