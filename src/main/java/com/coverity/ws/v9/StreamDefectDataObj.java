
package com.coverity.ws.v9;

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
 *         &lt;element name="checkerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="defectInstances" type="{http://ws.coverity.com/v9}defectInstanceDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="defectStateAttributeValues" type="{http://ws.coverity.com/v9}defectStateAttributeValueDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="history" type="{http://ws.coverity.com/v9}defectStateDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="id" type="{http://ws.coverity.com/v9}streamDefectIdDataObj" minOccurs="0"/>
 *         &lt;element name="streamId" type="{http://ws.coverity.com/v9}streamIdDataObj" minOccurs="0"/>
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
    "checkerName",
    "cid",
    "defectInstances",
    "defectStateAttributeValues",
    "domain",
    "history",
    "id",
    "streamId"
})
public class StreamDefectDataObj {

    protected String checkerName;
    protected Long cid;
    @XmlElement(nillable = true)
    protected List<DefectInstanceDataObj> defectInstances;
    @XmlElement(nillable = true)
    protected List<DefectStateAttributeValueDataObj> defectStateAttributeValues;
    protected String domain;
    @XmlElement(nillable = true)
    protected List<DefectStateDataObj> history;
    protected StreamDefectIdDataObj id;
    protected StreamIdDataObj streamId;

    /**
     * Gets the value of the checkerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckerName() {
        return checkerName;
    }

    /**
     * Sets the value of the checkerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckerName(String value) {
        this.checkerName = value;
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
     * Gets the value of the defectStateAttributeValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defectStateAttributeValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefectStateAttributeValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DefectStateAttributeValueDataObj }
     * 
     * 
     */
    public List<DefectStateAttributeValueDataObj> getDefectStateAttributeValues() {
        if (defectStateAttributeValues == null) {
            defectStateAttributeValues = new ArrayList<DefectStateAttributeValueDataObj>();
        }
        return this.defectStateAttributeValues;
    }

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomain(String value) {
        this.domain = value;
    }

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
