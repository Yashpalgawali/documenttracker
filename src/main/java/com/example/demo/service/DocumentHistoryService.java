package com.example.demo.service;

import java.util.List;
import com.example.demo.models.DocumentHistory;

public interface DocumentHistoryService {

	public DocumentHistory saveDocumentHistory(DocumentHistory hist);
	
	public List<DocumentHistory> getDochistById(Long id);

}