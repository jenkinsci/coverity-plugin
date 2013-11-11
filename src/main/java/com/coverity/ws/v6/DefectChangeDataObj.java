
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for defectChangeDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defectChangeDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="affectedStreams" type="{http://ws.coverity.com/v6}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="actionChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="classificationChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customAttributeChanges" type="{http://ws.coverity.com/v6}fieldChangeDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dateModified" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="externalReferenceChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="fixTargetChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="ownerChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="severityChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="statusChange" type="{http://ws.coverity.com/v6}fieldChangeDataObj" minOccurs="0"/>
 *         &lt;element name="userModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defectChangeDataObj", propOrder = {
    "affectedStreams",
    "actionChange",
    "classificationChange",
    "comments",
    "customAttributeChanges",
    "dateModified",
    "externalReferenceChange",
    "fixTargetChange",
    "ownerChange",
    "severityChange",
    "statusChange",
    "userModified"
})
public class DefectChangeDataObj {

    @XmlElement(nillable = true)
    protected List<StreamIdDataObj> affectedStreams;
    protected FieldChangeDataObj actionChange;
    protected FieldChangeDataObj classificationChange;
    protected String comments;
    @XmlElement(nillable = true)
    protected List<FieldChangeDataObj> customAttributeChanges;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateModified;
    protected FieldChangeDataObj externalReferenceChange;
    protected FieldChangeDataObj fixTargetChange;
    protected FieldChangeDataObj ownerChange;
    protected FieldChangeDataObj severityChange;
    protected FieldChangeDataObj statusChange;
    protected String userModified;

    /**
     * Gets the value of the affectedStreams property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the affectedStreams property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAffectedStreams().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getAffectedStreams() {
        if (affectedStreams == null) {
            affectedStreams = new ArrayList<StreamIdDataObj>();
        }
        return this.affectedStreams;
    }

    /**
     * Gets the value of the actionChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getActionChange() {
        return actionChange;
    }

    /**
     * Sets the value of the actionChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setActionChange(FieldChangeDataObj value) {
        this.actionChange = value;
    }

    /**
     * Gets the value of the classificationChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getClassificationChange() {
        return classificationChange;
    }

    /**
     * Sets the value of the classificationChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setClassificationChange(FieldChangeDataObj value) {
        this.classificationChange = value;
    }

    /**
     * Gets the value of the comments property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComments() {
        return comments;
    }

    /**
     * Sets the value of the comments property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComments(String value) {
        this.comments = value;
    }

    /**
     * Gets the value of the customAttributeChanges property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customAttributeChanges property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomAttributeChanges().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FieldChangeDataObj }
     * 
     * 
     */
    public List<FieldChangeDataObj> getCustomAttributeChanges() {
        if (customAttributeChanges == null) {
            customAttributeChanges = new ArrayList<FieldChangeDataObj>();
        }
        return this.customAttributeChanges;
    }

    /**
     * Gets the value of the dateModified property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateModified() {
        return dateModified;
    }

    /**
     * Sets the value of the dateModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateModified(XMLGregorianCalendar value) {
        this.dateModified = value;
    }

    /**
     * Gets the value of the externalReferenceChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getExternalReferenceChange() {
        return externalReferenceChange;
    }

    /**
     * Sets the value of the externalReferenceChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setExternalReferenceChange(FieldChangeDataObj value) {
        this.externalReferenceChange = value;
    }

    /**
     * Gets the value of the fixTargetChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getFixTargetChange() {
        return fixTargetChange;
    }

    /**
     * Sets the value of the fixTargetChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setFixTargetChange(FieldChangeDataObj value) {
        this.fixTargetChange = value;
    }

    /**
     * Gets the value of the ownerChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getOwnerChange() {
        return ownerChange;
    }

    /**
     * Sets the value of the ownerChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setOwnerChange(FieldChangeDataObj value) {
        this.ownerChange = value;
    }

    /**
     * Gets the value of the severityChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getSeverityChange() {
        return severityChange;
    }

    /**
     * Sets the value of the severityChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setSeverityChange(FieldChangeDataObj value) {
        this.severityChange = value;
    }

    /**
     * Gets the value of the statusChange property.
     * 
     * @return
     *     possible object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public FieldChangeDataObj getStatusChange() {
        return statusChange;
    }

    /**
     * Sets the value of the statusChange property.
     * 
     * @param value
     *     allowed object is
     *     {@link FieldChangeDataObj }
     *     
     */
    public void setStatusChange(FieldChangeDataObj value) {
        this.statusChange = value;
    }

    /**
     * Gets the value of the userModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserModified() {
        return userModified;
    }

    /**
     * Sets the value of the userModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserModified(String value) {
        this.userModified = value;
    }

}
