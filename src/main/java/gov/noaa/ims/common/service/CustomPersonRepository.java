package gov.noaa.ims.common.service;

import javax.persistence.EntityManager;

public interface CustomPersonRepository {
    EntityManager getEntityManager();

}
