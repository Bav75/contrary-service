package org.contrary.assessment.repository;

import org.contrary.assessment.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
	
	@Query(value = "select IFNULL(AVG(known_total_funding), 0.0) from ( select * from people where people.person_id = :#{#personId}) as A inner join companies C on C.company_name like CONCAT('%',A.company_name, '%')",
			nativeQuery = true)
	public double findAvgFundingByPerson(@Param("personId") String personId);
	
}
