package gov.noaa.ims.common.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

    private final AuditReaderProvider auditReaderProvider;

    private final PersonRepository personRepository;

    private final AddressRepository addressRepository;

    public PersonService(AuditReaderProvider auditReaderProvider, PersonRepository personRepository,
            AddressRepository addressRepository) {
        this.auditReaderProvider = auditReaderProvider;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
    }

    public List<Person> getAllPersons() {
        return personRepository.findAllJoinFetchAddress();
    }

    /*
     * This method returns a list of RevisionInfo objects for a given person.
     * 
     * @param person
     */
    @Transactional
    public List<RevisionInfo> getRevisionInfosForPerson(UUID personId) {
        AuditReader auditReader = AuditReaderFactory.get(personRepository.getEntityManager());

        List<Number> revisionNumbers = auditReader.getRevisions(Person.class,
                personId);

        return revisionNumbers.stream().map(revisionNumber -> {
            DefaultRevisionEntity revisionEntity = auditReader.findRevision(DefaultRevisionEntity.class,
                    revisionNumber);
            return new RevisionInfo(revisionEntity.getId(),
                    revisionEntity.getRevisionDate(),
                    revisionEntity.getTimestamp());
        }).collect(Collectors.toList());
    }

    /*
     * This method returns a list of RevisionInfo objects for a given address.
     * 
     * @param address
     */
    @Transactional
    public List<RevisionInfo> getRevisionInfosForAddress(Integer addressId) {
        AuditReader auditReader = auditReaderProvider.createAuditReader();

        List<Number> revisionNumbers = auditReader.getRevisions(Address.class,
                addressId);

        return revisionNumbers.stream().map(revisionNumber -> {
            DefaultRevisionEntity revisionEntity = auditReader.findRevision(DefaultRevisionEntity.class,
                    revisionNumber);
            return new RevisionInfo(revisionEntity.getId(),
                    revisionEntity.getRevisionDate(),
                    revisionEntity.getTimestamp());
        }).collect(Collectors.toList());
    }

    @Transactional
    public Person getPersonAtRevision(UUID personId, Integer revision) {
        AuditReader auditReader = auditReaderProvider.createAuditReader();
        return auditReader.find(Person.class, personId, revision);
    }

    public Person getPersonById(UUID personId) {
        return personRepository.findById(personId).orElse(null);
    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public Address getAddress(Integer addressId) {
        return addressRepository.findById(addressId).orElse(null);
    }

    public Address getAddressByStreet(String street) {
        return addressRepository.findByStreet(street);
    }

}
