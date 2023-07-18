package gov.noaa.ims.common.views.enversdemo;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import gov.noaa.ims.common.service.Address;
import gov.noaa.ims.common.service.Person;
import gov.noaa.ims.common.service.PersonService;
import gov.noaa.ims.common.service.TestDataGenerator;

@PageTitle("Envers Demo")
@Route
public class EnversDemoView extends VerticalLayout {

    private final PersonService personService;

    private final RevisionHistoryDialog revisionHistoryLayout;

    private GridPro<Person> personGrid;

    public EnversDemoView(TestDataGenerator testDataGenerator,
            PersonService personService, RevisionHistoryDialog revisionHistoryDialog) {
        this.personService = personService;
        this.revisionHistoryLayout = revisionHistoryDialog;

        testDataGenerator.populateDatabaseIfEmpty();

        personGrid = createGridProWithItems(personService.getAllPersons(), "Persons List");

        Button viewRevisionHistoryButton = new Button("Revision History", event -> {
            personGrid.getSelectionModel().getFirstSelectedItem().ifPresent(person -> {
                showRevisionHistoryLayoutDialog(person);
            });
        });

        add(new H1("Envers Demo"),
                personGrid,
                viewRevisionHistoryButton, revisionHistoryDialog, populateWithNewAddressObjectButton());

    }

    private Button populateWithNewAddressObjectButton() {
        return new Button("Populate with new Address object", event -> {
            personGrid.getSelectionModel().getFirstSelectedItem().ifPresent(person -> {
                person.setAddress(newAddress());
                savePerson(person);
                personGrid.getDataProvider().refreshAll();
            });
        });
    }

    private Address newAddress() {
        Address address = personService.getAddressByStreet("New Street Object");

        return address;
    }

    private void showRevisionHistoryLayoutDialog(Person person) {

        revisionHistoryLayout.setPerson(person);
        revisionHistoryLayout.open();
    }

    public GridPro<Person> createGridProWithItems(List<Person> items, String title) {
        GridPro<Person> grid = new GridPro<>();

        // add selection model
        grid.setSelectionMode(GridPro.SelectionMode.MULTI);

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
        this.personService.save(person);
    }
}
