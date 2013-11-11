
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for defectInstanceDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defectInstanceDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="events" type="{http://ws.coverity.com/v6}eventDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="properties" type="{http://ws.coverity.com/v6}propertyDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerSubcategoryId" type="{http://ws.coverity.com/v6}checkerSubcategoryIdDataObj" minOccurs="0"/>
 *         &lt;element name="extra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="function" type="{http://ws.coverity.com/v6}functionInfoDataObj" minOccurs="0"/>
 *         &lt;element name="id" type="{http://ws.coverity.com/v6}defectInstanceIdDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defectInstanceDataObj", propOrder = {
    "events",
    "properties",
    "checkerSubcategoryId",
    "extra",
    "function",
    "id"
})
public class DefectInstanceDataObj {

    @XmlElement(nillable = true)
    protected List<EventDataObj> events;
    @XmlElement(nillable = true)
    protected List<PropertyDataObj> properties;
    protected CheckerSubcategoryIdDataObj checkerSubcategoryId;
    protected String extra;
    protected FunctionInfoDataObj function;
    protected DefectInstanceIdDataObj id;

    /**
     * Gets the value of the events property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the events property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventDataObj }
     * 
     * 
     */
    public List<EventDataObj> getEvents() {
        if (events == null) {
            events = new ArrayList<EventDataObj>();
        }
        return this.events;
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
     * {@link PropertyDataObj }
     * 
     * 
     */
    public List<PropertyDataObj> getProperties() {
        if (properties == null) {
            properties = new ArrayList<PropertyDataObj>();
        }
        return this.properties;
    }

    /**
     * Gets the value of the checkerSubcategoryId property.
     * 
     * @return
     *     possible object is
     *     {@link CheckerSubcategoryIdDataObj }
     *     
     */
    public CheckerSubcategoryIdDataObj getCheckerSubcategoryId() {
        return checkerSubcategoryId;
    }

    /**
     * Sets the value of the checkerSubcategoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckerSubcategoryIdDataObj }
     *     
     */
    public void setCheckerSubcategoryId(CheckerSubcategoryIdDataObj value) {
        this.checkerSubcategoryId = value;
    }

    /**
     * Gets the value of the extra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtra() {
        return extra;
    }

    /**
     * Sets the value of the extra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtra(String value) {
        this.extra = value;
    }

    /**
     * Gets the value of the function property.
     * 
     * @return
     *     possible object is
     *     {@link FunctionInfoDataObj }
     *     
     */
    public FunctionInfoDataObj getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     * 
     * @param value
     *     allowed object is
     *     {@link FunctionInfoDataObj }
     *     
     */
    public void setFunction(FunctionInfoDataObj value) {
        this.function = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link DefectInstanceIdDataObj }
     *     
     */
    public DefectInstanceIdDataObj getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefectInstanceIdDataObj }
     *     
     */
    public void setId(DefectInstanceIdDataObj value) {
        this.id = value;
    }

}
