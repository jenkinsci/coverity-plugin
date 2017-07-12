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
package jenkins.plugins.coverity.ws;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Utility for setting up (mocking) the required classes to return values from the views service REST API.
 * You must have {@link PrepareForTest} set for {@link Client}.class in order to use this (it requires PowerMockito
 * to mock static).
 */
public final class TestableViewsService {
    public static void setupWithViewApi(String viewApiJsonResult) {
        Client restClient = mock(Client.class);
        PowerMockito.mockStatic(Client.class);
        when(Client.create()).thenReturn(restClient);
        WebResource webResource = mock(WebResource.class);
        when(webResource.get(String.class)).thenReturn(viewApiJsonResult);
        when(restClient.resource(any(String.class))).thenReturn(webResource);
    }
}
