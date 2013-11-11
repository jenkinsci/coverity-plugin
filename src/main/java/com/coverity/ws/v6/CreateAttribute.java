
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createAttribute complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createAttribute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeDefinitionSpec" type="{http://ws.coverity.com/v6}attributeDefinitionSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createAttribute", propOrder = {
    "attributeDefinitionSpec"
})
public class CreateAttribute {

    protected AttributeDefinitionSpecDataObj attributeDefinitionSpec;

    /**
     * Gets the value of the attributeDefinitionSpec property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeDefinitionSpecDataObj }
     *     
     */
    public AttributeDefinitionSpecDataObj getAttributeDefinitionSpec() {
        return attributeDefinitionSpec;
    }

    /**
     * Sets the value of the attributeDefinitionSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeDefinitionSpecDataObj }
     *     
     */
    public void setAttributeDefinitionSpec(AttributeDefinitionSpecDataObj value) {
        this.attributeDefinitionSpec = value;
    }

}
