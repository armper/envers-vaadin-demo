package gov.noaa.ims.common.views.enversdemo;

import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;

import gov.noaa.ims.common.service.Person;
import gov.noaa.ims.common.service.PersonService;
import gov.noaa.ims.common.service.RevisionInfo;

@SpringComponent
public class RevisionChangesDialog extends Dialog {

    private final PersonService personService;

    public RevisionChangesDialog(PersonService personService) {
        this.personService = personService;
    }

    @Transactional(readOnly = true)
    public void showChangesForRevision(Person currentPerson, RevisionInfo revisionInfo) {
        removeAll();
        addTitle(revisionInfo);
        addPersonComparison(currentPerson, revisionInfo);
    }

    private void addTitle(RevisionInfo revisionInfo) {
        String title = String.format("Revision Changes View for revision: %d", revisionInfo.getRevisionNumber());
        add(new H4(title));
    }

    private void addPersonComparison(Person currentPerson, RevisionInfo revisionInfo) {
        Person personAtRevision = fetchPersonAtRevision(currentPerson, revisionInfo);
        VerticalLayout currentPersonLayout = createPersonLayout("Current", currentPerson);
        VerticalLayout revisedPersonLayout = createPersonLayout(revisionInfo.getRevisionDate().toString(),
                personAtRevision);
        add(new HorizontalLayout(currentPersonLayout, revisedPersonLayout));
    }

    private Person fetchPersonAtRevision(Person person, RevisionInfo revisionInfo) {
        return personService.getPersonAtRevision(person.getId(), revisionInfo.getRevisionNumber());
    }

    private VerticalLayout createPersonLayout(String title, Person person) {
        return new VerticalLayout(new Paragraph(title), new PersonDetails(person));
    }
}
