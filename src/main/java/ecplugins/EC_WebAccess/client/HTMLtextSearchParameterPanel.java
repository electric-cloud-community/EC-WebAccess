
// HTMLtextSearchParameterPanel.java --
//
// HTMLtextSearchParameterPanel.java is part of ElectricCommander.
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
public class HTMLtextSearchParameterPanel
    extends ComponentBase
    implements ParameterPanel,
        ParameterPanelProvider
{

    //~ Static fields/initializers ---------------------------------------------

    // ~ Static fields/initializers----------------------------
    private static UiBinder<Widget, HTMLtextSearchParameterPanel> s_binder = GWT
            .create(Binder.class);

    // These are all the formalParameters on the Procedure
    static final String SEARCHOP   = "searchOp";
    static final String LOCAL      = "local";
    static final String URL        = "url";
    static final String HTMLTEXT   = "htmlText";
    static final String SEARCHTYPE = "searchType";
    static final String TEXT       = "text";
    static final String TAG        = "tag";

    //~ Instance fields --------------------------------------------------------

    // ~ Instance fields
    // --------------------------------------------------------
    @UiField FormBuilder searchParameterForm;

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
        Widget              base     = s_binder.createAndBindUi(this);
        final ValuedListBox searchOp = getUIFactory().createValuedListBox();
// Add items to listbox

        searchOp.addItem("URL", "urlOp");
        searchOp.addItem("Local File", "localOp");
        searchOp.addItem("HTML Text", "htmlTextOp");

        final ValuedListBox searchType = getUIFactory().createValuedListBox();

        searchType.addItem("Plain Text", "textOp");
        searchType.addItem("In Tags", "tagOp");
        searchParameterForm.addRow(true, "Search Option:",
            "Provide the search option, where the HTML text will be obtained",
            SEARCHOP, "urlOp", searchOp);
        searchParameterForm.addRow(false, "URL:", "Provide an absolute URL.",
            URL, "", new TextBox());
        searchParameterForm.addRow(false, "Local file path:",
            "Local path to an HTML file.", LOCAL, "", new TextBox());
        searchParameterForm.addRow(false, "HTML Text:",
            "Provide directly HTML text.", HTMLTEXT, "", new TextArea());
        searchParameterForm.addRow(true, "Search Type:",
            "Provide the search type, by plain text or by tags.", SEARCHTYPE,
            "textOp", searchType);
        searchParameterForm.addRow(false, "Text:",
            "Search for plain text on the HTML.", TEXT, "", new TextBox());
        searchParameterForm.addRow(false, "Tag:",
            "Search for all the tags of a kind.", TAG, "", new TextBox());
        searchOp.addValueChangeHandler(new ValueChangeHandler<String>() {
                @Override public void onValueChange(
                        ValueChangeEvent<String> event)
                {
                    updateRowVisibility();
                }
            });
        searchType.addValueChangeHandler(new ValueChangeHandler<String>() {
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
        boolean validationStatus = searchParameterForm.validate();
        String  searchop         = searchParameterForm.getValue(SEARCHOP);
        String  searchtype       = searchParameterForm.getValue(SEARCHTYPE);

        if ("urlOp".equals(searchop)) {

            if (StringUtil.isEmpty(searchParameterForm.getValue(URL))) {
                searchParameterForm.setErrorMessage(URL,
                    "This Field is required.");
                validationStatus = false;
            }
        }
        else if ("localOp".equals(searchop)) {

            if (StringUtil.isEmpty(
                        searchParameterForm.getValue(LOCAL)
                                       .trim())) {
                searchParameterForm.setErrorMessage(LOCAL,
                    "This Field is required.");
                validationStatus = false;
            }
        }
        else {

            if (StringUtil.isEmpty(
                        searchParameterForm.getValue(HTMLTEXT)
                                       .trim())) {
                searchParameterForm.setErrorMessage(HTMLTEXT,
                    "This Field is required.");
                validationStatus = false;
            }
        }

        if ("textOp".equals(searchtype)) {

            if (StringUtil.isEmpty(searchParameterForm.getValue(TEXT)
                                                      .trim())) {
                searchParameterForm.setErrorMessage(TEXT,
                    "This Field is required.");
                validationStatus = false;
            }
        }
        else {

            if (StringUtil.isEmpty(searchParameterForm.getValue(TAG)
                                                      .trim())) {
                searchParameterForm.setErrorMessage(TAG,
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
        String searchoption = searchParameterForm.getValue(SEARCHOP);
        String searchtype   = searchParameterForm.getValue(SEARCHTYPE);

        searchParameterForm.setRowVisible(URL, "urlOp".equals(searchoption));
        searchParameterForm.setRowVisible(LOCAL,
            "localOp".equals(searchoption));
        searchParameterForm.setRowVisible(HTMLTEXT,
            "htmlTextOp".equals(searchoption));
        searchParameterForm.setRowVisible(TEXT, "textOp".equals(searchtype));
        searchParameterForm.setRowVisible(TAG, "tagOp".equals(searchtype));
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
        Map<String, String> searchFormValues = searchParameterForm.getValues();

        actualParams.put(SEARCHOP, searchFormValues.get(SEARCHOP));
        actualParams.put(URL, searchFormValues.get(URL));
        actualParams.put(LOCAL, searchFormValues.get(LOCAL));
        actualParams.put(HTMLTEXT, searchFormValues.get(HTMLTEXT));
        actualParams.put(SEARCHTYPE, searchFormValues.get(SEARCHTYPE));
        actualParams.put(TEXT, searchFormValues.get(TEXT));
        actualParams.put(TAG, searchFormValues.get(TAG));

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
                    SEARCHOP,
                    URL,
                    LOCAL,
                    HTMLTEXT,
                    SEARCHTYPE,
                    TEXT,
                    TAG
                }) {
            searchParameterForm.setValue(key,
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
        extends UiBinder<Widget, HTMLtextSearchParameterPanel> { }
}
