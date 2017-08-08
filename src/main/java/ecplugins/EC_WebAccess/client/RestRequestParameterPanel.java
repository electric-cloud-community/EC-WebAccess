
// RestRequestParameterPanel.java --
//
// RestRequestParameterPanel.java is part of ElectricCommander.
//
// Copyright (c) 2005-2012 Electric Cloud, Inc.
// All rights reserved.
//

package ecplugins.EC_WebAccess.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.electriccloud.commander.client.domain.ActualParameter;
import com.electriccloud.commander.client.domain.FormalParameter;
import com.electriccloud.commander.client.util.StringUtil;
import com.electriccloud.commander.gwt.client.ComponentBase;
import com.electriccloud.commander.gwt.client.ui.FormBuilder;
import com.electriccloud.commander.gwt.client.ui.ParameterPanel;
import com.electriccloud.commander.gwt.client.ui.ParameterPanelProvider;
import com.electriccloud.commander.gwt.client.ui.ValuedListBox;

/**
 * Basic component that is meant to be cloned and then customized to perform a
 * real function.
 */
public class RestRequestParameterPanel
    extends ComponentBase
    implements ParameterPanel,
        ParameterPanelProvider
{

    //~ Static fields/initializers ---------------------------------------------

    // ~ Static fields/initializers----------------------------
    private static UiBinder<Widget, RestRequestParameterPanel> s_binder = GWT
            .create(Binder.class);

    // These are all the formalParameters on the Procedure
    static final String BASEURL        = "baseUrl";
    static final String PATHURL        = "pathUrl";
    static final String PORT           = "port";
    static final String CONTENTTYPE    = "contentType";
    static final String FORMCONTENT    = "formContent";
    static final String HEADERS        = "headers";
    static final String USERNAME       = "username";
    static final String PASSWORD       = "password";
    static final String REQUESTTYPE    = "requestType";
    static final String AUTHENTICATION = "authentication";
    static final String RESPONSE_OUTPP = "response_outpp";

    //~ Instance fields --------------------------------------------------------

    // ~ Instance fields
    // --------------------------------------------------------
    @UiField FormBuilder restParameterForm;

    //~ Methods ----------------------------------------------------------------

    /**
     * This function is called by SDK infrastructure to initialize the UI parts
     * of this component.
     *
     * @return  A widget that the infrastructure should place in the UI; usually
     *          a panel.
     */
    @Override public Widget doInit()
    {
        Widget              base        = s_binder.createAndBindUi(this);
        final ValuedListBox requestType = getUIFactory().createValuedListBox();

        // Add items to listbox
        requestType.addItem("GET", "GET");
        requestType.addItem("POST", "POST");
        requestType.addItem("PUT", "PUT");
        requestType.addItem("DELETE", "DELETE");

        final ValuedListBox authentication = getUIFactory()
                .createValuedListBox();

        authentication.addItem("No Authentication", "none");
        authentication.addItem("Basic", "basic");
        restParameterForm.addRow(true, "URL Base:",
            "Provide the base URL required for the request.", BASEURL, "",
            new TextBox());
        restParameterForm.addRow(false, "Port:",
            "Defines the port number at the host.", PORT, "", new TextBox());
        restParameterForm.addRow(true, "Path URL:",
            "Provide the rest of the URL required to perform the HTTP request.",
            PATHURL, "", new TextBox());
        restParameterForm.addRow(false, "Content Type:",
            "Specifies the nature of the linked resource.", CONTENTTYPE, "",
            new TextBox());
        restParameterForm.addRow(false, "Headers:",
            "Provide the HTTP header fields required for the request. Remember to write 'Key' whitespace and then 'Value', If more than one header, write each header in separate lines.",
            HEADERS, "", new TextArea());
        restParameterForm.addRow(false, "Content:",
            "Provide the body required for the request.", FORMCONTENT, "",
            new TextArea());
        restParameterForm.addRow(true, "Authentication:",
            "Select the type of authentication.", AUTHENTICATION, "none",
            authentication);
        restParameterForm.addRow(false, "Username:",
            "Provide a valid user name.", USERNAME, "", new TextBox());
        restParameterForm.addRow(false, "Password:",
            "Provide the valid password for your user name.", PASSWORD, "",
            new PasswordTextBox());
        restParameterForm.addRow(true, "Request Type:",
            "Select the request type from the given options.", REQUESTTYPE,
            "GET", requestType);
        restParameterForm.addRow(false, "Response (output property path):",
            "Provide a name to create an Electric Commander property, to store the server's response.",
            RESPONSE_OUTPP, "", new TextBox());
        authentication.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override public void onValueChange(
                        ValueChangeEvent<String> event)
                {
                    updateRowVisibility();
                }
            });
        updateRowVisibility();

        return base;
    }

    /**
     * Performs validation of user supplied data before submitting the form.
     *
     * <p>This function is called after the user hits submit.</p>
     *
     * @return  true if checks succeed, false otherwise
     */
    @Override public boolean validate()
    {
        boolean validationStatus = restParameterForm.validate();
        String  auth             = restParameterForm.getValue(AUTHENTICATION);

        if ("basic".equals(auth)) {

            if (StringUtil.isEmpty(restParameterForm.getValue(USERNAME))) {
                restParameterForm.setErrorMessage(USERNAME,
                    "This Field is required.");
                validationStatus = false;
            }

            if (StringUtil.isEmpty(
                        restParameterForm.getValue(PASSWORD)
                                     .trim())) {
                restParameterForm.setErrorMessage(PASSWORD,
                    "This Field is required.");
                validationStatus = false;
            }
        }

        return validationStatus;
    }

    /**
     * This method is used by UIBinder to embed FormBuilder's in the UI.
     *
     * @return  a new FormBuilder.
     */
    @UiFactory FormBuilder createFormBuilder()
    {
        return getUIFactory().createFormBuilder();
    }

    private void updateRowVisibility()
    {
        String auth = restParameterForm.getValue(AUTHENTICATION);

        restParameterForm.setRowVisible(USERNAME, "basic".equals(auth));
        restParameterForm.setRowVisible(PASSWORD, "basic".equals(auth));
    }

    /**
     * Straight forward function usually just return this;
     *
     * @return  straight forward function usually just return this;
     */
    @Override public ParameterPanel getParameterPanel()
    {
        return this;
    }

    /**
     * Gets the values of the parameters that should map 1-to-1 to the formal
     * parameters on the object being called. Transform user input into a map of
     * parameter names and values.
     *
     * <p>This function is called after the user hits submit and validation has
     * succeeded.</p>
     *
     * @return  The values of the parameters that should map 1-to-1 to the
     *          formal parameters on the object being called.
     */
    @Override public Map<String, String> getValues()
    {
        Map<String, String> actualParams     = new HashMap<String, String>();
        Map<String, String> searchFormValues = restParameterForm.getValues();

        actualParams.put(BASEURL, searchFormValues.get(BASEURL));
        actualParams.put(PATHURL, searchFormValues.get(PATHURL));
        actualParams.put(PORT, searchFormValues.get(PORT));
        actualParams.put(CONTENTTYPE, searchFormValues.get(CONTENTTYPE));
        actualParams.put(FORMCONTENT, searchFormValues.get(FORMCONTENT));
        actualParams.put(HEADERS, searchFormValues.get(HEADERS));
        actualParams.put(USERNAME, searchFormValues.get(USERNAME));
        actualParams.put(PASSWORD, searchFormValues.get(PASSWORD));
        actualParams.put(REQUESTTYPE, searchFormValues.get(REQUESTTYPE));
        actualParams.put(AUTHENTICATION, searchFormValues.get(AUTHENTICATION));
        actualParams.put(RESPONSE_OUTPP, searchFormValues.get(RESPONSE_OUTPP));

        return actualParams;
    }

    /**
     * Push actual parameters into the panel implementation.
     *
     * <p>This is used when editing an existing object to show existing content.
     * </p>
     *
     * @param  actualParameters  Actual parameters assigned to this list of
     *                           parameters.
     */
    @Override public void setActualParameters(
            Collection<ActualParameter> actualParameters)
    {

        // Store actual params into a hash for easy retrieval later
        if (actualParameters == null) {
            return;
        }

        // First load the parameters into a map. Makes it easier to
        // update the form by querying for various params randomly.
        Map<String, String> params = new HashMap<String, String>();

        for (ActualParameter p : actualParameters) {
            params.put(p.getName(), p.getValue());
        }

        // Do the easy form elements first.
        for (String key : new String[] {
                    BASEURL,
                    PATHURL,
                    PORT,
                    CONTENTTYPE,
                    FORMCONTENT,
                    HEADERS,
                    USERNAME,
                    PASSWORD,
                    REQUESTTYPE,
                    AUTHENTICATION,
                    RESPONSE_OUTPP
                }) {
            restParameterForm.setValue(key,
                StringUtil.nullToEmpty(params.get(key)));
        }

        updateRowVisibility();
    }

    /**
     * Push form parameters into the panel implementation.
     *
     * <p>This is used when creating a new object and showing default values.
     * </p>
     *
     * @param  formalParameters  Formal parameters on the target object.
     */
    @Override public void setFormalParameters(
            Collection<FormalParameter> formalParameters) { }

    //~ Inner Interfaces -------------------------------------------------------

    interface Binder
        extends UiBinder<Widget, RestRequestParameterPanel> { }
}
