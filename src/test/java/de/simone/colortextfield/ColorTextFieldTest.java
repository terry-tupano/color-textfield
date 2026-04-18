package de.simone.colortextfield;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class ColorTextFieldTest extends VerticalLayout {

    public ColorTextFieldTest() {
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        ColorTextField favoriteColor = new ColorTextField("Favorite Color");
        TextField addressField = new TextField("Address");
        ColorTextField houseColor = new ColorTextField("House Color");

        FormLayout formLayout = new FormLayout(firstName, lastName, favoriteColor, addressField, houseColor);
        formLayout.setColspan(addressField, 2);

        favoriteColor.setValue("getTestId()");
        add(formLayout);
    }
}
