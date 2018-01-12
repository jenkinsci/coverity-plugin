/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Specifies a Coverity static analysis tools override location for one publisher
 */
public class ToolsOverride {
    private final String toolInstallationName;

    private String toolsLocation;

    @DataBoundConstructor
    public ToolsOverride(String toolInstallationName) {
        this.toolInstallationName = toolInstallationName;
    }

    public String getToolInstallationName() {
        return toolInstallationName;
    }

    public String getToolsLocation() {
        return toolsLocation;
    }

    @DataBoundSetter
    public void setToolsLocation(String toolsLocation) {
        this.toolsLocation = toolsLocation;
    }
}
