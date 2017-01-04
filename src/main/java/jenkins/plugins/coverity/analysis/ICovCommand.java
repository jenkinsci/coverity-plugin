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
package jenkins.plugins.coverity.analysis;

import java.io.IOException;
import java.util.List;

public interface ICovCommand {
    /*
    This interface will run coverity tool commands with arguments
     */

    int runCommand() throws IOException, InterruptedException;

    List<String> getCommandLines();
}
