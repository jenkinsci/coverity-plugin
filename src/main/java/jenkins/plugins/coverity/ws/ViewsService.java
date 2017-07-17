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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Service for interacting with the Coverity Connect Views Service JSON API
 */
public class ViewsService {
    private static final Logger logger = Logger.getLogger(ViewsService.class.getName());

    private final URL coverityConnectUrl;
    private final Client restClient;

    public ViewsService(URL coverityConnectUrl, Client restClient) {
        this.coverityConnectUrl = coverityConnectUrl;
        this.restClient = restClient;
    }

    /**
     * Returns a Map of available Coverity connect views, using the numeric identifier as the key and name as value
     */
    public Map<Long, String> getViews() {
        Map<Long, String> views = new HashMap<>();
        JSONObject json;

        try {
            final UriBuilder uriBuilder = UriBuilder.fromUri(coverityConnectUrl.toURI())
                .path("api/views/v1");

            WebResource resource = restClient.resource(uriBuilder.build());
            String response = resource.get(String.class);
            JSONParser parser = new JSONParser();
            json = (JSONObject)parser.parse(response);
        } catch (ParseException | URISyntaxException e) {
            logger.throwing(ViewsService.class.getName(), "getViews", e);
            return views;
        }

        JSONArray jsonViews = (JSONArray)json.get("views");
        for (Object view : jsonViews) {
            JSONObject jsonView = (JSONObject)view;
            String type = (String)jsonView.get("type");
            if (type != null && type.equals("issues")) {
                final Long viewId = (Long)jsonView.get("id");
                final String viewName = (String)jsonView.get("name");
                if (viewId != null && viewName != null) {
                    views.put(viewId, viewName);
                }
            }
        }

        return views;
    }

    public ViewContents getViewContents(String projectId, String connectView, int pageSize, int offset) {
        try {
            final UriBuilder uriBuilder = UriBuilder.fromUri(coverityConnectUrl.toURI())
                .path("api/viewContents/issues/v1/")
                .path(connectView)
                .queryParam("projectId", projectId)
                .queryParam("rowCount", pageSize)
                .queryParam("offset", offset);

            final URI viewContentsUri = uriBuilder.build();
            logger.info("Retrieving View contents from " + viewContentsUri);

            WebResource resource = restClient.resource(viewContentsUri);

            String response = resource.get(String.class);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject)parser.parse(response);

            return new ViewContents((JSONObject)json.get("viewContentsV1"));

        } catch (ParseException | URISyntaxException e) {
            logger.throwing(ViewsService.class.getName(), "getViews", e);
            return new ViewContents(new JSONObject());
        }
    }
}
