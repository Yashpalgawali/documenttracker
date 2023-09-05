package com.example.demo.models;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="tbl_doc_history")
public class DocumentHistory {

	@Id
	@SequenceGenerator(name = "hist_seq",allocationSize = 1,initialValue = 1)
	@GeneratedValue(generator = "hist_seq" , strategy = GenerationType.AUTO)
	private Long hist_id;
	
	private String operation;
	
	private String op_date;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="doc_id" , referencedColumnName = "doc_id")
	private Document document;

	public Long getHist_id() {
		return hist_id;
	}

	public void setHist_id(Long hist_id) {
		this.hist_id = hist_id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOp_date() {
		return op_date;
	}

	public void setOp_date(String op_date) {
		this.op_date = op_date;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public String toString() {
		return "DocumentHistory [hist_id=" + hist_id + ", operation=" + operation + ", op_date=" + op_date
				+ ", document=" + document + "]";
	}
	
}
