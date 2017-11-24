package org.vaadin.addons.floating_textfield.client;

import org.vaadin.addons.floating_textfield.FloatingTextField;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
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
    String contentText;
    boolean hasFocusOrContent = false;
    FloatingTextFieldServerRpc rpc = RpcProxy
            .create(FloatingTextFieldServerRpc.class, this);

    public FloatingTextFieldConnector() {
        widget = getWidget();
        parentContainer = DOM.createDiv();
        placeholderSpan = DOM.createSpan();
        placeholderSpanContent = DOM.createSpan();
        placeholderSpan.addClassName("placeholderSpan");
        parentContainer.addClassName("v-widget floatingTextFieldWidget");
        parentContainer.appendChild(placeholderSpan);
        placeholderSpan.setClassName(phLabel);
        placeholderSpanContent.setClassName("placeholder");
        placeholderSpan.appendChild(placeholderSpanContent);
        if ((getWidget().getText() != null && !getWidget().getText().isEmpty())
                || getWidget().getElement().getClassName()
                        .contains(V_FOCUS_CLASS)) {
            hasFocusOrContent = true;
            parentContainer.addClassName(widgetFocus);
        }

        widget.addFocusHandler(new FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                parentContainer.addClassName(widgetFocus);
                hasFocusOrContent = true;
            }

        });
        widget.addBlurHandler(new BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                if (hasFocusOrContent) {
                    if (getWidget().getText() != null
                            && !getWidget().getText().isEmpty()) {
                        hasFocusOrContent = true;
                        return;
                    } else if (getWidget().getStyleName()
                            .contains(V_ERROR_CLASS)) {
                        parentContainer.addClassName(widgetError);
                    }
                    hasFocusOrContent = false;
                    parentContainer.removeClassName(widgetFocus);
                }
            }

        });
        parentContainer.getStyle().setProperty("width", getState().width);
        parentContainer.getStyle().setProperty("height", getState().height);
        placeholderSpan.getStyle().setProperty("height", getState().height);
        getWidget().getElement().getStyle().setProperty("padding-top",
                "calc(" + getState().height + " * 0.35)");

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
        if ((getWidget().getText() != null && !getWidget().getText().isEmpty())
                || getWidget().getElement().getClassName()
                        .contains(V_FOCUS_CLASS)) {
            hasFocusOrContent = true;
            parentContainer.addClassName(widgetFocus);
        }
        if (stateChangeEvent.hasPropertyChanged("height")
                && getState().height != null && !getState().height.isEmpty()) {
            parentContainer.getStyle().setProperty("height", getState().height);
            placeholderSpan.getStyle().setProperty("height", getState().height);
            getWidget().getElement().getStyle().setProperty("padding-top",
                    "calc(" + getState().height + " * 0.35)");
        }
        if (stateChangeEvent.hasPropertyChanged("width")
                && getState().width != null && !getState().width.isEmpty()) {
            parentContainer.getStyle().setProperty("width", getState().width);
        }
        placeholder = getState().floatingPlaceholder;
        if (placeholder == null) {
            return;
        } else {
            placeholderSpanContent.setInnerText(placeholder);
        }
        widget = getWidget();
        widgetElement = widget.getElement();
        widgetElement.addClassName("floatingTextField");

        contentText = widget.getText();

        Element previousParent = widgetElement.getParentElement();
        if (!previousParent.isOrHasChild(parentContainer)) {
            previousParent.replaceChild(parentContainer, widgetElement);
            parentContainer.appendChild(widgetElement);
        }
        parentContainer.removeClassName(widgetError);

        if (widget.getStyleName().contains(V_ERROR_CLASS)) {
            parentContainer.addClassName(widgetError);
        }
    }
}