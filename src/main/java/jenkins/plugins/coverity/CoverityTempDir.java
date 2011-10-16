/*******************************************************************************
 * Copyright (c) 2011 Coverity, Inc

 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Huybrechts - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity;

import hudson.FilePath;
import hudson.model.InvisibleAction;

/**
 * Created by CoverityLauncherDecorator to store the location of the temporary directory for Coverity for a certain build
 */
class CoverityTempDir extends InvisibleAction {
    transient final FilePath tempDir;

    CoverityTempDir(FilePath tempDir) {
        this.tempDir = tempDir;
    }
}
