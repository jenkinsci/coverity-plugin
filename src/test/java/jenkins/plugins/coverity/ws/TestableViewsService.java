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
package jenkins.plugins.coverity.ws;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


/**
 * Utility for setting up (mocking) the required classes to return values from the views service REST API.
 * You must have {@link PrepareForTest} set for {@link Client}.class in order to use this (it requires PowerMockito
 * to mock static).
 */
public final class TestableViewsService {
    public static void setupWithViewApi(String viewApiJsonResult) {
        Client restClient = mock(Client.class);
        WebTarget webTarget = mock(WebTarget.class);
        ClientBuilder clientBuilder = mock(ClientBuilder.class);
        PowerMockito.mockStatic(ClientBuilder.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);

        when(response.getStatus()).thenReturn(200);
        when(clientBuilder.newClient()).thenReturn(restClient);
        when(restClient.target(argThat(matchUriPath("/api/views/v1")))).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.get(String.class)).thenReturn(viewApiJsonResult);
        when(builder.get()).thenReturn(response);
    }

    public static void setupWithViews(Map<Long, String> views) {
        StringBuilder viewApiJsonBuilder = new StringBuilder();
        viewApiJsonBuilder.append("{\"views\": [");
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
        viewApiJsonBuilder.append("] }");
        setupWithViewApi(viewApiJsonBuilder.toString());
    }

    public static void setupViewContentsApi(String viewName, int httpStatus, String viewContentsApiJsonResult) {
        Client restClient = mock(Client.class);
        Response response = mock(Response.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        ClientBuilder clientBuilder = mock(ClientBuilder.class);
        PowerMockito.mockStatic(ClientBuilder.class);

        when(clientBuilder.newClient()).thenReturn(restClient);
        when(restClient.target(argThat(matchUriPath("/api/views/v1")))).thenReturn(webTarget);
        when(restClient.target(argThat(matchUriPath("/api/viewContents/issues/v1/" + viewName)))).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(response.getStatus()).thenReturn(httpStatus);
        when(response.readEntity(String.class)).thenReturn(viewContentsApiJsonResult);
        when(builder.get()).thenReturn(response);
    }

    public static void setupViewContentsApiThrows(Exception exception, String viewName) {
        Client restClient = mock(Client.class);
        Response response = mock(Response.class);
        WebTarget webTarget = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        ClientBuilder clientBuilder = mock(ClientBuilder.class);
        PowerMockito.mockStatic(ClientBuilder.class);

        when(clientBuilder.newClient()).thenReturn(restClient);
        when(restClient.target(argThat(matchUriPath("/api/viewContents/issues/v1/" + viewName)))).thenReturn(webTarget);
        when(webTarget.request()).thenReturn(builder);
        when(builder.get()).thenThrow(exception);
    }

    /**
     * Gets a {@link TypeSafeMatcher} which will only check the {@link URI} getPath value.
     * Currently this is sufficient enough for verify the View service behaviors
     */
    private static Matcher<URI> matchUriPath(final String path) {
        return new TypeSafeMatcher<URI>() {
            @Override
            protected boolean matchesSafely(URI item) {
                return item.getPath().equals(path);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("URI should have path ").appendValue(path);
            }
        };
    }
}
