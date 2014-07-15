package jenkins.plugins.coverity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An abstract representation of a Coverity version number. Conventional version numbers, as well as current and past
 * codenames are all valid and comparable.
 */
public class CoverityVersion implements Comparable<CoverityVersion>, Serializable {
    public static final CoverityVersion VERSION_FRESNO = new CoverityVersion("fresno");

    static final Pattern parseRegex = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)(?:\\.(\\d+))?|(\\w+)");

    final int major;
    final int minor;
    final int patch;
    final int hotfix;
    final String codeName;
    final boolean isCodeName;

    static final HashMap<String, CoverityVersion> codeNameEquivalents = new HashMap<String, CoverityVersion>();

    static {
        codeNameEquivalents.put("harmony", new CoverityVersion(8, 0, 0));
        codeNameEquivalents.put("gilroy1", new CoverityVersion(7, 5, 1));
        codeNameEquivalents.put("gilroy", new CoverityVersion(7, 5, 0));
        codeNameEquivalents.put("fresno", new CoverityVersion(7, 0, 0));
        codeNameEquivalents.put("eureka", new CoverityVersion(6, 6, 0));
        codeNameEquivalents.put("davis", new CoverityVersion(6, 5, 1));
        codeNameEquivalents.put("carmel", new CoverityVersion(6, 5, 0));
        codeNameEquivalents.put("berkeley", new CoverityVersion(6, 0, 0));
        codeNameEquivalents.put("alameda", new CoverityVersion(5, 5, 0));
    }

    public CoverityVersion(String codeName) {
        this.isCodeName = true;
        this.codeName = codeName;
        this.hotfix = 0;
        this.minor = 0;
        this.patch = 0;
        this.major = 0;
    }

    public CoverityVersion(int major, int minor, int patch, int hotfix) {
        this.isCodeName = false;
        this.codeName = null;
        this.hotfix = hotfix;
        this.minor = minor;
        this.patch = patch;
        this.major = major;
    }

    public CoverityVersion(int major, int minor, int patch) {
        this.isCodeName = false;
        this.codeName = null;
        this.hotfix = 0;
        this.minor = minor;
        this.patch = patch;
        this.major = major;
    }

    public static CoverityVersion parse(String s) {
        Matcher m = parseRegex.matcher(s);
        if(!m.find()) {
            return null;
        }

        if(m.group(5) != null) {
            //codename
            return new CoverityVersion(m.group(5));
        } else {
            //number
            return new CoverityVersion(gi(m, 1), gi(m, 2), gi(m, 3), gi(m, 4));
        }
    }

    /**
     * Shorthand method. Return an integer from the given group of the given {@link Matcher}
     */
    private static int gi(Matcher m, int group) {
        if(m.group(group) == null) {
            return 0;
        }
        return Integer.parseInt(m.group(group));
    }

    /**
     * Converts from a codename version to an equivalent numbered version if necessary.
     */
    public CoverityVersion getEffectiveVersion() {
        if(isCodeName) {
            if(codeNameEquivalents.containsKey(codeName)) {
                return codeNameEquivalents.get(codeName);
            } else {
                return new CoverityVersion(0, 0, 0);
            }
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        if(isCodeName) {
            return codeName;
        } else {
            return major + "." + minor + "." + patch + (hotfix > 0 ? ("." + hotfix) : "");
        }
    }

    public int compareTo(CoverityVersion o) {
        if(isCodeName || o.isCodeName) {
            return getEffectiveVersion().compareTo(o.getEffectiveVersion());
        } else {
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
    }

    /**
     * The way that Compare Major Minor works is that the argument passed in is the analysis version.
     * @param version
     * @return
     */
    public boolean compareToAnalys(CoverityVersion version){
        if(isCodeName || version.isCodeName){
            return getEffectiveVersion().compareToAnalys(version.getEffectiveVersion());
        }else{
            if(major == version.major){
                return minor >= version.minor;
            }else{
                return major > version.major;
            }
        }
    }

    private int cmp(int a, int b) {
        return (a < b ? -1 : (a == b ? 0 : 1));
    }

    public int compareMajor(int major){
        return cmp(this.major,major);
    }
    // Returns if the version is a code name or not.
    public boolean isCodeName(){
        return isCodeName;
    }

    public boolean containsCodeName(){
        return codeNameEquivalents.containsKey(codeName);
    }
}
