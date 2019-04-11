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

import jenkins.plugins.coverity.CIMInstance;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RedirectedServiceUrl {
    private static RedirectedServiceUrl instance = null;
    private final Map<CIMInstance, URL> serviceUrlMap;

    private RedirectedServiceUrl(){
        this.serviceUrlMap = new HashMap<>();
    }

    public static RedirectedServiceUrl getInstance() {
        // ignore for Coverity TA since unit tests will not run against a real Connect server
        //.*cov-begin-ignore
        synchronized (RedirectedServiceUrl.class) {
            if (instance == null) {
                instance = new RedirectedServiceUrl();
            }
            return instance;
        }
        //.*cov-end-ignore
    }

    public URL getURL(CIMInstance cimInstance){
        return serviceUrlMap.containsKey(cimInstance) ? serviceUrlMap.get(cimInstance) : null;
    }

    public void cacheURL(CIMInstance cimInstance, URL redirectedUrl){
        synchronized (this){
            serviceUrlMap.put(cimInstance, redirectedUrl);
        }
    }

    public void removeURL(CIMInstance cimInstance){
        synchronized (this){
            if (serviceUrlMap.containsKey(cimInstance)){
                serviceUrlMap.remove(cimInstance);
            }
        }
    }
}
