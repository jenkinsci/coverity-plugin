
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateDefectInstanceProperties complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateDefectInstanceProperties">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defectInstanceId" type="{http://ws.coverity.com/v6}defectInstanceIdDataObj" minOccurs="0"/>
 *         &lt;element name="properties" type="{http://ws.coverity.com/v6}propertySpecDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateDefectInstanceProperties", propOrder = {
    "defectInstanceId",
    "properties"
})
public class UpdateDefectInstanceProperties {

    protected DefectInstanceIdDataObj defectInstanceId;
    protected List<PropertySpecDataObj> properties;

    /**
     * Gets the value of the defectInstanceId property.
     * 
     * @return
     *     possible object is
     *     {@link DefectInstanceIdDataObj }
     *     
     */
    public DefectInstanceIdDataObj getDefectInstanceId() {
        return defectInstanceId;
    }

    /**
     * Sets the value of the defectInstanceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefectInstanceIdDataObj }
     *     
     */
    public void setDefectInstanceId(DefectInstanceIdDataObj value) {
        this.defectInstanceId = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertySpecDataObj }
     * 
     * 
     */
    public List<PropertySpecDataObj> getProperties() {
        if (properties == null) {
            properties = new ArrayList<PropertySpecDataObj>();
        }
        return this.properties;
    }

}
