package com.coverity.ws.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateComponentMap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateComponentMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentMapFilter" type="{http://ws.coverity.com/v3}componentMapIdDataObj" minOccurs="0"/>
 *         &lt;element name="componentMapSpec" type="{http://ws.coverity.com/v3}componentMapSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateComponentMap", propOrder = {
    "componentMapFilter",
    "componentMapSpec"
})
public class UpdateComponentMap {

    protected ComponentMapIdDataObj componentMapFilter;
    protected ComponentMapSpecDataObj componentMapSpec;

    /**
     * Gets the value of the componentMapFilter property.
     * 
     * @return
     *     possible object is
     *     {@link com.coverity.ws.v3.ComponentMapIdDataObj }
     *     
     */
    public ComponentMapIdDataObj getComponentMapFilter() {
        return componentMapFilter;
    }

    /**
     * Sets the value of the componentMapFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.coverity.ws.v3.ComponentMapIdDataObj }
     *     
     */
    public void setComponentMapFilter(ComponentMapIdDataObj value) {
        this.componentMapFilter = value;
    }

    /**
     * Gets the value of the componentMapSpec property.
     * 
     * @return
     *     possible object is
     *     {@link com.coverity.ws.v3.ComponentMapSpecDataObj }
     *     
     */
    public ComponentMapSpecDataObj getComponentMapSpec() {
        return componentMapSpec;
    }

    /**
     * Sets the value of the componentMapSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.coverity.ws.v3.ComponentMapSpecDataObj }
     *     
     */
    public void setComponentMapSpec(ComponentMapSpecDataObj value) {
        this.componentMapSpec = value;
    }

}
