package org.contrary.assessment.service;

import java.util.List;

import org.contrary.assessment.model.Person;
import org.contrary.assessment.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class PeopleService {

	@Autowired
	PeopleRepository peopleRepo;
	
	void persist(List<Person> people) {
		log.info("msg=Begin persisting {} people records into people repo", people.size());
		
		try {
			peopleRepo.saveAll(people);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to persist people records into people repo, error=" + e.getMessage());
		}
		
		log.info("msg=Completed persisting people records into people repo");
	}
	
	double findAvgFundingByPerson(String personId) {
		log.info("msg=Begin retrieving average company funding from people repo by personId={}", personId);
		double avgFunding = 0.0;
		
		try {
			avgFunding = peopleRepo.findAvgFundingByPerson(personId);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to retrieve average company funding by personId, error=" + e.getMessage());
		}
		
		log.info("msg=Completed retrieving average company funding from people repo for personId={}", personId);
		
		return avgFunding;
	}
	
	
}
