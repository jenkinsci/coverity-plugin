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

import hudson.model.Descriptor;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Publisher;
import hudson.util.DescribableList;
import jenkins.plugins.coverity.CoverityPublisher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtils {
    public static FreeStyleBuild getFreeStyleBuild(CoverityPublisher... publishers) {
        final FreeStyleBuild freeStyleBuild = mock(FreeStyleBuild.class);
        final FreeStyleProject freeStyleProject = mock(FreeStyleProject.class);
        final List<CoverityPublisher> coverityPublishers = publishers != null ? Arrays.asList(publishers) : Collections.<CoverityPublisher>emptyList();
        final DescribableList<Publisher, Descriptor<Publisher>> publisherList = new DescribableList<Publisher, Descriptor<Publisher>>(freeStyleProject, coverityPublishers);
        when(freeStyleProject.getPublishersList()).thenReturn(publisherList);
        when(freeStyleBuild.getProject()).thenReturn(freeStyleProject);
        return freeStyleBuild;
    }
}
