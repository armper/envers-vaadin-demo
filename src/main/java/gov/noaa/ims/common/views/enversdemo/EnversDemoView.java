package gov.noaa.ims.common.views.enversdemo;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import com.github.javafaker.Faker;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import gov.noaa.ims.common.views.Address;
import gov.noaa.ims.common.views.Person;

@PageTitle("Envers Demo")
@Route(value = "envers")
@RouteAlias(value = "")
public class EnversDemoView extends VerticalLayout {

    private final PersonRepository personRepository;
    private final Faker faker = new Faker();

    public EnversDemoView(PersonRepository personRepository) {
        this.personRepository = personRepository;
        initializeDatabaseIfEmpty();
        add(createTitle("Envers Demo"), createGridForPerson(), createGridForPersonRevision());
    }

    private Component createTitle(String titleText) {
        return new H1(titleText);
    }

    private Grid<Person> createGridForPersonRevision() {
        AuditReader reader = createAuditReader();
        Grid<Person> gridForPersonRevision = createGridWithItems(
                reader.createQuery().forRevisionsOfEntity(Person.class, true, true).getResultList());
        prependTitleToGrid(gridForPersonRevision, "Person Revision List");
        return gridForPersonRevision;
    }

    private GridPro<Person> createGridForPerson() {
        GridPro<Person> gridForPerson = createGridWithItems(personRepository.findAll());
        prependTitleToGrid(gridForPerson, "Person List");
        return gridForPerson;
    }

    private GridPro<Person> createGridWithItems(List<Person> items) {
        GridPro<Person> grid = new GridPro<>();

        grid.setItems(items);

        grid.addEditColumn(Person::getName)
                .text((person, value) -> {
                    person.setName(value);
                    savePerson(person);
                }).setHeader("Name");
        grid.addEditColumn(Person::getSurname)
                .text((person, value) -> {
                    person.setSurname(value);
                    savePerson(person);
                }).setHeader("Surname");
        grid.addEditColumn(person -> person.getAddress().getStreet())
                .text((person, value) -> {
                    person.getAddress().setStreet(value);
                    savePerson(person);
                }).setHeader("Street");
        grid.addEditColumn(person -> person.getAddress().getCity())
                .text((person, value) -> {
                    person.getAddress().setCity(value);
                    savePerson(person);
                }).setHeader("City");
        grid.addEditColumn(person -> person.getAddress().getZipCode())
                .text((person, value) -> {
                    person.getAddress().setZipCode(value);
                    savePerson(person);
                }).setHeader("Zip Code");

        return grid;
    }

    private void savePerson(Person person) {
        this.personRepository.save(person);
    }

    private void prependTitleToGrid(Grid<Person> grid, String titleText) {
        Component title = new H2(titleText);
        grid.prependHeaderRow().join(grid.getColumns().stream().toArray(Grid.Column[]::new)).setComponent(title);
    }

    private AuditReader createAuditReader() {
        EntityManager entityManager = personRepository.getEntityManager();
        return AuditReaderFactory.get(entityManager);
    }

    private void initializeDatabaseIfEmpty() {
        if (personRepository.count() == 0) {
            populateDatabaseWithRandomTestData();
        }
    }

    private void populateDatabaseWithRandomTestData() {
        for (int i = 0; i < 10; i++) {
            Person person = createRandomPerson();
            personRepository.save(person);
        }
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

}
