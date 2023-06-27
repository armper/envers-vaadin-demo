package gov.noaa.ims.common.service;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditReaderProvider {

    private final CustomPersonRepository personRepository;

    public AuditReaderProvider(CustomPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public AuditReader createAuditReader() {
        EntityManager entityManager = personRepository.getEntityManager();
        return AuditReaderFactory.get(entityManager);
    }
}