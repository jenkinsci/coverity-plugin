
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for importLicense complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="importLicense">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="licenseSpecDataObj" type="{http://ws.coverity.com/v9}licenseSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "importLicense", propOrder = {
    "licenseSpecDataObj"
})
public class ImportLicense {

    protected LicenseSpecDataObj licenseSpecDataObj;

    /**
     * Gets the value of the licenseSpecDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link LicenseSpecDataObj }
     *     
     */
    public LicenseSpecDataObj getLicenseSpecDataObj() {
        return licenseSpecDataObj;
    }

    /**
     * Sets the value of the licenseSpecDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link LicenseSpecDataObj }
     *     
     */
    public void setLicenseSpecDataObj(LicenseSpecDataObj value) {
        this.licenseSpecDataObj = value;
    }

}
