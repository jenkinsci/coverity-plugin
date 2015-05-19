
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateSignInConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateSignInConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="signInSettingsDataObj" type="{http://ws.coverity.com/v9}signInSettingsDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateSignInConfiguration", propOrder = {
    "signInSettingsDataObj"
})
public class UpdateSignInConfiguration {

    protected SignInSettingsDataObj signInSettingsDataObj;

    /**
     * Gets the value of the signInSettingsDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link SignInSettingsDataObj }
     *     
     */
    public SignInSettingsDataObj getSignInSettingsDataObj() {
        return signInSettingsDataObj;
    }

    /**
     * Sets the value of the signInSettingsDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignInSettingsDataObj }
     *     
     */
    public void setSignInSettingsDataObj(SignInSettingsDataObj value) {
        this.signInSettingsDataObj = value;
    }

}
