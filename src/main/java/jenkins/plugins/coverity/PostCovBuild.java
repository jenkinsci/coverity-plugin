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

public class PostCovBuild {
    private final String postCovBuildCmd;

    @DataBoundConstructor
    public PostCovBuild(String postCovBuildCmd) {
        this.postCovBuildCmd = postCovBuildCmd;
    }

    public String getPostCovBuildCmd() {
        return postCovBuildCmd;
    }
}
