package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Document;
import com.example.demo.repository.DocumentRepo;

@Service("docserv")
public class DocumentServImpl implements DocumentService {

	@Autowired
	DocumentRepo docrepo;
	
	@Override
	public Document saveDocument(Document docs) {
		// TODO Auto-generated method stub
		if(docs!=null)
		{
			return docrepo.save(docs);
		}
		else {
			return null;
		}
	}

	@Override
	public List<Document> getAllDocsList() {
		// TODO Auto-generated method stub
		return docrepo.findAll();
	}

	@Override
	public Document getDocById(String id) {
		// TODO Auto-generated method stub
		
		if(id!=null)
		{
			Long did = Long.valueOf(id);
			
			return docrepo.findById(did).get();
		}
		else {
			return null;
		}
	}

	@Override
	public int updateDocument(Document docs) {
		// TODO Auto-generated method stub
		return docrepo.updateDocument(docs.getDoc_name(), docs.getEmail(), docs.getIssued_date(), docs.getLast_renewed_date(), docs.getLicense_duration(), docs.getDoc_id());
	}

	@Override
	public int deleteDocumentById(String did) {
		// TODO Auto-generated method stub
		if(did!=null) 
		{
			Long docid = Long.valueOf(did);
			
			if(docrepo.findById(docid)!=null)
			{
				docrepo.deleteById(docid);
			}
			return 1;
		}
		else {
			return 0;
		}
	}



}
