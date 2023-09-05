package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.DocumentHistory;

@Repository("dochistrepo")
public interface DocumentHistoryRepo extends JpaRepository<DocumentHistory, Long> {

	@Query("SELECT d FROM DocumentHistory d JOIN d.document WHERE d.document.doc_id=:id")
	public List<DocumentHistory> getDocHistoryByDocId(Long id);
}
