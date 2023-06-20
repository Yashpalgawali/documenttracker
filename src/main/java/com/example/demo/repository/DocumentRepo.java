package com.example.demo.repository;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Document;

@Repository("docrepo")
public interface DocumentRepo extends JpaRepository<Document, Long> {

	@Transactional
	@Modifying
	@Query(value="update tbl_document set doc_name=?1,email=?2,issued_date=?3,last_renewed_date=?4,license_duration=?5 where doc_id=?6",nativeQuery = true)
	public int updateDocument(String dname,String email,Date idate,Date ludate,int duration,Long did);
}
