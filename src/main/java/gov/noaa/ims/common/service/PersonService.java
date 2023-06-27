package gov.noaa.ims.common.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final AuditReaderProvider auditReaderProvider;

    private final CustomPersonRepository personRepository;

    public PersonService(AuditReaderProvider auditReaderProvider, CustomPersonRepository personRepository) {
        this.auditReaderProvider = auditReaderProvider;
        this.personRepository = personRepository;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPersons() {
        return personRepository.findAllJoinAddress();
    }

    @Transactional(readOnly = true)
    public List<RevisionInfo> getRevisionInfosForPerson(Person person) {
        AuditReader auditReader = auditReaderProvider.createAuditReader();

        List<Number> revisionNumbers = auditReader.getRevisions(Person.class,
                person.getId());

        return revisionNumbers.stream().map(revisionNumber -> {
            DefaultRevisionEntity revisionEntity = auditReader.findRevision(DefaultRevisionEntity.class,
                    revisionNumber);
            return new RevisionInfo(revisionEntity.getId(),
                    revisionEntity.getRevisionDate(),
                    revisionEntity.getTimestamp());
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<RevisionInfo> getRevisionInfosForAddress(Address address) {
        AuditReader auditReader = auditReaderProvider.createAuditReader();

        List<Number> revisionNumbers = auditReader.getRevisions(Address.class,
                address.getId());

        return revisionNumbers.stream().map(revisionNumber -> {
            DefaultRevisionEntity revisionEntity = auditReader.findRevision(DefaultRevisionEntity.class,
                    revisionNumber);
            return new RevisionInfo(revisionEntity.getId(),
                    revisionEntity.getRevisionDate(),
                    revisionEntity.getTimestamp());
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Person getPersonAtRevision(Integer personId, Integer revision) {
        AuditReader auditReader = auditReaderProvider.createAuditReader();
        return auditReader.find(Person.class, personId, revision);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);

    }

    @Transactional(readOnly = true)
    public Person getPersonById(Integer personId) {
        return personRepository.findById(personId).orElse(null);
    }

}