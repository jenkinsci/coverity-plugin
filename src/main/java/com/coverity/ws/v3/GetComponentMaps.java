package com.coverity.ws.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getComponentMaps complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getComponentMaps">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentMapId" type="{http://ws.coverity.com/v3}componentMapFilterSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getComponentMaps", propOrder = {
    "componentMapId"
})
public class GetComponentMaps {

    protected ComponentMapFilterSpecDataObj componentMapId;

    /**
     * Gets the value of the componentMapId property.
     * 
     * @return
     *     possible object is
     *     {@link com.coverity.ws.v3.ComponentMapFilterSpecDataObj }
     *     
     */
    public ComponentMapFilterSpecDataObj getComponentMapId() {
        return componentMapId;
    }

    /**
     * Sets the value of the componentMapId property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.coverity.ws.v3.ComponentMapFilterSpecDataObj }
     *     
     */
    public void setComponentMapId(ComponentMapFilterSpecDataObj value) {
        this.componentMapId = value;
    }

}
