package jenkins.plugins.coverity;

import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;

public class SSLConfigurations {

    private boolean trustNewSelfSignedCert;
    JSONObject certFileJSON;
    String certFileName;

    @DataBoundConstructor
    public SSLConfigurations(boolean trustNewSelfSignedCert, JSONObject certFileJSON){
        setTrustNewSelfSignedCert(trustNewSelfSignedCert);

        /*SslConfigSingleton singleton = SslConfigSingleton.getInstance();
        if(trustNewSelfSignedCert){
            singleton.setOnNewSelfSignedCert(SslConnector.OnNewSelfSignedCert.valueOf("trust"));
        } else {
            singleton.setOnNewSelfSignedCert(SslConnector.OnNewSelfSignedCert.valueOf("distrust"));
        }*/

        String certFileName = null;
        if(certFileJSON != null){
            certFileName = (String) certFileJSON.get("certFileName");
        }

        if(certFileName != null && !certFileName.isEmpty()){
            //singleton.setCertFileName(certFileName);
            setCertFileName(certFileName);
        }
    }

    public boolean isTrustNewSelfSignedCert() {
        return trustNewSelfSignedCert;
    }

    public void setTrustNewSelfSignedCert(boolean trustNewSelfSignedCert) {
        this.trustNewSelfSignedCert = trustNewSelfSignedCert;
    }

    public JSONObject getCertFileJSON() {
        return certFileJSON;
    }

    public void setCertFileJSON(JSONObject certFileJSON) {
        this.certFileJSON = certFileJSON;
    }

    public String getCertFileName() {
        return certFileName;
    }

    public void setCertFileName(String certFileName) {
        this.certFileName = certFileName;
    }

    /**
     * Singleton used for configurations for ssl in case ssl is selected.
     */
    /*private static class SslConfigSingleton {

        *//**
         * Specify whether to trust or not a self-signed certificate.
         *//*
        //private SslConnector.OnNewSelfSignedCert onNewSelfSignedCert;

        public void setOnNewSelfSignedCert(SslConnector.OnNewSelfSignedCert onNewSelfSignedCert) {
            this.onNewSelfSignedCert = onNewSelfSignedCert;
        }

        *//**
         * Sets default extra trust store path.
         *//*
        public void setCertFile(String certFileName) {
            HttpsTrustStoreUrlConnection.setDefaultExtraTrustStorePath(certFileName);
        }

        private static SslConfigSingleton instance = null;
        private SslConfigSingleton() {
            // Tell Java not to try to use SSL for the secure connection, and use TLS instead.
            // We choose only TLSv1 and not v1.1 or v1.2 because Java 6, needed for the Analysis installation,
            // supports only TLSv1.
            System.setProperty("https.protocols", "TLSv1");
            // Activate our special https protocol handler, which fronts for the real one and sets up
            // the trust stores.
            TomcatURLStreamHandlerFactory tomcatHandlerFactory = org.apache.catalina.webresources.TomcatURLStreamHandlerFactory.getInstance();
            tomcatHandlerFactory.addUserFactory(new HttpsTrustStoreStreamHandlerFactory());

            // Don't use a UI with trust store operations.  The rationale is that we can't reliably tell whether
            // there's a user there (since the System.console() doesn't seem to do the equivalent of
            // isatty(stdin)), and since this application may be used in unattended mode, we can't risk causing
            // it to hang waiting for console input that will never come.
            HttpsTrustStoreUrlConnection.setDefaultUserInterface(null);
        }
        public static synchronized SslConfigSingleton getInstance() {
            if(instance == null) {
                instance = new SslConfigSingleton();
            }
            return instance;
        }
    }*/

}