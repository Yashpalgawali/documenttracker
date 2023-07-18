package com.example.demo;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.controller.DocumentController;

@SpringBootApplication
public class LatestDocumentTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LatestDocumentTrackerApplication.class, args);
		
		File updirectory = new File(DocumentController.uploadDirectory);
		if(!updirectory.isDirectory())
		{
			updirectory.mkdir();
		}
	}
	
	
}
