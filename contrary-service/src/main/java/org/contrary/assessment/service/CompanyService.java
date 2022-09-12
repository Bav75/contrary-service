package org.contrary.assessment.service;

import java.util.ArrayList;
import java.util.List;

import org.contrary.assessment.model.Company;
import org.contrary.assessment.repository.CompaniesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class CompanyService {
	
	@Autowired
	CompaniesRepository companiesRepo;
	
	void persist(List<Company> companies) {
		log.info("msg=Begin persisting {} company records into companies repo", companies.size());
		
		try {
			companiesRepo.saveAll(companies);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to persist company records into companies repo, error=" + e.getMessage());
		}
		
		log.info("msg=Completed persisting company records into companies repo");
	}
	
	List<Company> findCompaniesByPerson(String personId) {
		log.info("msg=Begin retrieving companies from companies repo by personId={}", personId);
		List<Company> companies = new ArrayList<>();
		
		try {
			companies = companiesRepo.findCompaniesByPerson(personId);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to retrieve companies by personId, error=" + e.getMessage());
		}
		
		log.info("msg=Retrieved {} records from companies repo for personId={}", companies.size(), personId);
		
		return companies;
	}

}
