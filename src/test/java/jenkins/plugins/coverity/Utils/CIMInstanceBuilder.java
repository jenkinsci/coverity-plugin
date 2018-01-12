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

import jenkins.plugins.coverity.CIMInstance;

public class CIMInstanceBuilder {
    private String name;
    private String host;
    private int port;
    private String credentialId;
    private boolean useSSL;

    public CIMInstanceBuilder withName(String name){
        this.name = name;
        return this;
    }

    public CIMInstanceBuilder withHost(String host){
        this.host = host;
        return this;
    }

    public CIMInstanceBuilder withPort(int port){
        this.port = port;
        return this;
    }

    public CIMInstanceBuilder withCredentialId(String credentialId){
        this.credentialId = credentialId;
        return this;
    }

    public CIMInstanceBuilder withDefaultCredentialId() {
        this.credentialId = "DefaultCredentialId";
        return this;
    }

    public CIMInstanceBuilder withUseSSL(boolean useSSL){
        this.useSSL = useSSL;
        return this;
    }

    public CIMInstance build(){
        CIMInstance instance = new CIMInstance(name, host, port, credentialId);
        instance.setUseSSL(useSSL);

        return instance;
    }
}
