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
@Table(name = "companies")
@Data
@NoArgsConstructor
public class Company {
	
	public Company (String companyName, String companyLinkedInName, String description, int headCount,
			Date foundingDate, double mostRecentRaise, double mostRecentValuation, double knownTotalFunding) {
		this.companyName = companyName;
		this.companyLinkedInName = companyLinkedInName;
		this.description = description;
		this.headCount = headCount;
		this.foundingDate = foundingDate;
		this.mostRecentRaise = mostRecentRaise;
		this.mostRecentValuation = mostRecentValuation;
		this.knownTotalFunding = knownTotalFunding;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "company_linked_in_name")
	private String companyLinkedInName;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "head_count")
	private int headCount;
	
	@Column(name = "founding_date")
	private Date foundingDate;
	
	@Column(name = "most_recent_raise")
	private double mostRecentRaise;
	
	@Column(name = "most_recent_valuation")
	private double mostRecentValuation;
	
	@Column(name = "known_total_funding")
	private double knownTotalFunding;

}
