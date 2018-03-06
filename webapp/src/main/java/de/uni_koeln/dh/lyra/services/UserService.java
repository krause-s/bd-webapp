package de.uni_koeln.dh.lyra.services;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	// TODO: should be set as "login"
	// public String userID;
	public String userID = "00001/";

	@Autowired
	private SearchService searchService;

	@PostConstruct
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
	}

	public String getUserID() {
		return this.userID;
	}

}
