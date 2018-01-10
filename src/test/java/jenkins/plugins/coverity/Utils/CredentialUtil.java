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

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.common.UsernamePasswordCredentials;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import hudson.model.ItemGroup;
import hudson.util.Secret;
import org.acegisecurity.Authentication;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;

public class CredentialUtil {

    public static void setCredentialManager(String username, String password) {
        PowerMockito.mockStatic(CredentialsMatchers.class);
        PowerMockito.mockStatic(CredentialsProvider.class);
        UsernamePasswordCredentials credentials = Mockito.mock(UsernamePasswordCredentialsImpl.class);

        Secret secret = PowerMockito.mock(Secret.class);
        PowerMockito.when(secret.getPlainText()).thenReturn(password);
        PowerMockito.when(credentials.getPassword()).thenReturn(secret);
        PowerMockito.when(credentials.getUsername()).thenReturn(username);

        PowerMockito.when(CredentialsProvider.lookupCredentials(
                Matchers.<Class<Credentials>>any(),
                Matchers.any(ItemGroup.class),
                Matchers.any(Authentication.class),
                Matchers.anyListOf(DomainRequirement.class)
        )).thenReturn(new ArrayList<Credentials>() {
        });

        PowerMockito.when(CredentialsMatchers.firstOrNull(Matchers.anyListOf(StandardCredentials.class),
                Matchers.any(CredentialsMatcher.class))).thenReturn((StandardUsernamePasswordCredentials) credentials);
    }
}
