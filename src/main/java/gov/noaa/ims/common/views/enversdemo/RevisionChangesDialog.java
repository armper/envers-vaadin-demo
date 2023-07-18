package gov.noaa.ims.common.views.enversdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import gov.noaa.ims.common.service.Address;
import gov.noaa.ims.common.service.Person;
import gov.noaa.ims.common.service.PersonService;
import gov.noaa.ims.common.service.RevisionInfo;

@SpringComponent
public class RevisionChangesDialog extends Dialog {

    private final PersonService personService;

    private Person currentPerson;
    private Person personAtRevision;

    public RevisionChangesDialog(PersonService personService) {
        this.personService = personService;

        setResizable(true);

        setDraggable(true);

    }

    @Transactional
    public void showChangesForRevision(Person currentPerson, RevisionInfo revisionInfo) {
        this.currentPerson = currentPerson;

        removeAll();
        addTitle(revisionInfo);
        addPersonComparison(revisionInfo);
    }

    private void addTitle(RevisionInfo revisionInfo) {
        String title = String.format("Revision Changes View for revision: %d", revisionInfo.getRevisionNumber());
        add(new H4(title));
    }

    private void addPersonComparison(RevisionInfo revisionInfo) {
        this.personAtRevision = fetchPersonAtRevision(revisionInfo);
        VerticalLayout currentPersonLayout = createPersonLayoutWithShadow("Current Entity", currentPerson);
        VerticalLayout revisedPersonLayout = createPersonLayoutWithShadow(revisionInfo.getRevisionDate().toString(),
                personAtRevision);
        add(new HorizontalLayout(currentPersonLayout, revisedPersonLayout));

        add(getRevertButton());

    }

    private Person fetchPersonAtRevision(RevisionInfo revisionInfo) {
        return personService.getPersonAtRevision(currentPerson.getId(), revisionInfo.getRevisionNumber());
    }

    private VerticalLayout createPersonLayoutWithShadow(String title, Person person) {
        VerticalLayout verticalLayout = new VerticalLayout(new H4(title),
                new PersonDetails(person, highlightedFields()));
        verticalLayout.getStyle().set("box-shadow", "0 0 5px 0 rgba(0, 0, 0, 0.2)");
        return verticalLayout;
    }

    private List<String> highlightedFields() {
        List<String> highlightedFields = new ArrayList<>();

        if (personAtRevision == null) {
            return highlightedFields;
        }

        if (!Objects.equals(currentPerson.getName(), personAtRevision.getName())) {
            highlightedFields.add("name");
        }

        if (!Objects.equals(currentPerson.getSurname(), personAtRevision.getSurname())) {
            highlightedFields.add("surname");
        }

        Address currentAddress = currentPerson.getAddress();
        Address revisedAddress = personAtRevision.getAddress();

        if (!Objects.equals(currentAddress.getId(), revisedAddress.getId())) {
            highlightedFields.add("address.id");
        }

        if (!Objects.equals(currentAddress.getStreet(), revisedAddress.getStreet())) {
            highlightedFields.add("address.street");
        }

        if (!Objects.equals(currentAddress.getCity(), revisedAddress.getCity())) {
            highlightedFields.add("address.city");
        }

        if (!Objects.equals(currentAddress.getZipCode(), revisedAddress.getZipCode())) {
            highlightedFields.add("address.zip");
        }

        return highlightedFields;
    }

    private Button getRevertButton() {
        return new Button("Revert", event -> {
            Dialog revertConfirmationDialog = new Dialog();
            revertConfirmationDialog.add(new Paragraph("Are you sure you want to revert?"));
            revertConfirmationDialog.add(new Button("Yes", revertEvent -> {
                revert(revertEvent);
                revertConfirmationDialog.close();
            }));
            revertConfirmationDialog.add(new Button("No", revertEvent -> revertConfirmationDialog.close()));
            revertConfirmationDialog.open();
        });
    }

    private void revert(ClickEvent<Button> revertEvent) {
        Notification.show("Reverting changes!");
    }

}
