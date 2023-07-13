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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
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

	@Scheduled(cron =" 20,59 14 16 1,13,L * *  ")
	void someJob() throws InterruptedException, Exception
	{
		LocalDate today = LocalDate.now();
		DateTimeFormatter dformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		System.err.println("Todays date is --> "+today+"\n");
		
		
		List<Document> doclist  = docserv.getAllDocsList();
		for(int i=0;i<doclist.size();i++)
		{
			String last_renewed_date="",todays_date="";
			
			todays_date = dformat.format(today);
			System.err.println("Last Renewed date is -->> "+doclist.get(i).getLast_renewed_date());
//			Instant instant = doclist.get(i).getLast_renewed_date().toInstant();
//
//			LocalDateTime ldt = instant
//					  .atZone(ZoneId.of("CET"))
//					  .toLocalDateTime();
//			String formattedDate = ldt.format(dformat);
			
			java.util.Date tday = new SimpleDateFormat("yyyy-MM-dd").parse(todays_date.toString());
			long diff = tday.getTime()-doclist.get(i).getLast_renewed_date().getTime();
			
			System.err.println("Differnrece is =>> "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
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
