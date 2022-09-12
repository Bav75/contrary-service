package org.contrary.assessment.service;

import java.util.ArrayList;
import java.util.List;

import org.contrary.assessment.model.Investor;
import org.contrary.assessment.repository.InvestorsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class InvestorService {
	
	@Autowired
	InvestorsRepository investorsRepo;
	
	void persist(List<Investor> investors) {
		log.info("msg=Begin persisting {} investors records into investors repo", investors.size());
		
		try {
			investorsRepo.saveAll(investors);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to persist investors records into investors repo, error=" + e.getMessage());
		}
		
		log.info("msg=Completed persisting investors records into investors repo");
	}
	
	List<String> findInvestorsByCompany(String companyLinkedInName) {
		log.info("msg=Begin retrieving investors from investors repo by companyLinkedInName={}", companyLinkedInName);
		List<String> investors = new ArrayList<>();
		
		try {
			investors = investorsRepo.findInvestorsByCompany(companyLinkedInName);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to investors by companyLinkedInName, error=" + e.getMessage());
		}
		
		log.info("msg=Completed retrieving investors from investors repo for companyLinkedInName={}", companyLinkedInName);
		
		return investors;
	}
	
	

}
