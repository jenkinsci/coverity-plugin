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
package jenkins.plugins.coverity.Utils;

import jenkins.plugins.coverity.ScmOptionBlock;

public class ScmOptionBlockBuilder {

    private String scmSystem;
    private String customTestTool;
    private String scmToolArguments;
    private String scmCommandArgs;
    private String logFileLoc;
    private String p4Port;
    private String accRevRepo;
    private String scmAdditionalCmd;
    private String fileRegex;

    public ScmOptionBlockBuilder withScmSystem(String scmSystem) {
        this.scmSystem = scmSystem;
        return this;
    }

    public ScmOptionBlockBuilder withCustomTestTool(String customTestTool) {
        this.customTestTool = customTestTool;
        return this;
    }

    public ScmOptionBlockBuilder withScmToolArguments(String scmToolArguments) {
        this.scmToolArguments = scmToolArguments;
        return this;
    }

    public ScmOptionBlockBuilder withScmCommandArgs(String scmCommandArgs) {
        this.scmCommandArgs = scmCommandArgs;
        return this;
    }

    public ScmOptionBlockBuilder withLogFileLoc(String logFileLoc) {
        this.logFileLoc = logFileLoc;
        return this;
    }

    public ScmOptionBlockBuilder withP4Port(String p4Port) {
        this.p4Port = p4Port;
        return this;
    }

    public ScmOptionBlockBuilder withAccRevRepo(String accRevRepo) {
        this.accRevRepo = accRevRepo;
        return this;
    }

    public ScmOptionBlockBuilder withScmAdditionalCmd(String scmAdditionalCmd) {
        this.scmAdditionalCmd = scmAdditionalCmd;
        return this;
    }

    public ScmOptionBlockBuilder withFileRegex(String fileRegex) {
        this.fileRegex = fileRegex;
        return this;
    }

    public ScmOptionBlock build() {
        ScmOptionBlock scmOptionBlock = new ScmOptionBlock(scmSystem);
        scmOptionBlock.setCustomTestTool(customTestTool);
        scmOptionBlock.setScmToolArguments(scmToolArguments);
        scmOptionBlock.setScmCommandArgs(scmCommandArgs);
        scmOptionBlock.setLogFileLoc(logFileLoc);
        scmOptionBlock.setP4Port(p4Port);
        scmOptionBlock.setAccRevRepo(accRevRepo);
        scmOptionBlock.setScmAdditionalCmd(scmAdditionalCmd);
        scmOptionBlock.setFileRegex(fileRegex);

        return scmOptionBlock;
    }
}
