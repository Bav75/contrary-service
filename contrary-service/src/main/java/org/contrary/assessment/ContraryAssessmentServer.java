package org.contrary.assessment;

import static org.springframework.boot.SpringApplication.run;

import org.contrary.assessment.service.ContraryAssessmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author bvelez
 *
 */
@SpringBootApplication
@Slf4j
public class ContraryAssessmentServer {
	public static void main(String[] args) {
		try {
			log.info("msg=Starting up ContraryAssessmentService");
			run(ContraryAssessmentServer.class, args);
		} catch (Exception e) {
			log.error("msg=Fatal exception encountered during application start up! Shutting down application.", e);
			System.exit(-1);
		}
	}
	
}
