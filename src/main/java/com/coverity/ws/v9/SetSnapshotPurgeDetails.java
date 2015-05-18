
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setSnapshotPurgeDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setSnapshotPurgeDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="purgeDetailsSpec" type="{http://ws.coverity.com/v9}snapshotPurgeDetailsObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setSnapshotPurgeDetails", propOrder = {
    "purgeDetailsSpec"
})
public class SetSnapshotPurgeDetails {

    protected SnapshotPurgeDetailsObj purgeDetailsSpec;

    /**
     * Gets the value of the purgeDetailsSpec property.
     * 
     * @return
     *     possible object is
     *     {@link SnapshotPurgeDetailsObj }
     *     
     */
    public SnapshotPurgeDetailsObj getPurgeDetailsSpec() {
        return purgeDetailsSpec;
    }

    /**
     * Sets the value of the purgeDetailsSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link SnapshotPurgeDetailsObj }
     *     
     */
    public void setPurgeDetailsSpec(SnapshotPurgeDetailsObj value) {
        this.purgeDetailsSpec = value;
    }

}
