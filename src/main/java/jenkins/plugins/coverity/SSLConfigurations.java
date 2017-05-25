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

import net.sf.json.JSONObject;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.kohsuke.stapler.DataBoundConstructor;

public class SSLConfigurations {

    // deprecated fields which were removed in plugin version 1.9.2
    private transient String certFileName;

    private boolean trustNewSelfSignedCert;
    private SSLCertFileName sslCertFileName;

    @DataBoundConstructor
    public SSLConfigurations(boolean trustNewSelfSignedCert, SSLCertFileName sslCertFileName){
        setTrustNewSelfSignedCert(trustNewSelfSignedCert);
        setCertFileName(sslCertFileName);
    }

    /**
     * Implement readResolve to update the de-serialized object in the case transient data was found. Transient fields
     * will be read during de-serialization and readResolve allow updating the SSLConfigurations object after being created.
     */
    protected Object readResolve() {
        // Check for certFileName
        if(certFileName != null) {
            setCertFileName(new SSLCertFileName(certFileName));
        }

        return this;
    }

    public boolean isTrustNewSelfSignedCert() {
        return trustNewSelfSignedCert;
    }

    public void setTrustNewSelfSignedCert(boolean trustNewSelfSignedCert) {
        this.trustNewSelfSignedCert = trustNewSelfSignedCert;
    }

    public String getCertFileName() {
        return sslCertFileName != null ? sslCertFileName.getCertFileName() : null;
    }

    public void setCertFileName(SSLCertFileName sslCertFileName) {
        this.sslCertFileName = sslCertFileName;
    }
}