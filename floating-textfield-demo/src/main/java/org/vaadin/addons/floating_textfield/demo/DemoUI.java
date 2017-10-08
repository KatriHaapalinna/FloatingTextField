package org.vaadin.addons.floating_textfield.demo;

import javax.servlet.annotation.WebServlet;

import org.vaadin.addons.floating_textfield.FloatingTextField;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("FloatingTextField Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {
    private String name;
    private String name1;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        Binder<String> binder = new Binder<>();

        // FloatingTextField with placeholder
        final FloatingTextField field = new FloatingTextField("Default field");

        // FloatingTextField with binder and validator
        final FloatingTextField validationField = new FloatingTextField();
        validationField.setPlaceholder("Name");
        validationField.addValueChangeListener(event -> {
            validationField.setHeight("50px");
        });
        binder.forField(validationField)
                .withValidator(new StringLengthValidator(
                        "Name must be within 1-10 letters", 1, 10))
                .bind(s -> name, (s, v) -> name = v);
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(false);
        layout.addComponents(field, validationField);
        layout.setComponentAlignment(field, Alignment.BOTTOM_CENTER);
        layout.setComponentAlignment(validationField, Alignment.TOP_CENTER);
        setContent(layout);
    }
}
