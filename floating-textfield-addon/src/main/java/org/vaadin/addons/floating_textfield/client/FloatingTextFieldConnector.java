package org.vaadin.addons.floating_textfield.client;

import org.vaadin.addons.floating_textfield.FloatingTextField;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.VLabel;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(FloatingTextField.class)
public class FloatingTextFieldConnector extends TextFieldConnector {
    private static final String CLASS_NAME = "floating-textfield";
    private static final String ERROR_CLASS = "v-textfield-error";
    FloatingTextFieldWidget widget;
    Element floatElement;
    VLabel label;
    Element parent;
    Element captionElement;
    String placeholder;
    boolean fieldFocus = false;
    // ServerRpc is used to send events to server. Communication implementation
    // is automatically created here
    FloatingTextFieldServerRpc rpc = RpcProxy
            .create(FloatingTextFieldServerRpc.class, this);

    public FloatingTextFieldConnector() {
    }

    // We must implement getWidget() to cast to correct type
    // (this will automatically create the correct widget type)
    @Override
    public FloatingTextFieldWidget getWidget() {
        return (FloatingTextFieldWidget) super.getWidget();
    }

    // We must implement getState() to cast to correct type
    @Override
    public FloatingTextFieldState getState() {
        return (FloatingTextFieldState) super.getState();
    }

    // Whenever the state changes in the server-side, this method is called
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        if (widget == null) {
            widget = getWidget();
            floatElement = widget.getElement();
            addPlaceholder();
        }
        boolean currentFocus = fieldFocus;
        Element previousParent = widget.getElement().getParentElement();
        if (!previousParent.isOrHasChild(parent)) {
            previousParent.replaceChild(parent, floatElement);
            parent.appendChild(floatElement);
        }
        label.getElement().removeClassName("error-text");
        label.getElement().removeClassName("error");

        if (widget.getStyleName().contains(ERROR_CLASS)) {
            if ((currentFocus) || (!widget.getText().isEmpty())) {
                label.getElement().addClassName("error-text");
            } else if (widget.getText().isEmpty()) {
                label.getElement().addClassName("error");
            }
        }
        if (currentFocus) {
            floatElement.focus();
        } else {
            floatElement.blur();
        }
    }

    private void addPlaceholder() {
        placeholder = getState().pl;
        parent = DOM.createDiv();
        captionElement = DOM.createDiv();
        label = new VLabel();
        widget.getElement().addClassName(CLASS_NAME);
        parent.addClassName("v-widget " + CLASS_NAME + "-widget");
        captionElement.setAttribute("class", CLASS_NAME + "-frame");

        widget.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                fieldFocus = true;
                label.getElement()
                        .addClassName(CLASS_NAME + "-placeholder-focus");
                if (label.getStyleName().contains("error")) {
                    label.getElement().removeClassName("error");
                    label.getElement().addClassName("error-text");
                }
            }

        });
        widget.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                fieldFocus = false;
                if (widget.getText() != null && !widget.getText().isEmpty()) {
                    return;
                } else if (widget.getStyleName().contains(ERROR_CLASS)) {
                    label.getElement().addClassName("error");
                }
                label.getElement()
                        .removeClassName(CLASS_NAME + "-placeholder-focus");
            }

        });
        label.getElement().setClassName(CLASS_NAME + "-placeholder");
        label.setText(placeholder);
        captionElement.appendChild(label.getElement());
        label.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                alert("noted");
                widget.setFocus(!fieldFocus);
                fieldFocus = !fieldFocus;
            }

        });
        parent.appendChild(captionElement);
    }

    private static native void alert(String s) /*-{
                                               alert(s);
                                               }-*/;

}
