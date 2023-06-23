package gov.noaa.ims.common.views.enversdemo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

public class CustomPersonRepositoryImpl implements CustomPersonRepository {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
