package org.vaadin.addons.floating_textfield.client;

import org.vaadin.addons.floating_textfield.FloatingTextField;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.textfield.TextFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(FloatingTextField.class)
public class FloatingTextFieldConnector extends TextFieldConnector {
    private static final String V_ERROR_CLASS = "v-textfield-error";
    private static final String V_FOCUS_CLASS = "v-textfield-focus";
    private String widgetFocus = "floatingTextFieldWidgetFocused";
    private String phLabel = "placeholderSpan";
    private String widgetError = "floatingTextFieldWidgetError";
    FloatingTextFieldWidget widget;
    Element parentContainer;
    Element widgetElement;
    Element placeholderSpan;
    Element placeholderSpanContent;
    String placeholder;
    FloatingTextFieldServerRpc rpc = RpcProxy
            .create(FloatingTextFieldServerRpc.class, this);

    public FloatingTextFieldConnector() {
        widget = getWidget();
        widgetElement = widget.getElement();
        if (!widgetElement.getClassName().contains("floatingTextField")) {
            addFocusHandler(widgetElement);
        }
        widgetElement.addClassName("floatingTextField");

        parentContainer = DOM.createDiv();
        placeholderSpan = DOM.createSpan();
        placeholderSpanContent = DOM.createSpan();

        placeholderSpan.addClassName("placeholderSpan");
        parentContainer.addClassName("v-widget floatingTextFieldWidget");
        placeholderSpanContent.setClassName("placeholder");

        parentContainer.appendChild(placeholderSpan);
        placeholderSpan.setClassName(phLabel);
        placeholderSpan.appendChild(placeholderSpanContent);

        if (hasFocusOrText(widget.getElement())) {
            parentContainer.addClassName(widgetFocus);
        }
        if (getWidget().getStyleName().contains(V_ERROR_CLASS)) {
            parentContainer.addClassName(widgetError);
        }
        parentContainer.getStyle().setProperty("width", getState().width);
        parentContainer.getStyle().setProperty("height", getState().height);
        placeholderSpan.getStyle().setProperty("height", getState().height);
        placeholderSpanContent.getStyle().setProperty("max-width",
                "calc(" + getState().width + " - 5px)");
    }

    @Override
    public FloatingTextFieldWidget getWidget() {
        return (FloatingTextFieldWidget) super.getWidget();
    }

    @Override
    public FloatingTextFieldState getState() {
        return (FloatingTextFieldState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        if (hasFocusOrText(widget.getElement())) {
            parentContainer.addClassName(widgetFocus);
        }

        if (widget.getStyleName().contains(V_ERROR_CLASS)) {
            parentContainer.addClassName(widgetError);
        } else {
            parentContainer.removeClassName(widgetError);
        }

        if (stateChangeEvent.hasPropertyChanged("height")
                && getState().height != null && !getState().height.isEmpty()) {
            parentContainer.getStyle().setProperty("height", getState().height);
            placeholderSpan.getStyle().setProperty("height", getState().height);
        }
        if (stateChangeEvent.hasPropertyChanged("width")
                && getState().width != null && !getState().width.isEmpty()) {
            parentContainer.getStyle().setProperty("width", getState().width);
            placeholderSpanContent.getStyle().setProperty("max-width",
                    "calc(" + getState().width + " - 5px)");
        }

        Element previousParent = widgetElement.getParentElement();
        if (!previousParent.isOrHasChild(parentContainer)) {
            previousParent.replaceChild(parentContainer, widgetElement);
            parentContainer.appendChild(widgetElement);
        }
        placeholder = getState().floatingPlaceholder;
        if (placeholder != null) {
            placeholderSpanContent.setInnerText(placeholder);
        }
    }

    private void focused() {
        parentContainer.addClassName(widgetFocus);
    }

    private void blurred() {
        if (hasFocusOrText(widget.getElement())) {
            return;
        }
        parentContainer.removeClassName(widgetFocus);
    }

    public final native void addFocusHandler(Element el)
    /*-{
        var self = this;
        el.addEventListener("focus", function () {
            self.@org.vaadin.addons.floating_textfield.client.FloatingTextFieldConnector::focused()();
        });
        el.addEventListener("blur", function () {
              self.@org.vaadin.addons.floating_textfield.client.FloatingTextFieldConnector::blurred()();
        });
    }-*/;

    public final native boolean hasFocusOrText(Element el)
    /*-{
        var s = el.value;
        if (Boolean(s)) {
            return true;
        }
        if (el === document.activeElement) {
            return true;
        }
        return false;
    }-*/;
}