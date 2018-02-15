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
package jenkins.plugins.coverity;

import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.CredentialsStore;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.Domain;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import com.thoughtworks.xstream.XStream;
import hudson.security.ACL;
import hudson.util.XStream2;
import jenkins.model.Jenkins;
import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class CIMInstanceMigrationTest {

    @Rule
    public final JenkinsRule r = new JenkinsRule();

    /**
     * This test verifies that if user and password are still stored in the older configuration ( before 1.11.0 ),
     * then the user and password will be migrated into credentials with default ID
     */
    @Test
    public void migrateOldUserAndPassword() {
        String oldConfiguration = "    <jenkins.plugins.coverity.CIMInstance>\n" +
                "      <name>Igor:1401</name>\n" +
                "      <host>igor</host>\n" +
                "      <port>1401</port>\n" +
                "      <user>admin</user>\n" +
                "      <password>coverity</password>\n" +
                "      <useSSL>false</useSSL>\n" +
                "      <credentialId></credentialId>\n" +
                "    </jenkins.plugins.coverity.CIMInstance>\n" +
                "  </instances>";

        XStream xstream = new XStream2();

        final CIMInstance instance = (CIMInstance)xstream.fromXML(oldConfiguration);
        assertNotNull(instance);
        String credentialId = instance.getCredentialId();
        assertEquals("Igor:1401_admin", credentialId);

        StandardCredentials credential = CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(
                        StandardCredentials.class, Jenkins.getInstance(), ACL.SYSTEM, Collections.<DomainRequirement>emptyList()
                ), CredentialsMatchers.withId(credentialId)
        );

        assertNotNull(credential);
        if (credential instanceof UsernamePasswordCredentials) {
            UsernamePasswordCredentialsImpl c = (UsernamePasswordCredentialsImpl)credential;
            assertEquals("Migrated Coverity Credential", c.getDescription());
        } else {
            fail("Migration should have added UsernamePasswordCredential type credential into Credentials");
        }
    }

    /**
     * This test verifies that the migration will not happen if old configuration does not have
     * user and password field
     */
    @Test
    public void doesNotMigrate_WhenUserAndPasswordNotExist() {
        String oldConfiguration = "    <jenkins.plugins.coverity.CIMInstance>\n" +
                "      <name>Igor:1401</name>\n" +
                "      <host>igor</host>\n" +
                "      <port>1401</port>\n" +
                "      <useSSL>false</useSSL>\n" +
                "      <credentialId></credentialId>\n" +
                "    </jenkins.plugins.coverity.CIMInstance>\n" +
                "  </instances>";

        XStream xstream = new XStream2();

        final CIMInstance instance = (CIMInstance)xstream.fromXML(oldConfiguration);
        assertNotNull(instance);
        String credentialId = instance.getCredentialId();
        assertEquals(StringUtils.EMPTY, credentialId);

        StandardCredentials credential = CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(
                        StandardCredentials.class, Jenkins.getInstance(), ACL.SYSTEM, Collections.<DomainRequirement>emptyList()
                ), CredentialsMatchers.withId(credentialId)
        );

        assertNull(credential);
    }

    /**
     * This test verifies that the migration will not happen if migration happened before by
     * checking the if the same id exists already
     */
    @Test
    public void doesNotMigrate_WhenSameCredentialIdExist() throws IOException {
        String oldConfiguration = "    <jenkins.plugins.coverity.CIMInstance>\n" +
                "      <name>Igor:1401</name>\n" +
                "      <host>igor</host>\n" +
                "      <port>1401</port>\n" +
                "      <user>admin</user>\n" +
                "      <password>coverity</password>\n" +
                "      <useSSL>false</useSSL>\n" +
                "      <credentialId></credentialId>\n" +
                "    </jenkins.plugins.coverity.CIMInstance>\n" +
                "  </instances>";

        UsernamePasswordCredentialsImpl testCredential = new UsernamePasswordCredentialsImpl(
                CredentialsScope.GLOBAL, "Igor:1401_admin", "Manually Added Credential", "admin", "coverity");

        CredentialsStore store = CredentialsProvider.lookupStores(Jenkins.getInstance()).iterator().next();
        store.addCredentials(Domain.global(), testCredential);

        XStream xstream = new XStream2();

        final CIMInstance instance = (CIMInstance)xstream.fromXML(oldConfiguration);
        assertNotNull(instance);
        String credentialId = instance.getCredentialId();
        assertEquals(StringUtils.EMPTY, credentialId);

        StandardCredentials credential = CredentialsMatchers.firstOrNull(
                CredentialsProvider.lookupCredentials(
                        StandardCredentials.class, Jenkins.getInstance(), ACL.SYSTEM, Collections.<DomainRequirement>emptyList()
                ), CredentialsMatchers.withId("Igor:1401_admin")
        );

        assertNotNull(credential);
        if (credential instanceof UsernamePasswordCredentials) {
            UsernamePasswordCredentialsImpl c = (UsernamePasswordCredentialsImpl)credential;
            assertEquals("Manually Added Credential", c.getDescription());
        } else {
            fail("Migration should have added UsernamePasswordCredential type credential into Credentials");
        }
    }
}
