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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * An abstract representation of a Coverity version number. Conventional version numbers, as well as current and past
 * codenames are all valid and comparable.
 */
public class CoverityVersion implements Comparable<CoverityVersion>, Serializable {
    public static final CoverityVersion VERSION_INDIO = new CoverityVersion(7, 7, 0, 0);
    public static final CoverityVersion VERSION_JASPER = new CoverityVersion(8, 0, 0, 0);

    public static final CoverityVersion MINIMUM_SUPPORTED_VERSION = VERSION_INDIO;

    final int major;
    final int minor;
    final int patch;
    final int hotfix;
    private boolean isSrmVersion;
    private String srmVersion;

    public CoverityVersion(int major, int minor, int patch, int hotfix) {
        this.isSrmVersion = false;
        this.hotfix = hotfix;
        this.minor = minor;
        this.patch = patch;
        this.major = major;
    }

    public CoverityVersion(int major, int minor) {
        this.hotfix = 0;
        this.patch = 0;
        this.major = major;
        this.minor = minor;
    }

    public static CoverityVersion parse(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }

        final String[] parts = s.split("\\.");

        if (parts.length == 2) {
            //srm number
            CoverityVersion srmVersion;
            final String[] srmParts = parts[1].split("-SP|-");
            if (srmParts.length == 2)
            {
                srmVersion =  new CoverityVersion(Integer.parseInt(parts[0]), Integer.parseInt(srmParts[0]), Integer.parseInt(srmParts[1]), 0);
            } else if (srmParts.length == 3) {
                srmVersion = new CoverityVersion(Integer.parseInt(parts[0]), Integer.parseInt(srmParts[0]), Integer.parseInt(srmParts[1]), Integer.parseInt(srmParts[2]));
            } else {
                srmVersion = new CoverityVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            }
            srmVersion.isSrmVersion = true;
            srmVersion.srmVersion = s;
            return srmVersion;
        } else if (parts.length == 3) {
            //number
            return new CoverityVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), 0);
        } else if (parts.length == 4) {
            //number w/hotfix
            return new CoverityVersion(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }

        return null;
    }

    @Override
    public String toString() {
        if (isSrmVersion && !StringUtils.isEmpty(srmVersion)) {
            return srmVersion;
        }

        return major + "." + minor + "." + patch + (hotfix > 0 ? ("." + hotfix) : "");
    }

    public int compareTo(CoverityVersion o) {

        if(major == o.major) {
            if(minor == o.minor) {
                if(patch == o.patch) {
                    return cmp(hotfix, o.hotfix);
                } else {
                    return cmp(patch, o.patch);
                }
            } else {
                return cmp(minor, o.minor);
            }
        } else {
            return cmp(major, o.major);
        }
    }

    /**
     * The way that Compare Major Minor works is that the argument passed in is the analysis version.
     * Compares the version's major and minor version number.
     * @param version
     * @return true if the current version is greater than or equals to the passed in version; Otherwise false
     */
    public boolean compareToAnalysis(CoverityVersion version){
        if(major == version.major){
            return minor >= version.minor;
        }else{
            return major > version.major;
        }
    }

    private int cmp(int a, int b) {
        return (a < b ? -1 : (a == b ? 0 : 1));
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        CoverityVersion other = (CoverityVersion) o;

        if (major != other.major) {
            return false;
        }
        if (minor != other.minor) {
            return false;
        }
        if (patch != other.patch) {
            return false;
        }
        if (hotfix != other.hotfix) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 31;
        result = 31 * result + major;
        result = 31 * result + minor;
        result = 31 * result + patch;
        result = 31 * result + hotfix;
        return result;
    }
}
