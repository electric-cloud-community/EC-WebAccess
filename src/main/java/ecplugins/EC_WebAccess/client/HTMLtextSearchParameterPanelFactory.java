
// HTMLtextSearchParameterPanelFactory.java --
//
// HTMLtextSearchParameterPanelFactory.java is part of ElectricCommander.
//
// Copyright (c) 2005-2012 Electric Cloud, Inc.
// All rights reserved.
//

package ecplugins.EC_WebAccess.client;

import com.electriccloud.commander.gwt.client.Component;
import com.electriccloud.commander.gwt.client.ComponentBaseFactory;
import com.electriccloud.commander.gwt.client.ComponentContext;
import org.jetbrains.annotations.NotNull;

/**
 * This factory is responsible for providing instances of the
 * HTMLtextSearchParameterPanel class.
 */
public class HTMLtextSearchParameterPanelFactory
    extends ComponentBaseFactory
{

    //~ Methods ----------------------------------------------------------------

    @NotNull
    @Override public Component createComponent(ComponentContext jso)
    {
        return new HTMLtextSearchParameterPanel();
    }
}
