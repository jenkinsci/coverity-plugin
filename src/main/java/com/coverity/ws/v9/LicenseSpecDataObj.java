
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for licenseSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="licenseSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="licenseDataFile" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licenseSpecDataObj", propOrder = {
    "licenseDataFile"
})
public class LicenseSpecDataObj {

    protected String licenseDataFile;

    /**
     * Gets the value of the licenseDataFile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseDataFile() {
        return licenseDataFile;
    }

    /**
     * Sets the value of the licenseDataFile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseDataFile(String value) {
        this.licenseDataFile = value;
    }

}
