package com.example.demo.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="tbl_document")
public class Document {
	
	@Id
	@SequenceGenerator(name="doc_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(strategy = GenerationType.AUTO,generator = "doc_seq")
	private Long doc_id;
	
	private String doc_name;
	
	private String email;
	
	private Date issued_date;
	
	private Date last_renewed_date;
	
	private int license_duration;

	public Long getDoc_id() {
		return doc_id;
	}

	public void setDoc_id(Long doc_id) {
		this.doc_id = doc_id;
	}

	public String getDoc_name() {
		return doc_name;
	}

	public void setDoc_name(String doc_name) {
		this.doc_name = doc_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getIssued_date() {
		return issued_date;
	}

	public void setIssued_date(Date issued_date) {
		this.issued_date = issued_date;
	}

	public Date getLast_renewed_date() {
		return last_renewed_date;
	}

	public void setLast_renewed_date(Date last_renewed_date) {
		this.last_renewed_date = last_renewed_date;
	}

	public int getLicense_duration() {
		return license_duration;
	}

	public void setLicense_duration(int license_duration) {
		this.license_duration = license_duration;
	}

	public Document(Long doc_id, String doc_name, String email, Date issued_date, Date last_renewed_date,
			int license_duration) {
		super();
		this.doc_id = doc_id;
		this.doc_name = doc_name;
		this.email = email;
		this.issued_date = issued_date;
		this.last_renewed_date = last_renewed_date;
		this.license_duration = license_duration;
	}
	
	
	public Document() {}

	@Override
	public String toString() {
		return "Document [doc_id=" + doc_id + ", doc_name=" + doc_name + ", email=" + email + ", issued_date="
				+ issued_date + ", last_renewed_date=" + last_renewed_date + ", license_duration=" + license_duration
				+ "]";
	}
	
	

}
