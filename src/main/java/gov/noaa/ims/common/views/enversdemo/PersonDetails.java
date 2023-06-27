package gov.noaa.ims.common.views.enversdemo;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import gov.noaa.ims.common.service.Person;

public class PersonDetails extends VerticalLayout {

    public PersonDetails(Person person) {

        add(new Paragraph("Id: " + person.getId()));

        add(new Paragraph("Name: " + person.getName()));

        add(new Paragraph("Surname: " + person.getSurname()));

        add(new Paragraph("Address" + person.getAddress()));
    }
}
