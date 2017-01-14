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

import org.junit.Assert;
import org.junit.Test;

import com.coverity.ws.v9.DefectService;

import jenkins.plugins.coverity.CIMInstance;
import jenkins.plugins.coverity.ws.TestWebServiceFactory.TestDefectService;

public class WebServiceFactoryTest {
    private CIMInstance cimInstance = new CIMInstance("test", "cim-host", 8080, "test-user", "password", false, 0);

    @Test
    public void getDefectService_returns_DefectService_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory(null);

        DefectService result = factory.getDefectService(cimInstance);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof TestDefectService);
    }

    @Test
    public void getDefectService_returns_same_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory(null);

        DefectService result = factory.getDefectService(cimInstance);

        Assert.assertNotNull(result);
        DefectService result2 = factory.getDefectService(cimInstance);

        Assert.assertSame(result, result2);
    }

    @Test
    public void getDefectService_returns_new_instance() throws IOException {
        WebServiceFactory factory = new TestWebServiceFactory(null);

        DefectService result = factory.getDefectService(cimInstance);

        Assert.assertNotNull(result);

        cimInstance = new CIMInstance("test instance 2", "other-cim-host", 8080, "test-user", "password", false, 0);

        DefectService result2 = factory.getDefectService(cimInstance);

        Assert.assertNotSame(result, result2);
    }
}
