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

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ScmOptionBlockTest {

    private String expectedMessage;
    private ScmOptionBlock scmOptionBlock;

    public ScmOptionBlockTest(String scm, String expectedMessage) {
        this.expectedMessage = expectedMessage;
        this.scmOptionBlock = new ScmOptionBlock(scm);
    }

    @Parameters
    public static Collection<Object[]> data() {
        final String pass = "Pass";
        return Arrays.asList(
                new Object[] { "none", pass }
                , new Object[] { "accurev",
                        "[SCM] Please specify AccuRev's source control repository under 'Advanced' \n" }
                , new Object[] { "clearcase", pass }
                , new Object[] { "cvs", pass }
                , new Object[] { "git", pass }
                , new Object[] { "hg", pass }
                , new Object[] { "tfs2008", pass }
                , new Object[] { "tfs2010", pass }
                , new Object[] { "tfs2012", pass }
                , new Object[] { "tfs2013", pass }
                , new Object[] { "tfs2015", pass }
                , new Object[] { "tfs2017", pass }
                , new Object[] { "plastic", pass }
                , new Object[] { "plastic-distributed", pass }
                , new Object[] { "perforce",
                        "[SCM] Please specify Perforce's port environment variable under 'Advanced'\n " }
                , new Object[] { "perforce2009", pass }
        );
    }

    @Test
    public void checkScmConfigTest() {
        Assert.assertEquals(expectedMessage, scmOptionBlock.checkScmConfig());
    }
}
