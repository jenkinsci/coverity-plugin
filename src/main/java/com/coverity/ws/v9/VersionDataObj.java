
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for versionDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="versionDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="externalVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="internalVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "versionDataObj", propOrder = {
    "externalVersion",
    "internalVersion"
})
public class VersionDataObj {

    protected String externalVersion;
    protected String internalVersion;

    /**
     * Gets the value of the externalVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalVersion() {
        return externalVersion;
    }

    /**
     * Sets the value of the externalVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalVersion(String value) {
        this.externalVersion = value;
    }

    /**
     * Gets the value of the internalVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalVersion() {
        return internalVersion;
    }

    /**
     * Sets the value of the internalVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalVersion(String value) {
        this.internalVersion = value;
    }

}
