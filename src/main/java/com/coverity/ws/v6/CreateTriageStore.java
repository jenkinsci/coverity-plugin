
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createTriageStore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createTriageStore">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="triageStoreSpec" type="{http://ws.coverity.com/v6}triageStoreSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createTriageStore", propOrder = {
    "triageStoreSpec"
})
public class CreateTriageStore {

    protected TriageStoreSpecDataObj triageStoreSpec;

    /**
     * Gets the value of the triageStoreSpec property.
     * 
     * @return
     *     possible object is
     *     {@link TriageStoreSpecDataObj }
     *     
     */
    public TriageStoreSpecDataObj getTriageStoreSpec() {
        return triageStoreSpec;
    }

    /**
     * Sets the value of the triageStoreSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriageStoreSpecDataObj }
     *     
     */
    public void setTriageStoreSpec(TriageStoreSpecDataObj value) {
        this.triageStoreSpec = value;
    }

}
