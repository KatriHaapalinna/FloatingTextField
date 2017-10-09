package org.vaadin.addons.floating_textfield.client;

import org.vaadin.addons.floating_textfield.FloatingTextField;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
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
    private static final String V_ERROR_CLASS = "v-textfield-error";
    private String phLabel = CLASS_NAME + "-placeholder";
    private String phFocus = CLASS_NAME + "-placeholder-focus";
    private String phErrorText = "placeholder-error-text";
    private String phError = "placeholder-error";
    private static final String EDGE_CLASS = "floatingTextFieldEdge";
    private boolean isEdge = false;
    FloatingTextFieldWidget widget;
    Element floatElement;
    VLabel placeholderLabel;
    Element parent;
    Element placeholderElement;
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
            isEdge = getState().isEdge;
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
        placeholderLabel.getElement().removeClassName(phErrorText);
        placeholderLabel.getElement().removeClassName(phError);

        if (widget.getStyleName().contains(V_ERROR_CLASS)) {
            if ((currentFocus) || (!widget.getText().isEmpty())) {
                placeholderLabel.getElement().addClassName(phErrorText);
            } else if (widget.getText().isEmpty()) {
                placeholderLabel.getElement().addClassName(phError);
            }
        }
        if (currentFocus) {
            floatElement.focus();
        } else {
            floatElement.blur();
        }
    }

    private void addPlaceholder() {
        placeholder = getState().floatingPlaceholder;
        VLabel parentContainer = new VLabel();
        parentContainer.addStyleName("floatingTextFieldWidget");
        parent = parentContainer.getElement();
        placeholderElement = DOM.createDiv();
        placeholderLabel = new VLabel();
        widget.getElement().addClassName(CLASS_NAME);
        parent.addClassName("v-widget " + CLASS_NAME + "-widget");
        placeholderElement.setAttribute("class", CLASS_NAME + "-frame");

        widget.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                fieldFocus = true;
                placeholderLabel.getElement().addClassName(phFocus);
                if (placeholderLabel.getStyleName().contains(phError)) {
                    placeholderLabel.getElement().removeClassName(phError);
                    placeholderLabel.getElement().addClassName(phErrorText);
                }
            }

        });
        widget.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                fieldFocus = false;
                if (widget.getText() != null && !widget.getText().isEmpty()) {
                    return;
                } else if (widget.getStyleName().contains(V_ERROR_CLASS)) {
                    placeholderLabel.getElement().addClassName(phError);
                }
                placeholderLabel.getElement().removeClassName(phFocus);
            }

        });

        placeholderLabel.getElement().setClassName(phLabel);
        if (isEdge) {
            parent.addClassName(EDGE_CLASS);
        }
        placeholderLabel.setText(placeholder);
        placeholderElement.appendChild(placeholderLabel.getElement());
        DOM.sinkEvents(placeholderLabel.getElement(),
                Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
        DOM.setEventListener(placeholderLabel.getElement(),
                new EventListener() {
                    @Override
                    public void onBrowserEvent(Event event) {
                        if (Event.ONCLICK == event.getTypeInt()) {
                            if (fieldFocus) {
                                widget.getElement().blur();
                            } else {
                                widget.getElement().focus();
                            }
                        }
                    }
                });
        parent.appendChild(placeholderElement);
    }

    private static native void alert(String s) /*-{
                                               alert(s);
                                               }-*/;
}
