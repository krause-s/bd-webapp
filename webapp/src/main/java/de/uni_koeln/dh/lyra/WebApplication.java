package de.uni_koeln.dh.lyra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application to start Web App
 *
 *
 */
@SpringBootApplication
public class WebApplication {

	/**
	 * runs SpringApplication
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}
	
}
