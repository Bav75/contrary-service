package org.contrary.assessment.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.contrary.assessment.model.Person;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PeopleParser {
	
	public enum PeopleHeaders {
		PERSON_ID, 
		COMPANY_NAME, 
		COMPANY_LI_NAME,
		LAST_TITLE,
		GROUP_START_DATE,
		GROUP_END_DATE
	}
	
	public static List<Person> parseCsvToPeople(InputStream input) {
		log.info("msg=Beginning to parse CSV file to people data model");
		List<Person> people = new ArrayList<>();
		
		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			CSVFormat format = CSVFormat.Builder.create()
					.setHeader(PeopleHeaders.class)
					.setTrim(true)
					.build();
			
			CSVParser csvParser = new CSVParser(fileReader, format);
			
			List<CSVRecord> csvRecords = csvParser.getRecords();
			
			people = csvRecords
			.stream()
			.map(record -> buildPerson(record))
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
			
			csvParser.close();		
		} catch (IOException e) {
			log.error("msg=Error encountered while parsing CSV file to peopel data model, error={}", e);
			throw new RuntimeException("msg=Failed to parse csv file, error=" + e.getMessage());
		}
		
		return people;
	}
	
	private static Person buildPerson(CSVRecord record) {	
		String personId = record.get("PERSON_ID");		
		String companyName = record.get("COMPANY_NAME");
		String companyLinkedInName = record.get("COMPANY_LI_NAME");
		String lastTitle = record.get("LAST_TITLE");
			
		try {		
			String groupStartDateString;
			Date groupStartDate;
			String groupStartEndString;
			Date groupEndDate;
			
			if (record.isSet("GROUP_START_DATE")) {
				groupStartDateString = record.get("GROUP_START_DATE");
				groupStartDate = StringUtils.hasLength(groupStartDateString) ?
						Date.valueOf(groupStartDateString) : null;
			} else {
				groupStartDate = null;
			}
			
			if (record.isSet("GROUP_END_DATE")) {
				groupStartEndString = record.get("GROUP_END_DATE");
				groupEndDate = StringUtils.hasLength(groupStartEndString) ?
						Date.valueOf(groupStartEndString) : null;
			} else {
				groupEndDate = null;
			}
			
			return new Person(personId, companyName, companyLinkedInName, lastTitle, groupStartDate, groupEndDate);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
