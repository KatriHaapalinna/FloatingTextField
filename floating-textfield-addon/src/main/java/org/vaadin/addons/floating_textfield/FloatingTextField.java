package org.vaadin.addons.floating_textfield;

import org.vaadin.addons.floating_textfield.client.FloatingTextFieldServerRpc;
import org.vaadin.addons.floating_textfield.client.FloatingTextFieldState;

import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

// This is the server-side UI component that provides public API
// for FloatingTextField
public class FloatingTextField extends TextField {
    private FloatingTextFieldServerRpc rpc = new FloatingTextFieldServerRpc() {
    };

    public FloatingTextField() {
        super();
        registerRpc(rpc);
        // To receive events from the client, we register ServerRpc
        getState().isEdge = UI.getCurrent().getPage().getWebBrowser().isEdge();
    }

    /**
     * Creates a FloatingTextField with specified placeholder
     *
     * @param placeholder
     *            String
     */
    public FloatingTextField(String placeholder) {
        super();
        registerRpc(rpc);
        getState().isEdge = UI.getCurrent().getPage().getWebBrowser().isEdge();
        getState().floatingPlaceholder = placeholder;
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
