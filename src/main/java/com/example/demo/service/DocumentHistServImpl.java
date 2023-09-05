package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.DocumentHistory;
import com.example.demo.repository.DocumentHistoryRepo;

@Service("dochistserv")
public class DocumentHistServImpl implements DocumentHistoryService {

	@Autowired
	DocumentHistoryRepo histrepo;
	
	@Override
	public DocumentHistory saveDocumentHistory(DocumentHistory hist) {
		// TODO Auto-generated method stub
		return histrepo.save(hist);
	}

	@Override
	public List<DocumentHistory> getDochistById(Long id) {
		// TODO Auto-generated method stub
		try {
			return histrepo.getDocHistoryByDocId(id);
		}
		catch(Exception e) {
			return null;
		}
	}
}