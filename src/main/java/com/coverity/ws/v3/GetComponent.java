package com.coverity.ws.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getComponent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getComponent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentMapFilter" type="{http://ws.coverity.com/v3}componentIdDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getComponent", propOrder = {
    "componentMapFilter"
})
public class GetComponent {

    protected ComponentIdDataObj componentMapFilter;

    /**
     * Gets the value of the componentMapFilter property.
     * 
     * @return
     *     possible object is
     *     {@link com.coverity.ws.v3.ComponentIdDataObj }
     *     
     */
    public ComponentIdDataObj getComponentMapFilter() {
        return componentMapFilter;
    }

    /**
     * Sets the value of the componentMapFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.coverity.ws.v3.ComponentIdDataObj }
     *     
     */
    public void setComponentMapFilter(ComponentIdDataObj value) {
        this.componentMapFilter = value;
    }

}
