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
        version2 = CoverityVersion.VERSION_JASPER1;
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

        version1 = CoverityVersion.VERSION_JASPER1;
        version2 = CoverityVersion.VERSION_JASPER;
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

        version1 = CoverityVersion.VERSION_JASPER1;
        Assert.assertTrue(version1.compareToAnalysis(version2));
    }

    @Test
    public void parseTest() {
        // test supported versions
        CoverityVersion version = CoverityVersion.parse("8.7.1.0");
        Assert.assertEquals(8, version.major);
        Assert.assertEquals(7, version.minor);
        Assert.assertEquals(1, version.patch);
        Assert.assertEquals(0, version.hotfix);
        Assert.assertEquals("8.7.1", version.toString());

        // SRM versions
        version = CoverityVersion.parse("2017.07");
        Assert.assertEquals(2017, version.major);
        Assert.assertEquals(7, version.minor);
        Assert.assertEquals(0, version.patch);
        Assert.assertEquals(0, version.hotfix);
        Assert.assertEquals("2017.07", version.toString());

        version = CoverityVersion.parse("2017.12");
        Assert.assertEquals(2017, version.major);
        Assert.assertEquals(12, version.minor);
        Assert.assertEquals(0, version.patch);
        Assert.assertEquals(0, version.hotfix);
        Assert.assertEquals("2017.12", version.toString());

        version = CoverityVersion.parse("2017.07-1");
        Assert.assertEquals(2017, version.major);
        Assert.assertEquals(7, version.minor);
        Assert.assertEquals(1, version.patch);
        Assert.assertEquals(0, version.hotfix);
        Assert.assertEquals("2017.07-1", version.toString());

        version = CoverityVersion.parse("2017.07-SP1");
        Assert.assertEquals(2017, version.major);
        Assert.assertEquals(7, version.minor);
        Assert.assertEquals(1, version.patch);
        Assert.assertEquals(0, version.hotfix);
        Assert.assertEquals("2017.07-SP1", version.toString());

        version = CoverityVersion.parse("2017.07-SP1-2");
        Assert.assertEquals(2017, version.major);
        Assert.assertEquals(7, version.minor);
        Assert.assertEquals(1, version.patch);
        Assert.assertEquals(2, version.hotfix);
        Assert.assertEquals("2017.07-SP1-2", version.toString());

        // named versions
        version = CoverityVersion.parse("lodi");
        Assert.assertNull(version);
    }
}
