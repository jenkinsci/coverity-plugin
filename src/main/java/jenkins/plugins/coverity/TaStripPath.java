/*******************************************************************************
 * Copyright (c) 2017 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.Util;
import org.kohsuke.stapler.DataBoundConstructor;

public class TaStripPath {

    private String taStripPath;

    public String getTaStripPath() { return this.taStripPath; }

    @DataBoundConstructor
    public TaStripPath(String taStripPath) {
        this.taStripPath = Util.fixEmpty(taStripPath);
    }
}
