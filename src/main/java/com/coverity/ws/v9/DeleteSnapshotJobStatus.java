
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteSnapshotJobStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="deleteSnapshotJobStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="QUEUED"/>
 *     &lt;enumeration value="RUNNING"/>
 *     &lt;enumeration value="SUCCEEDED"/>
 *     &lt;enumeration value="FAILED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "deleteSnapshotJobStatus")
@XmlEnum
public enum DeleteSnapshotJobStatus {

    QUEUED,
    RUNNING,
    SUCCEEDED,
    FAILED;

    public String value() {
        return name();
    }

    public static DeleteSnapshotJobStatus fromValue(String v) {
        return valueOf(v);
    }

}
