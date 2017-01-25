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

public class CoverityDefect {
    private final Long cid;
    private final String checkerName;
    private final String functionDisplayName;
    private final String filePathname;

    public CoverityDefect(Long cid, String checkerName, String functionDisplayName, String filePathname) {
        this.cid = cid;
        this.checkerName = checkerName;
        this.functionDisplayName = functionDisplayName;
        this.filePathname = filePathname;
    }

    public Long getCid() {
        return cid;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public String getFunctionDisplayName() {
        return functionDisplayName;
    }

    public String getFilePathname() {
        return filePathname;
    }
}
