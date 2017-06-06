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
package jenkins.plugins.coverity.Utils;

import jenkins.plugins.coverity.CIMStream;
import jenkins.plugins.coverity.CoverityPublisher;
import jenkins.plugins.coverity.InvocationAssistance;
import jenkins.plugins.coverity.ScmOptionBlock;
import jenkins.plugins.coverity.TaOptionBlock;

public class CoverityPublisherBuilder {

    private CIMStream cimStream;
    private InvocationAssistance invocationAssistance;
    private boolean failBuild;
    private boolean unstable;
    private boolean keepIntDir;
    private boolean skipFetchingDefects;
    private boolean hideChart;
    private TaOptionBlock taOptionBlock;
    private ScmOptionBlock scmOptionBlock;

    public CoverityPublisherBuilder withCimStream(CIMStream stream) {
        this.cimStream = stream;
        return this;
    }

    public CoverityPublisherBuilder withInvocationAssistance(InvocationAssistance invocationAssistance) {
        this.invocationAssistance = invocationAssistance;
        return this;
    }

    public CoverityPublisherBuilder withFailBuild(boolean failBuild) {
        this.failBuild = failBuild;
        return this;
    }

    public CoverityPublisherBuilder withUnstableBuild(boolean unstable) {
        this.unstable = unstable;
        return this;
    }

    public CoverityPublisherBuilder withKeepIntDir(boolean keepIntDir) {
        this.keepIntDir = keepIntDir;
        return this;
    }

    public CoverityPublisherBuilder withSkipFetchingDefects(boolean skipFetchingDefects) {
        this.skipFetchingDefects = skipFetchingDefects;
        return this;
    }

    public CoverityPublisherBuilder withHideChart(boolean hideChart) {
        this.hideChart = hideChart;
        return this;
    }

    public CoverityPublisherBuilder withTaOptionBlock(TaOptionBlock taOptionBlock) {
        this.taOptionBlock = taOptionBlock;
        return this;
    }

    public CoverityPublisherBuilder withScmOptionBlock(ScmOptionBlock scmOptionBlock) {
        this.scmOptionBlock = scmOptionBlock;
        return this;
    }

    public CoverityPublisher build() {
        return new CoverityPublisher(
                cimStream,
                invocationAssistance,
                failBuild,
                unstable,
                keepIntDir,
                skipFetchingDefects,
                hideChart,
                taOptionBlock,
                scmOptionBlock);
    }
}
