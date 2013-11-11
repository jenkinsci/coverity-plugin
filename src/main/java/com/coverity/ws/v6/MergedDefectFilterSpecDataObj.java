
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
 *         &lt;element name="checkerSubcategoryFilterSpecList" type="{http://ws.coverity.com/v6}checkerSubcategoryFilterSpecDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="filenamePatternList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamSnapshotFilterSpecIncludeList" type="{http://ws.coverity.com/v6}streamSnapshotFilterSpecDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamSnapshotFilterSpecExcludeList" type="{http://ws.coverity.com/v6}streamSnapshotFilterSpecDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentIdList" type="{http://ws.coverity.com/v6}componentIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="statusNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="classificationNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="actionNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fixTargetNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="severityNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ownerNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="issueKindList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attributeDefinitionValueFilterMap" type="{http://ws.coverity.com/v6}attributeDefinitionValueFilterMapDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentIdExclude" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="defectPropertyKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defectPropertyPattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalReferencePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
 *         &lt;element name="maxOccurrenceCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="minCid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="minOccurrenceCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ownerNamePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="streamSnapshotExcludeAll" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="streamSnapshotIncludeAll" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "checkerSubcategoryFilterSpecList",
    "filenamePatternList",
    "streamSnapshotFilterSpecIncludeList",
    "streamSnapshotFilterSpecExcludeList",
    "componentIdList",
    "statusNameList",
    "classificationNameList",
    "actionNameList",
    "fixTargetNameList",
    "severityNameList",
    "ownerNameList",
    "issueKindList",
    "attributeDefinitionValueFilterMap",
    "componentIdExclude",
    "defectPropertyKey",
    "defectPropertyPattern",
    "externalReferencePattern",
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
    "maxOccurrenceCount",
    "minCid",
    "minOccurrenceCount",
    "ownerNamePattern",
    "streamSnapshotExcludeAll",
    "streamSnapshotIncludeAll"
})
public class MergedDefectFilterSpecDataObj {

