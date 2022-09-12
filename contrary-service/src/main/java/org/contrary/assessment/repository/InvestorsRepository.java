package org.contrary.assessment.repository;

import java.util.List;

import org.contrary.assessment.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestorsRepository extends JpaRepository<Investor, Long> {

	@Query(value = "select investor from investors where company_linked_in_name like (:#{#companyLinkedInName})",
			nativeQuery = true)
	public List<String> findInvestorsByCompany(@Param("companyLinkedInName") String companyLinkedInName);
	
}
