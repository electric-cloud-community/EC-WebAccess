
// AccessURLParameterPanel.java --
//
// AccessURLParameterPanel.java is part of ElectricCommander.
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.electriccloud.commander.client.domain.ActualParameter;
import com.electriccloud.commander.client.domain.FormalParameter;
import com.electriccloud.commander.client.util.StringUtil;
import com.electriccloud.commander.gwt.client.ComponentBase;
import com.electriccloud.commander.gwt.client.ui.CustomValueCheckBox;
import com.electriccloud.commander.gwt.client.ui.FormBuilder;
import com.electriccloud.commander.gwt.client.ui.ParameterPanel;
import com.electriccloud.commander.gwt.client.ui.ParameterPanelProvider;

/**
 * Basic component that is meant to be cloned and then customized to perform a
 * real function.
 */
public class AccessURLParameterPanel
    extends ComponentBase
    implements ParameterPanel,
        ParameterPanelProvider
{

    //~ Static fields/initializers ---------------------------------------------

    // ~ Static fields/initializers----------------------------
    private static UiBinder<Widget, AccessURLParameterPanel> s_binder = GWT
            .create(Binder.class);

    // These are all the formalParameters on the Procedure
    static final String URL              = "url";
    static final String GETHTML          = "getHtml";
    static final String CHECKCREDENTIALS = "checkCredentials";
    static final String USERNAME         = "username";
    static final String PASSWORD         = "password";
    static final String CHECKTIME        = "checkTime";
    static final String HTML_OUTPP       = "html_outpp";

    //~ Instance fields --------------------------------------------------------

    // ~ Instance fields
    // --------------------------------------------------------
    @UiField FormBuilder accessParameterForm;

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
        Widget                    base             = s_binder.createAndBindUi(
                this);
        final CustomValueCheckBox getHtml          = getUIFactory()
                .createCustomValueCheckBox("1", "0");
        final CustomValueCheckBox checkCredentials = getUIFactory()
                .createCustomValueCheckBox("1", "0");
        final CustomValueCheckBox checkTime        = getUIFactory()
                .createCustomValueCheckBox("1", "0");

        accessParameterForm.addRow(true, "URL:",
            "Provide the URL required for the access.", URL, "", new TextBox());
        accessParameterForm.addRow(false, "Get HTML:",
            "Gets the HTML from the given URL, if the server allows it.",
            GETHTML, "", getHtml);
        accessParameterForm.addRow(false, "Credentials Required:",
            "In case credentials are required to access the URL.",
            CHECKCREDENTIALS, "", checkCredentials);
        accessParameterForm.addRow(false, "Username:",
            "Provide a valid username.", USERNAME, "", new TextBox());
        accessParameterForm.addRow(false, "Password:",
            "Provide the valid password for your user name.", PASSWORD, "",
            new PasswordTextBox());
        accessParameterForm.addRow(false, "Check Time:",
            "Calculates the response time since the request is sent, until all the information reaches the client.",
            CHECKTIME, "", checkTime);
        accessParameterForm.addRow(false, "HTML (output property path):",
            "Provide a name to create an Electric Commander property, to store the server's response.",
            HTML_OUTPP, "", new TextBox());
        checkCredentials.addValueChangeHandler(
            new ValueChangeHandler<String>() {
                @Override public void onValueChange(
                        ValueChangeEvent<String> event)
                {
                    updateRowVisibility();
                }
            });
        getHtml.addValueChangeHandler(new ValueChangeHandler<String>() {
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
        boolean validationStatus = accessParameterForm.validate();
        String  checkCredent     = accessParameterForm.getValue(
                CHECKCREDENTIALS);

        if (checkCredent == "1") {

            if (StringUtil.isEmpty(accessParameterForm.getValue(USERNAME))) {
                accessParameterForm.setErrorMessage(USERNAME,
                    "This Field is required.");
                validationStatus = false;
            }

            if (StringUtil.isEmpty(
                        accessParameterForm.getValue(PASSWORD)
                                       .trim())) {
                accessParameterForm.setErrorMessage(PASSWORD,
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
        String getH = accessParameterForm.getValue(GETHTML);

        if (getH.equals("1")) {
            accessParameterForm.setRowVisible(HTML_OUTPP, true);
        }
        else {
            accessParameterForm.setRowVisible(HTML_OUTPP, false);
        }

        String checkCredent = accessParameterForm.getValue(CHECKCREDENTIALS);

        if (checkCredent.equals("1")) {
            accessParameterForm.setRowVisible(USERNAME, true);
            accessParameterForm.setRowVisible(PASSWORD, true);
        }
        else {
            accessParameterForm.setRowVisible(USERNAME, false);
            accessParameterForm.setRowVisible(PASSWORD, false);
        }
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
        Map<String, String> searchFormValues = accessParameterForm.getValues();

        actualParams.put(URL, searchFormValues.get(URL));
        actualParams.put(GETHTML, searchFormValues.get(GETHTML));
        actualParams.put(CHECKCREDENTIALS,
            searchFormValues.get(CHECKCREDENTIALS));
        actualParams.put(USERNAME, searchFormValues.get(USERNAME));
        actualParams.put(PASSWORD, searchFormValues.get(PASSWORD));
        actualParams.put(CHECKTIME, searchFormValues.get(CHECKTIME));
        actualParams.put(HTML_OUTPP, searchFormValues.get(HTML_OUTPP));

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
                    URL,
                    GETHTML,
                    CHECKCREDENTIALS,
                    USERNAME,
                    PASSWORD,
                    CHECKTIME,
                    HTML_OUTPP
                }) {
            accessParameterForm.setValue(key,
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
        extends UiBinder<Widget, AccessURLParameterPanel> { }
}
