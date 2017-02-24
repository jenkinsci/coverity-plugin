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

import org.junit.Assert;
import org.junit.Test;

public class CoverityVersionTest {

    @Test
    public void equalTest() {
        CoverityVersion version1 = CoverityVersion.VERSION_JASPER;

        Assert.assertTrue(version1.equals(version1));
        Assert.assertFalse(version1.equals(null));
        Assert.assertFalse(version1.equals("Jasper"));

        version1 = new CoverityVersion(8, 7, 7, 0);
        CoverityVersion version2 = new CoverityVersion(7, 7, 7, 0);
        Assert.assertFalse(version1.equals(version2));

        version2 = new CoverityVersion(8, 8, 7, 0);
        Assert.assertFalse(version1.equals(version2));

        version2 = new CoverityVersion(8, 7, 8, 0);
        Assert.assertFalse(version1.equals(version2));

        version2 = new CoverityVersion(8, 7, 7, 1);
        Assert.assertFalse(version1.equals(version2));

        version2 = new CoverityVersion(8, 7, 7, 0);
        Assert.assertTrue(version1.equals(version2));

        version1 = CoverityVersion.VERSION_JASPER;
        version2 = CoverityVersion.VERSION_INDIO;
        Assert.assertFalse(version1.equals(version2));
    }

    @Test
    public void compareToTest() {
        CoverityVersion version1 = new CoverityVersion(8, 7, 7, 0);
        CoverityVersion version2 = new CoverityVersion(8, 7, 7, 0);
        Assert.assertEquals(0, version1.compareTo(version2));

        version2 = new CoverityVersion(7, 7, 7, 0);
        Assert.assertEquals(1, version1.compareTo(version2));

        version2 = new CoverityVersion(8, 8, 7, 0);
        Assert.assertEquals(-1, version1.compareTo(version2));

        version2 = new CoverityVersion(8, 7, 6, 0);
        Assert.assertEquals(1, version1.compareTo(version2));

        version2 = new CoverityVersion(8, 7, 7, 1);
        Assert.assertEquals(-1, version1.compareTo(version2));

        version1 = CoverityVersion.VERSION_JASPER;
        version2 = CoverityVersion.VERSION_INDIO;
        Assert.assertEquals(1, version1.compareTo(version2));
    }

    @Test
    public void compareToAnalysisTest() {
        CoverityVersion version1 = new CoverityVersion(8, 7, 7, 0);
        CoverityVersion version2 = new CoverityVersion(8, 7, 7, 0);
        Assert.assertTrue(version1.compareToAnalysis(version2));

        version2 = new CoverityVersion(8, 6, 0, 0);
        Assert.assertTrue(version1.compareToAnalysis(version2));

        version2 = new CoverityVersion(8, 8, 0, 0);
        Assert.assertFalse(version1.compareToAnalysis(version2));

        version1 = CoverityVersion.VERSION_JASPER;
        version2 = CoverityVersion.VERSION_JASPER;
        Assert.assertTrue(version1.compareToAnalysis(version2));

        version2 = CoverityVersion.VERSION_INDIO;
        Assert.assertTrue(version1.compareToAnalysis(version2));

        version2 = new CoverityVersion("lodi");
        Assert.assertFalse(version1.compareToAnalysis(version2));
    }
}
