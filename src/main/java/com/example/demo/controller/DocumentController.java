package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.Document;
import com.example.demo.models.DocumentHistory;
import com.example.demo.service.DocumentHistoryService;
import com.example.demo.service.DocumentService;
import com.example.demo.service.EmailService;

@Controller
public class DocumentController {

	// System.getProperty("user.dir") will return the path of the project folder
	public static String uploadDirectory = System.getProperty("user.dir")+"\\uploads";

	public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("en", "EN"));
	
	Date today = new Date();
	
	@Autowired
	DocumentService docserv;
	
	@GetMapping("/adddocument")
	public String addDocument()
	{	return "AddDocument";
	}
	
	@Autowired
	DocumentHistoryService histserv;
	
	@RequestMapping("/savedocument")
	public String saveDocument(@ModelAttribute("Document")Document docs, RedirectAttributes attr)
	{
		Document document = docserv.saveDocument(docs);
		
		if(document!=null)
		{	
			DocumentHistory dhist = new DocumentHistory();
			
			String tday = simpleDateFormat.format(today);
			
			dhist.setDocument(document);
			dhist.setOp_date(tday);
			dhist.setOperation("Document "+document.getDoc_name()+" is saved successfully");
			
			histserv.saveDocumentHistory(dhist);
			
			attr.addFlashAttribute("response", "Document is saved successfully..");
			return "redirect:/viewdocuments";
		}
		else {
			attr.addFlashAttribute("reserr", "Document is not saved ");
			return "redirect:/viewdocuments";
		}
	}
	
	
	@GetMapping("/viewdocuments")
	public String viewDocuments()
	{
		return "ViewDocuments";
	}
	
	@GetMapping("/viewalldocuments")
	@ResponseBody
	public List<Document>  viewAllDocuments()
	{
		List<Document> dlist = docserv.getAllDocsList();
		return dlist;
	}
	
	@RequestMapping("/getdocbyid{id}")
	public String getDocById(@RequestParam String id,Model model, RedirectAttributes attr)
	{
		Document document = docserv.getDocById(id);
		
		if(document!=null){
			model.addAttribute("docs", document);
			return "EditDocument";
		}
		else{
			attr.addFlashAttribute("reserr", "No document found for given Id");
			return "redirect:/viewdocuments";
		}
	}
	
	@RequestMapping("/updatedocument")
	public String updateDocument(@ModelAttribute("Document")Document docs,RedirectAttributes attr)
	{
		int res = docserv.updateDocument(docs);
		if(res > 0)
		{
			Document document = docserv.getDocById(docs.getDoc_id().toString());
			
			DocumentHistory dhist = new DocumentHistory();
			
			String tday = simpleDateFormat.format(today);
			
			dhist.setDocument(document);
			dhist.setOp_date(tday);
			dhist.setOperation("Document "+document.getDoc_name()+" is Updated successfully");
			
			histserv.saveDocumentHistory(dhist);
			
			attr.addFlashAttribute("response", "Document is updated successfully");
			return "redirect:/viewdocuments";
		}
		else {
			
				Document document = docserv.getDocById(docs.getDoc_id().toString());
				
				DocumentHistory dhist = new DocumentHistory();
				
				String tday = simpleDateFormat.format(today);
				
				dhist.setDocument(document);
				dhist.setOp_date(tday);
				dhist.setOperation("Document "+document.getDoc_name()+" is Updated successfully");
				
				histserv.saveDocumentHistory(dhist);
				
				attr.addFlashAttribute("reserr", "Document is not updated");
				return "redirect:/viewdocuments";
		}
	}
	
	@RequestMapping("/uploaddocuments")
	public String uploadDocuments()
	{
		return "UploadDocument";
	}
	
	
	@RequestMapping("/upload")@ResponseBody
	public String uploadFiles(@RequestParam("files") MultipartFile[] files,RedirectAttributes attr)
	{
		StringBuilder filenames= new StringBuilder();
		
		for(MultipartFile fileone : files)
		{
			Path filenamesandpath = Paths.get(uploadDirectory ,fileone.getOriginalFilename());
			filenames.append(fileone.getOriginalFilename());
			
			try {
				Files.write(filenamesandpath, fileone.getBytes());
				
			}catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}	
		attr.addFlashAttribute("response", "Document(s) uploaded successfully...");
		return "redirect:/viewdocuments";
	}
	
	
//	@RequestMapping("/getdocbyid{id}")
//	@ResponseBody
//	public String getDocument(@RequestParam("id") String id)
//	{
//		return "Using Request Param "+id;
//	}
	
	
	@RequestMapping("/fileexists{id}")
	@ResponseBody
	public String  downloadDocuments(@RequestParam("id") String id,RedirectAttributes attr)
	{
		return "The id is ->>> "+id;
	}
	
	@Autowired
	EmailService mailserv;

	
	
	@Scheduled(cron =" 20,59 44 14 1,10,L * *  ")
	void someJob() throws InterruptedException, Exception
	{
		List<Document> doclist  = docserv.getAllDocsList();
		
		String stdate		=	null;
		String today_date 	= 	null;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("en", "EN"));
		
		Date last_found_date ,  todays_date_final ;
		
		today_date = simpleDateFormat.format(new Date());
		
		long difference_In_Time , difference_In_Days;
		
		for(int i=0;i<doclist.size();i++)
		{
			  Document doc = doclist.get(i);
			  last_found_date = doc.getLast_renewed_date();
			 // last_found_date =	simpleDateFormat.parse(last_found_date);
			
			  System.err.println("Last renewed date ->> "+last_found_date+"\n Todays date is ->> "+today_date);
			  
			  LocalDate local = LocalDate.ofInstant(last_found_date.toInstant(), ZoneId.systemDefault());
			  
		  
			        // printing the local date object
			    	local = local.plusYears(doc.getLicense_duration());
			    	
			    	stdate = simpleDateFormat.format(local);
			  
			    	// 	This will give the formatted date in the format " Fri Feb 25 14:48:21 IST 2022 "
			        //	last_found_date 	=	simpleDateFormat.parse(stdate);
			    	
			    	last_found_date		=	simpleDateFormat.parse(stdate);
			    	todays_date_final	=	simpleDateFormat.parse(today_date);
			        difference_In_Time 	=	last_found_date.getTime() - todays_date_final.getTime();
			         
			        difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
			        
			        if(difference_In_Days >=-30 && difference_In_Days <=1) 
			        {
				        //mailserv.sendSimpleMail(stdate, datenew, today_date);
				        mailserv.sendSimpleMail(doc.getEmail(), "Hello, \nThis is the reminder email."+"\n"+"Regarding the renewal of "+doc.getDoc_name().toUpperCase() +" which will expire in "+difference_In_Days+" days", "License renewal of "+doc.getDoc_name().toUpperCase());
			        	//break;
		            } 
			        
			    System.err.println("\n "+doc.getDoc_name() +"-->> Difference in Days is ->> "+difference_In_Days);	   
		}
	}
	
	@RequestMapping("/deldocbyid/{id}")@ResponseBody
	public String deldocbyid(@PathVariable("id")String id)
	{
		int res = docserv.deleteDocumentById(id);
		if(res!=0)
		{
			
			System.err.println("Document is deleted successfully");
			return "success";
		}
		else {
			System.err.println("Document is NOT deleted ");
			return "failed";
		}
		
	}
	
	
	@EnableScheduling
	class SchedulingConfiguration implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
}
