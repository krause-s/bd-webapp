package de.uni_koeln.dh.bd.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.uni_koeln.dh.bd.data.albums.Album;
import de.uni_koeln.dh.bd.data.albums.Albums;
import de.uni_koeln.dh.bd.data.songs.Song;
import de.uni_koeln.dh.bd.data.songs.Songs;
import de.uni_koeln.dh.bd.util.IO;
import de.uni_koeln.dh.bd.util.OS;

@Service
public class CorpusService {
	
		private Logger logger = LoggerFactory.getLogger(getClass());
		
		private File songFile,
			albumFile;
	
	public CorpusService() {
		// TODO project dependency
		File preprocPRJ = new File(".." + OS.SEP + "preproc");
		
		this.songFile = new File(preprocPRJ, "songs.xml");
		this.albumFile = new File(preprocPRJ, "albums.xml");
	}
	
	public List<Song> getSongs() {
		if (songFile.exists()) {
			try {
				/*
				 *  TODO deletion of invalid tags required: <lyrics> contains HTML
				 */
				String content = IO.getContent(songFile);
				content = content.replaceAll("<br />", "");
				StringReader strReader = new StringReader(content);
				
				logger.info("Unmarshalling '" + songFile.getName() +  "'...");

				JAXBContext context = JAXBContext.newInstance(Songs.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				
				Songs songs = (Songs) unmarshaller.unmarshal(strReader);
				return songs.getList();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		} 		

		return null;
	}
	
	public List<Album> getAlbums() {
		if (albumFile.exists()) {
			try {
				logger.info("Unmarshalling '" + albumFile.getName() +  "'...");
				
				JAXBContext context = JAXBContext.newInstance(Albums.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				
				Albums albums = (Albums) unmarshaller.unmarshal(albumFile);
				return albums.getList();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	// TODO move?
	public char[] getAlphabet() {
		return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	}
	
}
