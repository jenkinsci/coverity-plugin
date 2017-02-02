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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.coverity.ws.v9.ConfigurationService;
import com.coverity.ws.v9.CovRemoteServiceException_Exception;
import com.coverity.ws.v9.ProjectDataObj;
import com.coverity.ws.v9.ProjectFilterSpecDataObj;
import com.coverity.ws.v9.StreamDataObj;

import jenkins.plugins.coverity.CIMInstance;

/**
 * Temporary cache of cim data to improve job configuration behavior
 */
public final class CimCache {

    private static CimCache instance = null;

    private Map<CIMInstance, CachedData> cache;

    private CimCache(){
        cache = new HashMap<>();
    }

    public static CimCache getInstance() {
        if (instance == null) {
            synchronized (CimCache.class) {
                if (instance == null) {
                    instance = new CimCache();
                }
            }
        }
        return instance;
    }

    public void cacheInstance(CIMInstance cimInstance) {
        if(cache.containsKey(cimInstance)) {
            //todo: check for instance data being up to data... otherwise add
        }

        cache.put(cimInstance, new CachedData(cimInstance));
    }

    public Set<String> getProjects(CIMInstance cimInstance) {
        if (cache.containsKey(cimInstance)) {
            final CachedData cachedData = cache.get(cimInstance);
            return cachedData.getAvailableProjects();
        }

        final CachedData cachedData = new CachedData(cimInstance);
        cache.put(cimInstance, cachedData);
        return cachedData.getAvailableProjects();
    }

    public Set<String> getStreams(CIMInstance cimInstance, String project) {
        if (cache.containsKey(cimInstance)) {
            final CachedData cachedData = cache.get(cimInstance);
            return cachedData.getStreamsForProject(project);
        }

        final CachedData cachedData = new CachedData(cimInstance);
        cache.put(cimInstance, cachedData);
        return cachedData.getStreamsForProject(project);
    }

    private class CachedData {
        private Map<String, Set<String>> projectStreams;

        public CachedData(CIMInstance cimInstance) {
            projectStreams = new HashMap<>();
            try {
                ConfigurationService configurationService = cimInstance.getConfigurationService();
                final List<ProjectDataObj> projects = configurationService.getProjects(new ProjectFilterSpecDataObj());

                for (ProjectDataObj project : projects){
                    Set<String> streamNames = new HashSet<>();
                    for (StreamDataObj stream : project.getStreams()) {
                        streamNames.add(stream.getId().getName());
                    }
                    projectStreams.put(project.getId().getName(), streamNames);
                }
            } catch (IOException e) {
                // ignore exceptions here (nothing will be cached in this case)
            } catch (CovRemoteServiceException_Exception e) {
                // ignore exceptions here (nothing will be cached in this case)
            }
        }

        public Set<String> getAvailableProjects() {
            return projectStreams.keySet();
        }

        public Set<String> getStreamsForProject(String projectName) {
            if (projectStreams.containsKey(projectName)) {
                return projectStreams.get(projectName);
            }

            return new HashSet<>();
        }
    }
}
