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
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("FloatingTextField Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {
    private String name1;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        Binder<String> binder = new Binder<>();

        // FloatingTextField with binder and validator
        final FloatingTextField validationField = new FloatingTextField();
        validationField.setCaption("Caption");
        validationField.setPlaceholder("Required and tall field");
        binder.forField(validationField)
                .withValidator(new StringLengthValidator("1-10", 1, 10))
                .asRequired("can't be empty")
                .bind(s -> name1, (s, v) -> name1 = v);

        final FloatingTextField field = new FloatingTextField("Default field");
        Button b = new Button("Change sizes");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout hl = new HorizontalLayout(validationField);
        hl.setMargin(true);
        layout.setMargin(true);
        layout.addComponents(field, validationField, b);

        b.addClickListener(l -> {
            field.setWidth("100%");
            field.setPlaceholder("Long field");
            validationField.setHeight(4, Unit.EM);
        });

        // testing content in TabSheet is always a good idea
        TabSheet t = new TabSheet();
        t.addTab(layout, "1");
        t.addTab(new VerticalLayout(new Label("Tab 2")), "2");

        setContent(t);
    }
}
