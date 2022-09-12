package org.contrary.assessment.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.contrary.assessment.model.Company;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompanyParser {

	public enum CompaniesHeaders {
		NAME, 
		COMPANY_LINKEDIN_NAMES,
		DESCRIPTION,
		HEADCOUNT,
		FOUNDING_DATE,
		MOST_RECENT_RAISE,
		MOST_RECENT_VALUATION,
		KNOWN_TOTAL_FUNDING
	}
	
	public static final String HEADCOUNT = "HEADCOUNT";
	public static final String MOST_RECENT_RAISE = "MOST_RECENT_RAISE";
	public static final String MOST_RECENT_VALUATION = "MOST_RECENT_VALUATION";
	public static final String KNOWN_TOTAL_FUNDING = "KNOWN_TOTAL_FUNDING";
	public static final String FOUNDING_DATE = "FOUNDING_DATE";
	
	public static List<Company> parseCsvToCompanies(InputStream input) {
		log.info("msg=Beginning to parse CSV file to companies data model");
		List<Company> companies = new ArrayList<>();
		
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			CSVFormat format = CSVFormat.Builder.create()
					.setHeader(CompaniesHeaders.class)
					.setTrim(true)
					.setQuote(null)
					.setNullString("empty")
					.build();
			
			CSVParser csvParser = new CSVParser(fileReader, format);
			List<CSVRecord> csvRecords = csvParser.getRecords();
			
			companies = csvRecords
			.stream()
			.map(record -> buildCompany(record))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
			
			csvParser.close();		
		} catch (IOException e) {
			log.error("msg=Error encountered while parsing CSV file to companies data model, error={}", e);
			throw new RuntimeException("msg=Failed to parse csv file, error=" + e.getMessage());
		}
		
		return companies;
	}
	
	private static Company buildCompany(CSVRecord record) {			
		try {		
			String companyName = record.get("NAME");

			if (record.size() == 1) return null;
			if (companyName.equalsIgnoreCase("NAME")) return null;
			
			String companyLinkedInName = record.get("COMPANY_LINKEDIN_NAMES");
			String description = record.get("DESCRIPTION");
			int headCount = isValidRecord(record, HEADCOUNT) ? Integer.parseInt(record.get(HEADCOUNT)) : 0;
			double mostRecentRaise = isValidRecord(record, MOST_RECENT_RAISE) ? Double.parseDouble(record.get(MOST_RECENT_RAISE)) : 0.0;
			double mostRecentValuation = isValidRecord(record, MOST_RECENT_VALUATION) ? Double.parseDouble(record.get(MOST_RECENT_VALUATION)) : 0.0;
			double knownTotalFunding = isValidRecord(record, KNOWN_TOTAL_FUNDING) ? Double.parseDouble(record.get(KNOWN_TOTAL_FUNDING)) : 0.0;
			String foundingDateString;
			Date foundingDate;
			
			if (isValidRecord(record, FOUNDING_DATE)) {
				foundingDateString = record.get(FOUNDING_DATE);

				LocalDate unformatted = LocalDate.parse(foundingDateString, DateTimeFormatter.ofPattern("M/d/yyyy"));
				String formatted = unformatted.format(DateTimeFormatter.ofPattern("yyyy-M-d"));
				foundingDate = StringUtils.hasLength(foundingDateString) ?
						Date.valueOf(formatted) : null;
			} else {
				foundingDate = null;
			}

			return new Company(companyName, companyLinkedInName, description, 
					headCount, foundingDate, mostRecentRaise, mostRecentValuation, knownTotalFunding);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static boolean isValidRecord(CSVRecord record, String field) {
		return record.isSet(field) && !StringUtils.hasLength(record.get(field));
	}
	
}
