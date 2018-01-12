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

import com.thoughtworks.xstream.XStream;
import hudson.util.XStream2;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CIMInstanceMigrationTest {

    @Rule
    public final JenkinsRule r = new JenkinsRule();


    /**
     * This test verifies that if user and password are still stored in the older configuration ( before 1.11.0 ),
     * then the user and password will be migrated into credentials with default ID
     */
    @Test
    public void migrateOldUseAndPassword() {
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
        assertEquals("Igor:1401_admin", instance.getCredentialId());
    }
}
