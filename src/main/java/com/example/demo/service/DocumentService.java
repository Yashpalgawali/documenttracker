package com.example.demo.service;

import java.util.List;

import com.example.demo.models.Document;

public interface DocumentService {

	public Document saveDocument(Document docs);
	
	public List<Document> getAllDocsList();
	
	public Document getDocById(String id);
	
	public int updateDocument(Document docs);
	
	public int deleteDocumentById(String did);
}
