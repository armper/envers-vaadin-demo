package gov.noaa.ims.common.views.enversdemo;

import java.util.List;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import gov.noaa.ims.common.service.Person;

public class PersonDetails extends VerticalLayout {

    private final List<String> highlightFields;

    public PersonDetails(Person person, List<String> highlightFields) {
        this.highlightFields = highlightFields;

        if (person == null) {
            return;
        }

        addDetails("id", "Id", String.valueOf(person.getId()));
        addDetails("name", "Name", person.getName());
        addDetails("surname", "Surname", person.getSurname());
        addDetails("address.id", "Id", person.getAddress().getId() + "");
        addDetails("address.street", "Street", person.getAddress().getStreet());
        addDetails("address.city", "City", person.getAddress().getCity());
        addDetails("address.zip", "Zip", person.getAddress().getZipCode());
    }

    private void addDetails(String id, String label, String value) {
        Paragraph paragraph = createParagraph(label, value);

        if (highlightFields.contains(id)) {
            highlight(paragraph);
        }

        add(paragraph);
    }

    private Paragraph createParagraph(String label, String value) {
        return new Paragraph(new Span(label + ": " + value));
    }

    private void highlight(Paragraph paragraph) {
        paragraph.getElement().getStyle().set("background-color", "yellow");
    }
}
