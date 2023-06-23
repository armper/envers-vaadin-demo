package gov.noaa.ims.common.views.enversdemo;

import org.springframework.data.jpa.repository.JpaRepository;

import gov.noaa.ims.common.views.Person;

public interface PersonRepository extends JpaRepository<Person, Integer>, CustomPersonRepository {

}
