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

import jenkins.plugins.coverity.CIMInstance;

public class CIMInstanceBuilder {
    private String name;
    private String host;
    private int port;
    private String credentialId;
    private String user;
    private String password;
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

    public CIMInstanceBuilder withUser(String user){
        this.user = user;
        return this;
    }

    public CIMInstanceBuilder withPassword(String password){
        this.password = password;
        return this;
    }

    public CIMInstanceBuilder withUseSSL(boolean useSSL){
        this.useSSL = useSSL;
        return this;
    }

    public CIMInstance build(){
        CIMInstance instance = new CIMInstance(name, host, port, credentialId);
        instance.setUseSSL(useSSL);
        instance.setUser(user);
        instance.setPassword(password);

        return instance;
    }
}
