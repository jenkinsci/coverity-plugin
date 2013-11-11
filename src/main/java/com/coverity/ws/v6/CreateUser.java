
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userSpec" type="{http://ws.coverity.com/v6}userSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createUser", propOrder = {
    "userSpec"
})
public class CreateUser {

    protected UserSpecDataObj userSpec;

    /**
     * Gets the value of the userSpec property.
     * 
     * @return
     *     possible object is
     *     {@link UserSpecDataObj }
     *     
     */
    public UserSpecDataObj getUserSpec() {
        return userSpec;
    }

    /**
     * Sets the value of the userSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserSpecDataObj }
     *     
     */
    public void setUserSpec(UserSpecDataObj value) {
        this.userSpec = value;
    }

}
