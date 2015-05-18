
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createComponentMap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createComponentMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentMapSpec" type="{http://ws.coverity.com/v9}componentMapSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createComponentMap", propOrder = {
    "componentMapSpec"
})
public class CreateComponentMap {

    protected ComponentMapSpecDataObj componentMapSpec;

    /**
     * Gets the value of the componentMapSpec property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentMapSpecDataObj }
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
     *     {@link ComponentMapSpecDataObj }
     *     
     */
    public void setComponentMapSpec(ComponentMapSpecDataObj value) {
        this.componentMapSpec = value;
    }

}
