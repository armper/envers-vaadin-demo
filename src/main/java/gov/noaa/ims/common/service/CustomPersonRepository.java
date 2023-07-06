package gov.noaa.ims.common.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

public interface CustomPersonRepository {

    public EntityManager getEntityManager();

    public int count();

    public void merge(Person person);

    public void save(Person person);

    public List<Person> findAll();

    public Optional<Person> findById(Integer personId);

    public void delete(Person person);

    public List<Person> findAllJoinAddress();
}
