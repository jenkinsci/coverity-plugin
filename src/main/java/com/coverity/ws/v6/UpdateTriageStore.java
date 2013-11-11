
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateTriageStore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateTriageStore">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="triageStoreId" type="{http://ws.coverity.com/v6}triageStoreIdDataObj" minOccurs="0"/>
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
@XmlType(name = "updateTriageStore", propOrder = {
    "triageStoreId",
    "triageStoreSpec"
})
public class UpdateTriageStore {

    protected TriageStoreIdDataObj triageStoreId;
    protected TriageStoreSpecDataObj triageStoreSpec;

    /**
     * Gets the value of the triageStoreId property.
     * 
     * @return
     *     possible object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public TriageStoreIdDataObj getTriageStoreId() {
        return triageStoreId;
    }

    /**
     * Sets the value of the triageStoreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public void setTriageStoreId(TriageStoreIdDataObj value) {
        this.triageStoreId = value;
    }

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
