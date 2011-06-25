package com.coverity.ws.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getAssignableUsers complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getAssignableUsers">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="pageSpec" type="{http://ws.coverity.com/v3}pageSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAssignableUsers", propOrder = {
    "pageSpec"
})
public class GetAssignableUsers {

    protected PageSpecDataObj pageSpec;

    /**
     * Gets the value of the pageSpec property.
     * 
     * @return
     *     possible object is
     *     {@link PageSpecDataObj }
     *     
     */
    public PageSpecDataObj getPageSpec() {
        return pageSpec;
    }

    /**
     * Sets the value of the pageSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageSpecDataObj }
     *     
     */
    public void setPageSpec(PageSpecDataObj value) {
        this.pageSpec = value;
    }

}
