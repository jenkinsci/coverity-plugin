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

import java.util.Map;
import java.util.Map.Entry;

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

    public static void setupWithViews(Map<Long, String> views) {
        StringBuilder viewApiJsonBuilder = new StringBuilder();
        viewApiJsonBuilder.append("{\"views\": [ {");
        for (Entry<Long, String> e : views.entrySet()) {
            viewApiJsonBuilder.append("{");
            viewApiJsonBuilder.append("\"id\": " + e.getKey() + ",");
            viewApiJsonBuilder.append("\"type\": \"issues\",");
            viewApiJsonBuilder.append("\"name\": \"" + e.getValue() + "\",");
            viewApiJsonBuilder.append("\"groupBy\":false,");
            viewApiJsonBuilder.append("\"columns\": [");
            viewApiJsonBuilder.append("{\"name\": \"cid\",\"label\": \"CID\"},");
            viewApiJsonBuilder.append("{\"name\": \"checker\",\"label\": \"Checker\"},");
            viewApiJsonBuilder.append("{\"name\": \"displayFunction\",\"label\": \"Function\"},");
            viewApiJsonBuilder.append("{\"name\": \"displayFile\",\"label\": \"File\"}");
            viewApiJsonBuilder.append("]");
            viewApiJsonBuilder.append("}");
        }
        viewApiJsonBuilder.append("} ] }");
        setupWithViewApi(viewApiJsonBuilder.toString());
    }
}
