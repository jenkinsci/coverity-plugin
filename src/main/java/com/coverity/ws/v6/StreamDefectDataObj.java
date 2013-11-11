
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for streamDefectDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamDefectDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="history" type="{http://ws.coverity.com/v6}defectStateDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="defectInstances" type="{http://ws.coverity.com/v6}defectInstanceDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkerSubcategoryId" type="{http://ws.coverity.com/v6}checkerSubcategoryIdDataObj" minOccurs="0"/>
 *         &lt;element name="cid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="classification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defectStateCustomAttributeValues" type="{http://ws.coverity.com/v6}defectStateCustomAttributeValueDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="externalReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fixTarget" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="id" type="{http://ws.coverity.com/v6}streamDefectIdDataObj" minOccurs="0"/>
 *         &lt;element name="owner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="severity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="streamId" type="{http://ws.coverity.com/v6}streamIdDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamDefectDataObj", propOrder = {
    "history",
    "defectInstances",
    "action",
    "checkerSubcategoryId",
    "cid",
    "classification",
    "comment",
    "defectStateCustomAttributeValues",
    "externalReference",
    "fixTarget",
    "id",
    "owner",
    "severity",
    "status",
    "streamId"
})
public class StreamDefectDataObj {

    @XmlElement(nillable = true)
    protected List<DefectStateDataObj> history;
    @XmlElement(nillable = true)
    protected List<DefectInstanceDataObj> defectInstances;
    protected String action;
    protected CheckerSubcategoryIdDataObj checkerSubcategoryId;
    protected Long cid;
    protected String classification;
    protected String comment;
    @XmlElement(nillable = true)
    protected List<DefectStateCustomAttributeValueDataObj> defectStateCustomAttributeValues;
    protected String externalReference;
    protected String fixTarget;
    protected StreamDefectIdDataObj id;
    protected String owner;
    protected String severity;
    protected String status;
    protected StreamIdDataObj streamId;

    /**
     * Gets the value of the history property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the history property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefectStateDataObj }
     * 
     * 
     */
    public List<DefectStateDataObj> getHistory() {
        if (history == null) {
            history = new ArrayList<DefectStateDataObj>();
        }
        return this.history;
    }

    /**
     * Gets the value of the defectInstances property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defectInstances property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefectInstances().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefectInstanceDataObj }
     * 
     * 
     */
    public List<DefectInstanceDataObj> getDefectInstances() {
        if (defectInstances == null) {
            defectInstances = new ArrayList<DefectInstanceDataObj>();
        }
        return this.defectInstances;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
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
     * Gets the value of the cid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCid() {
        return cid;
    }

    /**
     * Sets the value of the cid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCid(Long value) {
        this.cid = value;
    }

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassification(String value) {
        this.classification = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the defectStateCustomAttributeValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defectStateCustomAttributeValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefectStateCustomAttributeValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefectStateCustomAttributeValueDataObj }
     * 
     * 
     */
    public List<DefectStateCustomAttributeValueDataObj> getDefectStateCustomAttributeValues() {
        if (defectStateCustomAttributeValues == null) {
            defectStateCustomAttributeValues = new ArrayList<DefectStateCustomAttributeValueDataObj>();
        }
        return this.defectStateCustomAttributeValues;
    }

    /**
     * Gets the value of the externalReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalReference() {
        return externalReference;
    }

    /**
     * Sets the value of the externalReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalReference(String value) {
        this.externalReference = value;
    }

    /**
     * Gets the value of the fixTarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFixTarget() {
        return fixTarget;
    }

    /**
     * Sets the value of the fixTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFixTarget(String value) {
        this.fixTarget = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link StreamDefectIdDataObj }
     *     
     */
    public StreamDefectIdDataObj getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamDefectIdDataObj }
     *     
     */
    public void setId(StreamDefectIdDataObj value) {
        this.id = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Gets the value of the severity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * Sets the value of the severity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeverity(String value) {
        this.severity = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the streamId property.
     * 
     * @return
     *     possible object is
     *     {@link StreamIdDataObj }
     *     
     */
    public StreamIdDataObj getStreamId() {
        return streamId;
    }

    /**
     * Sets the value of the streamId property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamIdDataObj }
     *     
     */
    public void setStreamId(StreamIdDataObj value) {
        this.streamId = value;
    }

}
