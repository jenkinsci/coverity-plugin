
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteSnapshotJobInfoDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteSnapshotJobInfoDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="snapshotId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="status" type="{http://ws.coverity.com/v9}deleteSnapshotJobStatus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteSnapshotJobInfoDataObj", propOrder = {
    "snapshotId",
    "status"
})
public class DeleteSnapshotJobInfoDataObj {

    protected long snapshotId;
    protected DeleteSnapshotJobStatus status;

    /**
     * Gets the value of the snapshotId property.
     * 
     */
    public long getSnapshotId() {
        return snapshotId;
    }

    /**
     * Sets the value of the snapshotId property.
     * 
     */
    public void setSnapshotId(long value) {
        this.snapshotId = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link DeleteSnapshotJobStatus }
     *     
     */
    public DeleteSnapshotJobStatus getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeleteSnapshotJobStatus }
     *     
     */
    public void setStatus(DeleteSnapshotJobStatus value) {
        this.status = value;
    }

}
