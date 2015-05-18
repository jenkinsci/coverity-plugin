
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setSkeletonizationConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setSkeletonizationConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="skeletonizationConfigurationDataObj" type="{http://ws.coverity.com/v9}skeletonizationConfigurationDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setSkeletonizationConfiguration", propOrder = {
    "skeletonizationConfigurationDataObj"
})
public class SetSkeletonizationConfiguration {

    protected SkeletonizationConfigurationDataObj skeletonizationConfigurationDataObj;

    /**
     * Gets the value of the skeletonizationConfigurationDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link SkeletonizationConfigurationDataObj }
     *     
     */
    public SkeletonizationConfigurationDataObj getSkeletonizationConfigurationDataObj() {
        return skeletonizationConfigurationDataObj;
    }

    /**
     * Sets the value of the skeletonizationConfigurationDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link SkeletonizationConfigurationDataObj }
     *     
     */
    public void setSkeletonizationConfigurationDataObj(SkeletonizationConfigurationDataObj value) {
        this.skeletonizationConfigurationDataObj = value;
    }

}
