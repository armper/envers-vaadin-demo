package gov.noaa.ims.common.views.enversdemo;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.spring.annotation.SpringComponent;

import gov.noaa.ims.common.service.Person;
import gov.noaa.ims.common.service.PersonService;
import gov.noaa.ims.common.service.RevisionInfo;

@SpringComponent
public class RevisionHistoryDialog extends Dialog {

    private final PersonService personService;

    private final RevisionChangesDialog revisionChangesDialog;

    public RevisionHistoryDialog(PersonService personService, RevisionChangesDialog revisionChangesDialog) {
        this.personService = personService;
        this.revisionChangesDialog = revisionChangesDialog;

        // make this dialog resizable
        setResizable(true);

        setDraggable(true);
    }

    public void setPerson(Person person) {
        removeAll();

        add(new H1("Revision History"));

        add(new Paragraph("Getting revision for person: " + person.getName() + " " + person.getSurname()));

        List<RevisionInfo> personRevisions = personService.getRevisionInfosForPerson(person.getId());
        List<RevisionInfo> addressRevisions = personService.getRevisionInfosForAddress(person.getAddress().getId());

        addressRevisions.stream()
                .filter(addressRevision -> personRevisions.stream().noneMatch(
                        personRevision -> personRevision.getRevisionNumber() == addressRevision.getRevisionNumber()))
                .forEach(personRevisions::add);

        // sort personRevisions by revision number ascending
        personRevisions.sort((revision1, revision2) -> revision1.getRevisionNumber() - revision2.getRevisionNumber());

        Grid<RevisionInfo> personRevisionGrid = new Grid<>();
        personRevisionGrid.setWidth("100%");
        personRevisionGrid.setSelectionMode(GridPro.SelectionMode.SINGLE);

        personRevisionGrid.setItems(personRevisions);

        personRevisionGrid.addColumn(RevisionInfo::getRevisionNumber).setHeader("Revision Number");
        personRevisionGrid.addColumn(RevisionInfo::getRevisionDate).setHeader("Revision Date");
        personRevisionGrid.addColumn(RevisionInfo::getRevisionTimestamp).setHeader("Revision Timestamp");

        add(personRevisionGrid);

        Button viewChangesButton = new Button("View Changes", viewChangesButtonEvent -> {
            personRevisionGrid.getSelectedItems().stream().findFirst().ifPresent(revisionInfo -> {
                revisionChangesDialog.showChangesForRevision(person, revisionInfo);
                revisionChangesDialog.open();
            });
        });

        add(viewChangesButton, revisionChangesDialog);

    }

}
