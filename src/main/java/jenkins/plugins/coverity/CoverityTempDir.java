/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Coverity, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.FilePath;
import hudson.model.InvisibleAction;

/**
 * Created by CoverityLauncherDecorator to store the location of the temporary directory for Coverity for a certain
 * build
 */
public class CoverityTempDir extends InvisibleAction {
    transient final FilePath tempDir;
    transient final boolean def;

    public CoverityTempDir(FilePath tempDir, boolean def) {
        this.tempDir = tempDir;
        this.def = def;
    }

    public FilePath getTempDir() {
        return tempDir;
    }

    public boolean isDef() {
        return def;
    }
}
