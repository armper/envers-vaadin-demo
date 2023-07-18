package gov.noaa.ims.common.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, UUID>, CustomPersonRepository {

    @Query("SELECT p FROM Person p JOIN FETCH p.address")
    List<Person> findAllJoinFetchAddress();
}
