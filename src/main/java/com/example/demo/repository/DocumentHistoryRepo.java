package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.DocumentHistory;

@Repository("dochistrepo")
public interface DocumentHistoryRepo extends JpaRepository<DocumentHistory, Long> {

}
