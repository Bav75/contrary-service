package org.contrary.assessment.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.contrary.assessment.model.Investor;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.StringUtils.substringBetween;

@Slf4j
public class InvestorParser {

	public enum InvestorsHeaders {
		NAME, 
		COMPANY_LINKEDIN_NAMES,
		INVESTORS
	}
	
	public static final String NAME = "NAME";
	public static final String COMPANY_LINKEDIN_NAMES = "COMPANY_LINKEDIN_NAMES";
	public static final String INVESTORS = "INVESTORS";
	
	public static List<Investor> parseCsvToInvestors(InputStream input) {
		log.info("msg=Beginning to parse CSV file to investors data model");
		List<Investor> investors = new ArrayList<>();
		
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			CSVFormat format = CSVFormat.Builder.create()
					.setHeader(InvestorsHeaders.class)
					.setTrim(true)
					.setQuote(null)
					.setNullString("empty")
					.build();
			
			CSVParser csvParser = new CSVParser(fileReader, format);
			List<CSVRecord> csvRecords = csvParser.getRecords();
			
			investors = csvRecords
			.stream()
			.map(record -> buildInvestor(record))
			.filter(Objects::nonNull)
			.flatMap(Collection::stream)
			.collect(Collectors.toList());
			
			csvParser.close();		
		} catch (IOException e) {
			log.error("msg=Error encountered while parsing CSV file to companies data model, error={}", e);
			throw new RuntimeException("msg=Failed to parse csv file, error=" + e.getMessage());
		}
		
		return investors;
	}
	
	private static List<Investor> buildInvestor(CSVRecord record) {	
		List<Investor> investors = new ArrayList<>();		
		
		try {		
			String companyName = record.get(NAME);

			if (record.size() == 1) return null;			
			if (companyName.equalsIgnoreCase("NAME")) return null;
			
			String companyLinkedInName = record.get(COMPANY_LINKEDIN_NAMES);
			
			if (StringUtils.isNotEmpty(companyLinkedInName)) {
				companyLinkedInName = companyLinkedInName.replaceAll("\\[|\\]", "");
			}

			String investorSubstring = substringBetween(record.toString(), "], [", "]");
			
			if (StringUtils.isNotEmpty(investorSubstring)) {
				for (String investor : investorSubstring.split(",")) {
					investors.add(new Investor(companyName, companyLinkedInName, investor));
				}
			}		
						
			return investors;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
