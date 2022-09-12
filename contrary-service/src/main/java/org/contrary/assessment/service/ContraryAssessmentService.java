package org.contrary.assessment.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.input.BOMInputStream;
import org.contrary.assessment.model.Company;
import org.contrary.assessment.model.Investor;
import org.contrary.assessment.model.Person;
import org.contrary.assessment.parser.CompanyParser;
import org.contrary.assessment.parser.InvestorParser;
import org.contrary.assessment.parser.PeopleParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RestController
@AllArgsConstructor
@Slf4j
public class ContraryAssessmentService {
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private PeopleService peopleService;
	
	@Autowired
	private InvestorService investorService;
	
	@Autowired
	private final RestTemplate restTemplate;
	
	public static final String PEOPLE_URL = "https://contrary-engineering-interview.s3.amazonaws.com/data/people.csv";
	public static final String COMPANIES_FILE_PATH = "../../Downloads/companies.csv";
	public static final String INVESTORS_FILE_PATH = "../../Downloads/investors.csv";
	
	@GetMapping("/people")
	public String readPeopleData() {		
		String response;
		
		try {
			log.info("msg=Reading people data from S3 url endpoint.");
			response = this.restTemplate.getForObject(PEOPLE_URL, String.class);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to retrieve people data from url endpoint" + e.getMessage());
		}
		
		log.info("msg=Retrieved the following people data={}", response);
		
		log.info("msg=Beginning formatting of people data");
		response = formatPeopleData(response);
		log.info("msg=Formatted people data to {}", response);

		List<Person> people = PeopleParser.parseCsvToPeople(new BOMInputStream(new ByteArrayInputStream(response.getBytes())));
		log.info("msg=Parsed people data into {}", people);	
		
		peopleService.persist(people);	
		return response;
	}
	
	@GetMapping("/companies")
	public String readCompaniesData() throws IOException {
		String response;
		
		try {
			log.info("msg=Reading companies data from filepath {}", COMPANIES_FILE_PATH);	
			Path path = Path.of(COMPANIES_FILE_PATH);
			response = Files.readString(path);
			log.info("msg=Retrieved the following companies data={}", response);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to retrieve companies data from filepath" + e.getMessage());
		}	
		
		log.info("msg=Beginning formatting of companies data");
		response = formatCompaniesData(response);
		log.info("msg=Formatted companies data to {}", response);
		
		List<Company> companies = CompanyParser.parseCsvToCompanies(new BOMInputStream(new ByteArrayInputStream(response.getBytes())));
		log.info("msg=Parsed companies data into {}", companies);	
		
		companyService.persist(companies);
		
		return response;
	}
	
	@GetMapping("/investors")
	public String readInvestorsData() throws IOException {		
		String response;
		
		try {
			log.info("msg=Reading investors data from filepath.");	
			Path path = Path.of(INVESTORS_FILE_PATH);
			response = Files.readString(path);
			log.info("msg=Returned the following companies data={}", response);
		} catch (Exception e) {
			throw new RuntimeException("msg=Failed to retrieve investors data from filepath" + e.getMessage());
		}	

		log.info("msg=Beginning formatting of investors data");
		response = formatInvestorsData(response);
		log.info("msg=Formatted investors data to {}", response);

		List<Investor> investors = InvestorParser.parseCsvToInvestors(new BOMInputStream(new ByteArrayInputStream(response.getBytes())));
		log.info("msg=Parsed investors data into {}", investors);	

		investorService.persist(investors);
		return response;
	}
	
	@GetMapping(value = "/avg-funding-by-person/{personId}")
	public double averageFundingByPerson(@PathVariable("personId") String personId) {
		log.info("msg=Begin retrieving average funding associated with personId={}", personId);
		double avgFunding = peopleService.findAvgFundingByPerson(personId);
		log.info("msg=Retrieved average company funding of {} for personId={}", avgFunding, personId);
		return avgFunding;
	}
	
	@GetMapping(value = "/companies-by-person/{personId}")
	public List<Company> findCompaniesByPerson(@PathVariable("personId") String personId) {
		log.info("msg=Begin retrieving companies worked at by person with personId={}", personId);
		List<Company> companies = companyService.findCompaniesByPerson(personId);
		log.info("msg=Retrieved the following companies {}", companies);
		return companies;
	}
	
	@GetMapping(value = "/investors-by-company/{companyLinkedInName}")
	public List<String> findInvestorsByCompany(@PathVariable("companyLinkedInName") String companyLinkedInName) {
		log.info("msg=Begin investors associated with company={}", companyLinkedInName);
		List<String> investors = investorService.findInvestorsByCompany(companyLinkedInName);
		log.info("msg=Retrieved {} investors associated with company={}, investors={}", investors.size(), companyLinkedInName, investors);
		return investors;
	}
	
	private String formatPeopleData(String peopleData) {
		return peopleData.replaceAll("\"", "").replaceAll("\\([^)]*\\)", "");
	}
	
	private String formatCompaniesData(String companiesData) {
		return companiesData.replaceAll("ï»¿", "")
				.replaceAll("[\\[\\]']+", "")
				.replaceAll("\\\"\n", "")
				.replaceAll(",\s", "")
				.replaceAll("\"\"(?=\\w)", ",")
				.replaceAll("(?<=\\w)\"\"", "");
	}
	
	private String formatInvestorsData(String investorsData) {
		return investorsData.replaceAll("\n", "")
				.replaceAll("\"", "")
				.replaceAll("\s", "");
	}
	

}
