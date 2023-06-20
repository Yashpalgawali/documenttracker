package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String homePage()
	{
		LocalDate oneYearAfter =  LocalDate.now().plusYears(2) ;// now.plusYears(1);
		
		Date current = new Date();
		
		System.out.println("Date after 1 year is -> "+oneYearAfter);
		
		
		 LocalDate local = LocalDate.ofInstant(
		            current.toInstant(), ZoneId.systemDefault());
		  
		        // printing the local date object
		        System.out.println(local);
		
		return "Home";
	}
	
}