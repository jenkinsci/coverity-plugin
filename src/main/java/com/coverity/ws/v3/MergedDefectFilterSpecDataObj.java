package com.coverity.ws.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for mergedDefectFilterSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mergedDefectFilterSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cidList" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerFilterSpecList" type="{http://ws.coverity.com/v3}checkerFilterSpecDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamIdIncludeList" type="{http://ws.coverity.com/v3}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamIdExcludeList" type="{http://ws.coverity.com/v3}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentIdList" type="{http://ws.coverity.com/v3}componentIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="statusNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="classificationNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="actionNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="severityNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ownerNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentIdExclude" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="defectPropertyKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defectPropertyPattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filenamePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetectedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="firstDetectedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="functionNamePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastDetectedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastDetectedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastFixedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastFixedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastTriagedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastTriagedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="maxCid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="minCid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="snapshotId" type="{http://ws.coverity.com/v3}snapshotIdDataObj" minOccurs="0"/>
 *         &lt;element name="streamIdIncludeAll" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mergedDefectFilterSpecDataObj", propOrder = {
    "cidList",
    "checkerFilterSpecList",
    "streamIdIncludeList",
    "streamIdExcludeList",
    "componentIdList",
    "statusNameList",
    "classificationNameList",
    "actionNameList",
    "severityNameList",
    "ownerNameList",
    "componentIdExclude",
    "defectPropertyKey",
    "defectPropertyPattern",
    "filenamePattern",
    "firstDetectedEndDate",
    "firstDetectedStartDate",
    "functionNamePattern",
    "lastDetectedEndDate",
    "lastDetectedStartDate",
    "lastFixedEndDate",
    "lastFixedStartDate",
    "lastTriagedEndDate",
    "lastTriagedStartDate",
    "maxCid",
    "minCid",
    "snapshotId",
    "streamIdIncludeAll"
})
public class MergedDefectFilterSpecDataObj {

    @XmlElement(nillable = true)
    protected List<Long> cidList;
    @XmlElement(nillable = true)
    protected List<CheckerFilterSpecDataObj> checkerFilterSpecList;
    @XmlElement(nillable = true)
    protected List<StreamIdDataObj> streamIdIncludeList;
    @XmlElement(nillable = true)
    protected List<StreamIdDataObj> streamIdExcludeList;
    @XmlElement(nillable = true)
    protected List<ComponentIdDataObj> componentIdList;
    @XmlElement(nillable = true)
    protected List<String> statusNameList;
    @XmlElement(nillable = true)
    protected List<String> classificationNameList;
    @XmlElement(nillable = true)
    protected List<String> actionNameList;
    @XmlElement(nillable = true)
    protected List<String> severityNameList;
    @XmlElement(nillable = true)
    protected List<String> ownerNameList;
    protected Boolean componentIdExclude;
    protected String defectPropertyKey;
    protected String defectPropertyPattern;
    protected String filenamePattern;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetectedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetectedStartDate;
    protected String functionNamePattern;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetectedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetectedStartDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastFixedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastFixedStartDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastTriagedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastTriagedStartDate;
    protected Long maxCid;
    protected Long minCid;
    protected SnapshotIdDataObj snapshotId;
    protected Boolean streamIdIncludeAll;

    /**
     * Gets the value of the cidList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cidList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCidList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getCidList() {
        if (cidList == null) {
            cidList = new ArrayList<Long>();
        }
        return this.cidList;
    }

    /**
     * Gets the value of the checkerFilterSpecList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checkerFilterSpecList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheckerFilterSpecList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link com.coverity.ws.v3.CheckerFilterSpecDataObj }
     * 
     * 
     */
    public List<CheckerFilterSpecDataObj> getCheckerFilterSpecList() {
        if (checkerFilterSpecList == null) {
            checkerFilterSpecList = new ArrayList<CheckerFilterSpecDataObj>();
        }
        return this.checkerFilterSpecList;
    }

    /**
     * Gets the value of the streamIdIncludeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamIdIncludeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamIdIncludeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getStreamIdIncludeList() {
        if (streamIdIncludeList == null) {
            streamIdIncludeList = new ArrayList<StreamIdDataObj>();
        }
        return this.streamIdIncludeList;
    }

    /**
     * Gets the value of the streamIdExcludeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamIdExcludeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamIdExcludeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getStreamIdExcludeList() {
        if (streamIdExcludeList == null) {
            streamIdExcludeList = new ArrayList<StreamIdDataObj>();
        }
        return this.streamIdExcludeList;
    }

    /**
     * Gets the value of the componentIdList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentIdList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentIdList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link com.coverity.ws.v3.ComponentIdDataObj }
     * 
     * 
     */
    public List<ComponentIdDataObj> getComponentIdList() {
        if (componentIdList == null) {
            componentIdList = new ArrayList<ComponentIdDataObj>();
        }
        return this.componentIdList;
    }

