package com.example.demo.controller;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	@Autowired
	DocumentHistoryService histserv;
	
	@GetMapping("/adddocument")
	public String addDocument() {
		return "AddDocument";
	}
	
	
	@RequestMapping("/savedocument")
	public String saveDocument(@ModelAttribute("Document")Document docs, RedirectAttributes attr) {
		Document document = docserv.saveDocument(docs);
		if(document!=null) {	
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
	public String viewDocuments() {
		return "ViewDocuments";
	}
	
	@GetMapping("/viewalldocuments")
	@ResponseBody
	public List<Document>  viewAllDocuments() {
		List<Document> dlist = docserv.getAllDocsList();
		return dlist;
	}
	
	@RequestMapping("/getdocbyid{id}")
	public String getDocById(@RequestParam String id,Model model, RedirectAttributes attr) {
		Document document = docserv.getDocById(id);
		if(document!=null) {
			model.addAttribute("docs", document);
			return "EditDocument";
		}
		else {
			attr.addFlashAttribute("reserr", "No document found for given Id");
			return "redirect:/viewdocuments";
		}
	}
	
	@RequestMapping("/updatedocument")
	public String updateDocument(@ModelAttribute("Document")Document docs,RedirectAttributes attr) {
		int res = docserv.updateDocument(docs);
		if(res > 0) {
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
	public String uploadDocuments() {
		return "UploadDocument";
	}
	
	
	@RequestMapping("/upload")@ResponseBody
	public String uploadFiles(@RequestParam("files") MultipartFile[] files,RedirectAttributes attr) {
		StringBuilder filenames= new StringBuilder();
		
		for(MultipartFile fileone : files) {
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
	public String  downloadDocuments(@RequestParam("id") String id,RedirectAttributes attr) {
		return "The id is ->>> "+id;
	}
	
	@Autowired
	EmailService mailserv;

	@Scheduled(cron ="2,20 18 14 5,18,L * *  ")
	void someJob() throws InterruptedException, Exception 
	{
		LocalDate today = LocalDate.now();
		LocalDate l_year;
		DateTimeFormatter dformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		List<Document> doclist  = docserv.getAllDocsList();
		for(int i=0;i<doclist.size();i++)
		{
			l_year = doclist.get(i).getLast_renewed_date().toLocalDate();
			l_year = l_year.plusYears(doclist.get(i).getLicense_duration());
			
			String n_year = dformat.format(l_year),t_year = dformat.format(today);
			
			Date last_renew_date =  format.parse(n_year);
			Date current_date =  format.parse(t_year);
			
			// Calculate time difference in milliseconds   
            long time_difference = last_renew_date.getTime() - current_date.getTime() ;  
			
		     // Calculate time difference in days using TimeUnit class  
            long days_difference = TimeUnit.MILLISECONDS.toDays(time_difference) % 365;  
            
            if(days_difference<=30 && days_difference>=1) {
            	mailserv.sendSimpleMail(doclist.get(i).getEmail(), "Respected Sir , \n \t the document "+doclist.get(i).getDoc_name()+" will expire in "+days_difference+" days. On "+last_renew_date , "Document "+doclist.get(i).getDoc_name()+" expiry");
            }
            if(days_difference>=(-30) && days_difference<=(-1)) {
            	mailserv.sendSimpleMail(doclist.get(i).getEmail(), "Respected Sir , \n \t the document "+doclist.get(i).getDoc_name()+" is expired On "+l_year, "Document "+doclist.get(i).getDoc_name()+" expired");
            }
		}
	}
	
	@RequestMapping("/deldocbyid/{id}")@ResponseBody
	public String deldocbyid(@PathVariable("id")String id) {
		int res = docserv.deleteDocumentById(id);
		if(res!=0) {
			System.err.println("Document is deleted successfully");
			return "success";
		}
		else {
			System.err.println("Document is NOT deleted ");
			return "failed";
		}
	}
	
	
	@GetMapping("getdochistbyid/{id}")
	public String getDocHistoryById(@PathVariable("id") Long id,Model model,RedirectAttributes attr)
	{
		List<DocumentHistory> dochist = histserv.getDochistById(id);
		model.addAttribute("dochist", dochist);
		return "ViewDocumentHistory";
	}
	
	@EnableScheduling
	class SchedulingConfiguration implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
}
