package org.contrary.assessment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "investors")
@Data
@NoArgsConstructor
public class Investor {
	
	public Investor (String companyName, String companyLinkedInName, String investor) {
		this.companyName = companyName;
		this.companyLinkedInName = companyLinkedInName;
		this.investor = investor;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "company_name")
	private String companyName;
	
	@Column(name = "company_linked_in_name")
	private String companyLinkedInName;
	
	@Column(name = "investor")
	private String investor;

}
