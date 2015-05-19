
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteLdapConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteLdapConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serverDomainIdDataObj" type="{http://ws.coverity.com/v9}serverDomainIdDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteLdapConfiguration", propOrder = {
    "serverDomainIdDataObj"
})
public class DeleteLdapConfiguration {

    protected ServerDomainIdDataObj serverDomainIdDataObj;

    /**
     * Gets the value of the serverDomainIdDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link ServerDomainIdDataObj }
     *     
     */
    public ServerDomainIdDataObj getServerDomainIdDataObj() {
        return serverDomainIdDataObj;
    }

    /**
     * Sets the value of the serverDomainIdDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServerDomainIdDataObj }
     *     
     */
    public void setServerDomainIdDataObj(ServerDomainIdDataObj value) {
        this.serverDomainIdDataObj = value;
    }

}