    /**
     * Gets the value of the statusNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statusNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatusNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getStatusNameList() {
        if (statusNameList == null) {
            statusNameList = new ArrayList<String>();
        }
        return this.statusNameList;
    }

    /**
     * Gets the value of the classificationNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classificationNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassificationNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClassificationNameList() {
        if (classificationNameList == null) {
            classificationNameList = new ArrayList<String>();
        }
        return this.classificationNameList;
    }

    /**
     * Gets the value of the actionNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getActionNameList() {
        if (actionNameList == null) {
            actionNameList = new ArrayList<String>();
        }
        return this.actionNameList;
    }

    /**
     * Gets the value of the severityNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the severityNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSeverityNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSeverityNameList() {
        if (severityNameList == null) {
            severityNameList = new ArrayList<String>();
        }
        return this.severityNameList;
    }

    /**
     * Gets the value of the ownerNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ownerNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOwnerNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getOwnerNameList() {
        if (ownerNameList == null) {
            ownerNameList = new ArrayList<String>();
        }
        return this.ownerNameList;
    }

    /**
     * Gets the value of the componentIdExclude property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isComponentIdExclude() {
        return componentIdExclude;
    }

    /**
     * Sets the value of the componentIdExclude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setComponentIdExclude(Boolean value) {
        this.componentIdExclude = value;
    }

    /**
     * Gets the value of the defectPropertyKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefectPropertyKey() {
        return defectPropertyKey;
    }

    /**
     * Sets the value of the defectPropertyKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefectPropertyKey(String value) {
        this.defectPropertyKey = value;
    }

    /**
     * Gets the value of the defectPropertyPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDefectPropertyPattern() {
        return defectPropertyPattern;
    }

    /**
     * Sets the value of the defectPropertyPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDefectPropertyPattern(String value) {
        this.defectPropertyPattern = value;
    }

    /**
     * Gets the value of the filenamePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilenamePattern() {
        return filenamePattern;
    }

    /**
     * Sets the value of the filenamePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilenamePattern(String value) {
        this.filenamePattern = value;
    }

    /**
     * Gets the value of the firstDetectedEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFirstDetectedEndDate() {
        return firstDetectedEndDate;
    }

    /**
     * Sets the value of the firstDetectedEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setFirstDetectedEndDate(XMLGregorianCalendar value) {
        this.firstDetectedEndDate = value;
    }

    /**
     * Gets the value of the firstDetectedStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFirstDetectedStartDate() {
        return firstDetectedStartDate;
    }

    /**
     * Sets the value of the firstDetectedStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setFirstDetectedStartDate(XMLGregorianCalendar value) {
        this.firstDetectedStartDate = value;
    }

    /**
     * Gets the value of the functionNamePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionNamePattern() {
        return functionNamePattern;
    }

    /**
     * Sets the value of the functionNamePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionNamePattern(String value) {
        this.functionNamePattern = value;
    }

    /**
     * Gets the value of the lastDetectedEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastDetectedEndDate() {
        return lastDetectedEndDate;
    }

    /**
     * Sets the value of the lastDetectedEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastDetectedEndDate(XMLGregorianCalendar value) {
        this.lastDetectedEndDate = value;
    }

    /**
     * Gets the value of the lastDetectedStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastDetectedStartDate() {
        return lastDetectedStartDate;
    }

    /**
     * Sets the value of the lastDetectedStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastDetectedStartDate(XMLGregorianCalendar value) {
        this.lastDetectedStartDate = value;
    }

    /**
     * Gets the value of the lastFixedEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastFixedEndDate() {
        return lastFixedEndDate;
    }

    /**
     * Sets the value of the lastFixedEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastFixedEndDate(XMLGregorianCalendar value) {
        this.lastFixedEndDate = value;
    }

    /**
     * Gets the value of the lastFixedStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastFixedStartDate() {
        return lastFixedStartDate;
    }

    /**
     * Sets the value of the lastFixedStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastFixedStartDate(XMLGregorianCalendar value) {
        this.lastFixedStartDate = value;
    }

    /**
     * Gets the value of the lastTriagedEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastTriagedEndDate() {
        return lastTriagedEndDate;
    }

    /**
     * Sets the value of the lastTriagedEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastTriagedEndDate(XMLGregorianCalendar value) {
        this.lastTriagedEndDate = value;
    }

    /**
     * Gets the value of the lastTriagedStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastTriagedStartDate() {
        return lastTriagedStartDate;
    }

    /**
     * Sets the value of the lastTriagedStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link javax.xml.datatype.XMLGregorianCalendar }
     *     
     */
    public void setLastTriagedStartDate(XMLGregorianCalendar value) {
        this.lastTriagedStartDate = value;
    }

    /**
     * Gets the value of the maxCid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMaxCid() {
        return maxCid;
    }

    /**
     * Sets the value of the maxCid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMaxCid(Long value) {
        this.maxCid = value;
    }

    /**
     * Gets the value of the minCid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMinCid() {
        return minCid;
    }

    /**
     * Sets the value of the minCid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMinCid(Long value) {
        this.minCid = value;
    }

    /**
     * Gets the value of the snapshotId property.
     * 
     * @return
     *     possible object is
     *     {@link SnapshotIdDataObj }
     *     
     */
    public SnapshotIdDataObj getSnapshotId() {
        return snapshotId;
    }

    /**
     * Sets the value of the snapshotId property.
     * 
     * @param value
     *     allowed object is
     *     {@link SnapshotIdDataObj }
     *     
     */
    public void setSnapshotId(SnapshotIdDataObj value) {
        this.snapshotId = value;
    }

    /**
     * Gets the value of the streamIdIncludeAll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStreamIdIncludeAll() {
        return streamIdIncludeAll;
    }

    /**
     * Sets the value of the streamIdIncludeAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStreamIdIncludeAll(Boolean value) {
        this.streamIdIncludeAll = value;
    }

}
