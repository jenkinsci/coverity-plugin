
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createRole complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createRole">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="roleSpec" type="{http://ws.coverity.com/v9}roleSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createRole", propOrder = {
    "roleSpec"
})
public class CreateRole {

    protected RoleSpecDataObj roleSpec;

    /**
     * Gets the value of the roleSpec property.
     * 
     * @return
     *     possible object is
     *     {@link RoleSpecDataObj }
     *     
     */
    public RoleSpecDataObj getRoleSpec() {
        return roleSpec;
    }

    /**
     * Sets the value of the roleSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoleSpecDataObj }
     *     
     */
    public void setRoleSpec(RoleSpecDataObj value) {
        this.roleSpec = value;
    }

}
