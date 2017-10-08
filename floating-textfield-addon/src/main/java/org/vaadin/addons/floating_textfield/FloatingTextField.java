package org.vaadin.addons.floating_textfield;

import org.vaadin.addons.floating_textfield.client.FloatingTextFieldServerRpc;
import org.vaadin.addons.floating_textfield.client.FloatingTextFieldState;

import com.vaadin.ui.TextField;

// This is the server-side UI component that provides public API
// for FloatingTextField
public class FloatingTextField extends TextField {

    public FloatingTextField() {
        super();
        // To receive events from the client, we register ServerRpc
        FloatingTextFieldServerRpc rpc = new FloatingTextFieldServerRpc() {
        };
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
        getState().pl = placeholder;
    }

    @Override
    public void setHeight(String height) {
        getState().mainHeight = height;
    }

    @Override
    public void setHeight(float height, Unit unit) {

    }

    /**
     * Sets placeholder for FloatingTextField
     */
    @Override
    public void setPlaceholder(String placeholder) {
        getState().pl = placeholder;
    }

    // We must override getState() to cast the state to FloatingTextFieldState
    @Override
    protected FloatingTextFieldState getState() {
        return (FloatingTextFieldState) super.getState();
    }
}
