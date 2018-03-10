package de.uni_koeln.dh.lyra.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	public String userID;

	@Autowired
	private SearchService searchService;

	public void init() {
		if (userID != null) {
			searchService.setIndexDirPath(userID);
			try {
				searchService.initIndex();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setUserID(String userID) {
		this.userID = userID;
		init();
	}
	
	public void generateNewID(){
		this.userID = UUID.randomUUID().toString();
	}

	public String getUserID() {
		return this.userID;
	}
	
	public boolean knownUser(){
		if(userID == null)
			return false;
		return true;
	}
	
	public boolean loggedIn(){
		if(userID == null){
			return false;
		}
		return true;
	}

}
