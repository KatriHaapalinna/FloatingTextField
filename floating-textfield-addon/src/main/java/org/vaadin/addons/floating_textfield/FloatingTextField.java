package org.vaadin.addons.floating_textfield;

import org.vaadin.addons.floating_textfield.client.FloatingTextFieldServerRpc;
import org.vaadin.addons.floating_textfield.client.FloatingTextFieldState;

import com.vaadin.ui.TextField;

public class FloatingTextField extends TextField {
    private FloatingTextFieldServerRpc rpc = new FloatingTextFieldServerRpc() {
    };

    public FloatingTextField() {
        super();
        setWidth("185px");
        setHeight("55px");
        registerRpc(rpc);
    }

    /**
     * Creates a FloatingTextField with specified placeholder
     *
     * @param placeholder
     *            String
     */
    public FloatingTextField(String placeholder) {
        super();
        setHeight("55px");
        setWidth("185px");
        setPlaceholder(placeholder);
        registerRpc(rpc);
    }

    @Override
    public String getPlaceholder() {
        return getState().floatingPlaceholder;
    }

    /**
     * Sets placeholder for FloatingTextField
     */
    @Override
    public void setPlaceholder(String placeholder) {
        getState().floatingPlaceholder = placeholder;
    }

    @Override
    protected FloatingTextFieldState getState() {
        return (FloatingTextFieldState) super.getState();
    }
}
