package org.contrary.assessment.repository;

import java.util.List;

import org.contrary.assessment.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompaniesRepository extends JpaRepository<Company, Long>{

	@Query(value = "select companies.id, companies.company_name, companies.company_linked_in_name, companies.description, companies.head_count, companies. most_recent_raise, companies.founding_date, companies.founding_date, companies.most_recent_valuation, companies.known_total_funding from companies left join people on companies.company_name like CONCAT('%',people.company_name,'%') where people.person_id = :#{#personId}",
			nativeQuery = true)
	public List<Company> findCompaniesByPerson(@Param("personId") String personId);
	
}
