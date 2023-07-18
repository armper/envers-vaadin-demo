package gov.noaa.ims.common.service;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

@Component
public class TestDataGenerator {

    private final Faker faker = new Faker();
    private final PersonRepository personRepository;

    private final AddressRepository addressRepository;

    public TestDataGenerator(PersonRepository personRepository, AddressRepository addressRepository) {
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
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

    public void populateDatabaseIfEmpty() {
        if (personRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                personRepository.save(createRandomPerson());
            }

            // populate one Address object
            Address address = new Address();
            address.setId(777);
            address.setStreet("New Street Object");
            address.setCity("New City Object");
            address.setZipCode("New Zip Object");

            addressRepository.save(address);

        }

    }
}