    @XmlElement(nillable = true)
    protected List<Long> cidList;
    @XmlElement(nillable = true)
    protected List<CheckerSubcategoryFilterSpecDataObj> checkerSubcategoryFilterSpecList;
    @XmlElement(nillable = true)
    protected List<String> filenamePatternList;
    @XmlElement(nillable = true)
    protected List<StreamSnapshotFilterSpecDataObj> streamSnapshotFilterSpecIncludeList;
    @XmlElement(nillable = true)
    protected List<StreamSnapshotFilterSpecDataObj> streamSnapshotFilterSpecExcludeList;
    @XmlElement(nillable = true)
    protected List<ComponentIdDataObj> componentIdList;
    @XmlElement(nillable = true)
    protected List<String> statusNameList;
    @XmlElement(nillable = true)
    protected List<String> classificationNameList;
    @XmlElement(nillable = true)
    protected List<String> actionNameList;
    @XmlElement(nillable = true)
    protected List<String> fixTargetNameList;
    @XmlElement(nillable = true)
    protected List<String> severityNameList;
    @XmlElement(nillable = true)
    protected List<String> ownerNameList;
    @XmlElement(nillable = true)
    protected List<String> issueKindList;
    @XmlElement(nillable = true)
    protected List<AttributeDefinitionValueFilterMapDataObj> attributeDefinitionValueFilterMap;
    protected Boolean componentIdExclude;
    protected String defectPropertyKey;
    protected String defectPropertyPattern;
    protected String externalReferencePattern;
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
    protected Integer maxOccurrenceCount;
    protected Long minCid;
    protected Integer minOccurrenceCount;
    protected String ownerNamePattern;
    protected Boolean streamSnapshotExcludeAll;
    protected Boolean streamSnapshotIncludeAll;

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
     * Gets the value of the checkerSubcategoryFilterSpecList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checkerSubcategoryFilterSpecList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheckerSubcategoryFilterSpecList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CheckerSubcategoryFilterSpecDataObj }
     * 
     * 
     */
    public List<CheckerSubcategoryFilterSpecDataObj> getCheckerSubcategoryFilterSpecList() {
        if (checkerSubcategoryFilterSpecList == null) {
            checkerSubcategoryFilterSpecList = new ArrayList<CheckerSubcategoryFilterSpecDataObj>();
        }
        return this.checkerSubcategoryFilterSpecList;
    }

    /**
     * Gets the value of the filenamePatternList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the filenamePatternList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilenamePatternList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFilenamePatternList() {
        if (filenamePatternList == null) {
            filenamePatternList = new ArrayList<String>();
        }
        return this.filenamePatternList;
    }

    /**
     * Gets the value of the streamSnapshotFilterSpecIncludeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamSnapshotFilterSpecIncludeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamSnapshotFilterSpecIncludeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamSnapshotFilterSpecDataObj }
     * 
     * 
     */
    public List<StreamSnapshotFilterSpecDataObj> getStreamSnapshotFilterSpecIncludeList() {
        if (streamSnapshotFilterSpecIncludeList == null) {
            streamSnapshotFilterSpecIncludeList = new ArrayList<StreamSnapshotFilterSpecDataObj>();
        }
        return this.streamSnapshotFilterSpecIncludeList;
    }

    /**
     * Gets the value of the streamSnapshotFilterSpecExcludeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamSnapshotFilterSpecExcludeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamSnapshotFilterSpecExcludeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamSnapshotFilterSpecDataObj }
     * 
     * 
     */
    public List<StreamSnapshotFilterSpecDataObj> getStreamSnapshotFilterSpecExcludeList() {
        if (streamSnapshotFilterSpecExcludeList == null) {
            streamSnapshotFilterSpecExcludeList = new ArrayList<StreamSnapshotFilterSpecDataObj>();
        }
        return this.streamSnapshotFilterSpecExcludeList;
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
     * {@link ComponentIdDataObj }
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
     * Gets the value of the fixTargetNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fixTargetNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFixTargetNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFixTargetNameList() {
        if (fixTargetNameList == null) {
            fixTargetNameList = new ArrayList<String>();
        }
        return this.fixTargetNameList;
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
     * Gets the value of the issueKindList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issueKindList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssueKindList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIssueKindList() {
        if (issueKindList == null) {
            issueKindList = new ArrayList<String>();
        }
        return this.issueKindList;
    }

    /**
     * Gets the value of the attributeDefinitionValueFilterMap property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeDefinitionValueFilterMap property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeDefinitionValueFilterMap().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeDefinitionValueFilterMapDataObj }
     * 
     * 
     */
    public List<AttributeDefinitionValueFilterMapDataObj> getAttributeDefinitionValueFilterMap() {
        if (attributeDefinitionValueFilterMap == null) {
            attributeDefinitionValueFilterMap = new ArrayList<AttributeDefinitionValueFilterMapDataObj>();
        }
        return this.attributeDefinitionValueFilterMap;
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
     * Gets the value of the externalReferencePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalReferencePattern() {
        return externalReferencePattern;
    }

    /**
     * Sets the value of the externalReferencePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalReferencePattern(String value) {
        this.externalReferencePattern = value;
    }

    /**
     * Gets the value of the firstDetectedEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     *     {@link XMLGregorianCalendar }
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
     * Gets the value of the maxOccurrenceCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxOccurrenceCount() {
        return maxOccurrenceCount;
    }

    /**
     * Sets the value of the maxOccurrenceCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxOccurrenceCount(Integer value) {
        this.maxOccurrenceCount = value;
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
     * Gets the value of the minOccurrenceCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinOccurrenceCount() {
        return minOccurrenceCount;
    }

    /**
     * Sets the value of the minOccurrenceCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinOccurrenceCount(Integer value) {
        this.minOccurrenceCount = value;
    }

    /**
     * Gets the value of the ownerNamePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwnerNamePattern() {
        return ownerNamePattern;
    }

    /**
     * Sets the value of the ownerNamePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwnerNamePattern(String value) {
        this.ownerNamePattern = value;
    }

    /**
     * Gets the value of the streamSnapshotExcludeAll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStreamSnapshotExcludeAll() {
        return streamSnapshotExcludeAll;
    }

    /**
     * Sets the value of the streamSnapshotExcludeAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStreamSnapshotExcludeAll(Boolean value) {
        this.streamSnapshotExcludeAll = value;
    }

    /**
     * Gets the value of the streamSnapshotIncludeAll property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStreamSnapshotIncludeAll() {
        return streamSnapshotIncludeAll;
    }

    /**
     * Sets the value of the streamSnapshotIncludeAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStreamSnapshotIncludeAll(Boolean value) {
        this.streamSnapshotIncludeAll = value;
    }

}
