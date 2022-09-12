package org.contrary.assessment.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "people")
@Data
@NoArgsConstructor
public class Person {
	
	public Person(String personId, String companyName, String companyLinkedInName, String lastTitle, Date groupStartDate, Date groupEndDate) {
		this.personId = personId;
		this.companyName = companyName;
		this.companyLinkedInName = companyLinkedInName;
		this.lastTitle = lastTitle;
		this.groupStartDate = groupStartDate;
		this.groupEndDate = groupEndDate;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "person_id")
	private String personId;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "company_linked_in_name")
	private String companyLinkedInName;
	
	@Column(name = "last_title")
	private String lastTitle;
	
	@Column(name = "group_start_date")
	private Date groupStartDate;
	
	@Column(name = "group_end_date")
	private Date groupEndDate;

}
