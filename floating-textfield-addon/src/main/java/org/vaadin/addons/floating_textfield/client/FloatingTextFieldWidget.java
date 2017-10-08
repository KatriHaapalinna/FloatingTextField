package org.vaadin.addons.floating_textfield.client;

import com.vaadin.client.ui.VTextField;

// Extend any GWT Widget
public class FloatingTextFieldWidget extends VTextField {
    private static final String CLASS_NAME = "floating-textfield";
    private String placeholder;

    public FloatingTextFieldWidget() {

        // CSS class-name should not be v- prefixed
        setStyleName(CLASS_NAME);

        // State is set to widget in FloatingTextFieldConnector
    }

}