
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for componentPathRuleDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="componentPathRuleDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentId" type="{http://ws.coverity.com/v6}componentIdDataObj"/>
 *         &lt;element name="pathPattern" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "componentPathRuleDataObj", propOrder = {
    "componentId",
    "pathPattern"
})
public class ComponentPathRuleDataObj {

    @XmlElement(required = true)
    protected ComponentIdDataObj componentId;
    @XmlElement(required = true)
    protected String pathPattern;

    /**
     * Gets the value of the componentId property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentIdDataObj }
     *     
     */
    public ComponentIdDataObj getComponentId() {
        return componentId;
    }

    /**
     * Sets the value of the componentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentIdDataObj }
     *     
     */
    public void setComponentId(ComponentIdDataObj value) {
        this.componentId = value;
    }

    /**
     * Gets the value of the pathPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathPattern() {
        return pathPattern;
    }

    /**
     * Sets the value of the pathPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathPattern(String value) {
        this.pathPattern = value;
    }

}
