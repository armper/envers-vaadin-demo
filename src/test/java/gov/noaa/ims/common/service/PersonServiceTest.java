package gov.noaa.ims.common.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonServiceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceTest.class);

    @Autowired
    private PersonService personService;

    @Test
    public void testModificationOfPersonAddress() {
        Person initialPerson = createAndSaveTestPerson();

        Person updatedPerson = updatePersonAddress(initialPerson);
        personService.merge(updatedPerson);

        List<RevisionInfo> revisionInfos = personService.getRevisionInfosForPerson(updatedPerson);
        assertEquals(2, revisionInfos.size(), "Expected 2 revisions for the updated person");

        int latestRevisionNumber = revisionInfos.get(1).getRevisionNumber();
        Person revisedPerson = personService.getPersonAtRevision(updatedPerson.getId(), latestRevisionNumber);

        validateUpdatedPerson(revisedPerson);
    }

    private Person createAndSaveTestPerson() {
        Person person = new Person();
        person.setName("Test First");
        person.setSurname("Test Last");

        Address address = new Address();
        address.setCity("Test City");
        address.setStreet("Test Street");
        address.setZipCode("Test Zip");

        person.setAddress(address);

        personService.merge(person);

        return person;
    }

    private Person updatePersonAddress(Person personToRevise) {
        personToRevise.setName("Test First Modified");

        Address existingAddress = personToRevise.getAddress();
        Address newAddress = new Address();
        newAddress.setCity(existingAddress.getCity() + " Modified");
        newAddress.setStreet(existingAddress.getStreet());
        newAddress.setZipCode(existingAddress.getZipCode());

        personToRevise.setAddress(newAddress);

        return personToRevise;
    }

    private void validateUpdatedPerson(Person person) {
        assertEquals("Test First Modified", person.getName(),
                "Expected the name of the revised person to be 'Test First Modified'");
        assertEquals("Test City Modified", person.getAddress().getCity(),
                "Expected the city of the revised person's address to be 'Test City Modified'");

        LOGGER.info("Revised Person: Name - {}, City - {}", person.getName(), person.getAddress().getCity());
    }
}
