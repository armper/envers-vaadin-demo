package gov.noaa.ims.common.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CustomPersonRepositoryImpl implements CustomPersonRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

}
