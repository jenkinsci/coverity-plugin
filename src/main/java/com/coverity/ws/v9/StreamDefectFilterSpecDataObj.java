
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for streamDefectFilterSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamDefectFilterSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defectStateEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="defectStateStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="includeDefectInstances" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="includeHistory" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="streamIdList" type="{http://ws.coverity.com/v9}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamDefectFilterSpecDataObj", propOrder = {
    "defectStateEndDate",
    "defectStateStartDate",
    "includeDefectInstances",
    "includeHistory",
    "streamIdList"
})
public class StreamDefectFilterSpecDataObj {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar defectStateEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar defectStateStartDate;
    protected boolean includeDefectInstances;
    protected boolean includeHistory;
    @XmlElement(nillable = true)
    protected List<StreamIdDataObj> streamIdList;

    /**
     * Gets the value of the defectStateEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDefectStateEndDate() {
        return defectStateEndDate;
    }

    /**
     * Sets the value of the defectStateEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDefectStateEndDate(XMLGregorianCalendar value) {
        this.defectStateEndDate = value;
    }

    /**
     * Gets the value of the defectStateStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDefectStateStartDate() {
        return defectStateStartDate;
    }

    /**
     * Sets the value of the defectStateStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDefectStateStartDate(XMLGregorianCalendar value) {
        this.defectStateStartDate = value;
    }

    /**
     * Gets the value of the includeDefectInstances property.
     * 
     */
    public boolean isIncludeDefectInstances() {
        return includeDefectInstances;
    }

    /**
     * Sets the value of the includeDefectInstances property.
     * 
     */
    public void setIncludeDefectInstances(boolean value) {
        this.includeDefectInstances = value;
    }

    /**
     * Gets the value of the includeHistory property.
     * 
     */
    public boolean isIncludeHistory() {
        return includeHistory;
    }

    /**
     * Sets the value of the includeHistory property.
     * 
     */
    public void setIncludeHistory(boolean value) {
        this.includeHistory = value;
    }

    /**
     * Gets the value of the streamIdList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamIdList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamIdList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getStreamIdList() {
        if (streamIdList == null) {
            streamIdList = new ArrayList<StreamIdDataObj>();
        }
        return this.streamIdList;
    }

}
