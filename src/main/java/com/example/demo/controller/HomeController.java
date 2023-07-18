package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class HomeController {

	@Autowired
	Environment env; 
	
	@GetMapping("/")
	public String homePage(HttpServletRequest request,HttpSession sess)
	{
		String base_url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		sess.setAttribute("base_url", base_url);
		sess.setAttribute("appname", env.getProperty("spring.application.name"));
		
		return "Home";
	}
	
}