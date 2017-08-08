
// AccessURLParameterPanelFactory.java --
//
// AccessURLParameterPanelFactory.java is part of ElectricCommander.
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
 * AccessURLParameterPanel class.
 */
public class AccessURLParameterPanelFactory
    extends ComponentBaseFactory
{

    //~ Methods ----------------------------------------------------------------

    @NotNull
    @Override public Component createComponent(ComponentContext jso)
    {
        return new AccessURLParameterPanel();
    }
}
