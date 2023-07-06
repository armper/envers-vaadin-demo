package gov.noaa.ims.common.service;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

@Component
public class TestDataGenerator {

    private final Faker faker = new Faker();
    private final CustomPersonRepository personRepository;

    public TestDataGenerator(CustomPersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private Person createRandomPerson() {
        Person person = new Person();
        person.setName(faker.name().firstName());
        person.setSurname(faker.name().lastName());
        person.setAddress(createRandomAddress());
        return person;
    }

    private Address createRandomAddress() {
        Address address = new Address();
        address.setCity(faker.address().city());
        address.setStreet(faker.address().streetAddress());
        address.setZipCode(faker.address().zipCode());
        return address;
    }

    @Transactional
    public void populateDatabaseIfEmpty() {
        if (personRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                personRepository.merge(createRandomPerson());
            }
        }

    }
}