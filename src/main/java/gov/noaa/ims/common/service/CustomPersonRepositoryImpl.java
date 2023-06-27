package gov.noaa.ims.common.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class CustomPersonRepositoryImpl implements CustomPersonRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public List<Person> findAll() {
        return entityManager.createQuery("from Person", Person.class).getResultList();
    }

    @Override
    public List<Person> findAllJoinAddress() {
        return entityManager.createQuery("select distinct p from Person p left join fetch p.address", Person.class)
                .getResultList();
    }

    @Override
    public void delete(Person person) {
        entityManager.remove(person);
    }

    @Override
    public int count() {
        return entityManager.createQuery("select count(*) from Person", Long.class).getSingleResult().intValue();
    }

    @Override
    public void save(Person person) {
        entityManager.merge(person);
    }

    @Override
    public Optional<Person> findById(Integer personId) {
        return Optional.ofNullable(entityManager.find(Person.class, personId));
    }

}
