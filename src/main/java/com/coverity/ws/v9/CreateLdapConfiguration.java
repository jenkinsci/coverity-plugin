
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createLdapConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createLdapConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ldapConfigurationSpec" type="{http://ws.coverity.com/v9}ldapConfigurationSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createLdapConfiguration", propOrder = {
    "ldapConfigurationSpec"
})
public class CreateLdapConfiguration {

    protected LdapConfigurationSpecDataObj ldapConfigurationSpec;

    /**
     * Gets the value of the ldapConfigurationSpec property.
     * 
     * @return
     *     possible object is
     *     {@link LdapConfigurationSpecDataObj }
     *     
     */
    public LdapConfigurationSpecDataObj getLdapConfigurationSpec() {
        return ldapConfigurationSpec;
    }

    /**
     * Sets the value of the ldapConfigurationSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link LdapConfigurationSpecDataObj }
     *     
     */
    public void setLdapConfigurationSpec(LdapConfigurationSpecDataObj value) {
        this.ldapConfigurationSpec = value;
    }

}
