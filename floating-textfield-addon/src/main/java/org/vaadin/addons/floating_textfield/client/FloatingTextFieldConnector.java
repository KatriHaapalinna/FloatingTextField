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
    private static final String ERROR_CLASS = "v-textfield-error";
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
        placeholderLabel.getElement().removeClassName("placeholder-error-text");
        placeholderLabel.getElement().removeClassName("error");

        if (widget.getStyleName().contains(ERROR_CLASS)) {
            if ((currentFocus) || (!widget.getText().isEmpty())) {
                placeholderLabel.getElement().addClassName("placeholder-error-text");
            } else if (widget.getText().isEmpty()) {
                placeholderLabel.getElement().addClassName("placeholder-error");
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
        parent = DOM.createDiv();
        placeholderElement = DOM.createDiv();
        placeholderLabel = new VLabel();
        widget.getElement().addClassName(CLASS_NAME);
        parent.addClassName("v-widget " + CLASS_NAME + "-widget");
        placeholderElement.setAttribute("class", CLASS_NAME + "-frame");

        widget.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                fieldFocus = true;
                placeholderLabel.getElement()
                        .addClassName(CLASS_NAME + "-placeholder-focus");
                if (placeholderLabel.getStyleName().contains("placeholder-error")) {
                    placeholderLabel.getElement().removeClassName("placeholder-error");
                    placeholderLabel.getElement().addClassName("placeholder-error-text");
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
                    placeholderLabel.getElement().addClassName("placeholder-error");
                }
                placeholderLabel.getElement()
                        .removeClassName(CLASS_NAME + "-placeholder-focus");
            }

        });
        
        placeholderLabel.getElement().setClassName(CLASS_NAME + "-placeholder");
        placeholderLabel.setText(placeholder);
        placeholderElement.appendChild(placeholderLabel.getElement());
        DOM.sinkEvents(placeholderLabel.getElement(), Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
        DOM.setEventListener(placeholderLabel.getElement(), new EventListener() {
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
