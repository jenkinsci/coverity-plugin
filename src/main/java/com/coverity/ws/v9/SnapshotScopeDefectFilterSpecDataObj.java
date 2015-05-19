
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
 * <p>Java class for snapshotScopeDefectFilterSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="snapshotScopeDefectFilterSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actionNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attributeDefinitionValueFilterMap" type="{http://ws.coverity.com/v9}attributeDefinitionValueFilterMapDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerCategoryList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerTypeList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cidList" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="classificationNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentIdExclude" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="componentIdList" type="{http://ws.coverity.com/v9}componentIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cweList" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="externalReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetectedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="firstDetectedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fixTargetNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="functionMergeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="functionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="impactNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="issueComparison" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issueKindList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lastDetectedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastDetectedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="legacyNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="maxOccurrenceCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mergeExtra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mergeKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="minOccurrenceCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ownerNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ownerNamePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="severityNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="statusNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamExcludeNameList" type="{http://ws.coverity.com/v9}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamExcludeQualifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="streamIncludeNameList" type="{http://ws.coverity.com/v9}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamIncludeQualifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "snapshotScopeDefectFilterSpecDataObj", propOrder = {
    "actionNameList",
    "attributeDefinitionValueFilterMap",
    "checkerCategoryList",
    "checkerList",
    "checkerTypeList",
    "cidList",
    "classificationNameList",
    "componentIdExclude",
    "componentIdList",
    "cweList",
    "externalReference",
    "fileName",
    "firstDetectedEndDate",
    "firstDetectedStartDate",
    "fixTargetNameList",
    "functionMergeName",
    "functionName",
    "impactNameList",
    "issueComparison",
    "issueKindList",
    "lastDetectedEndDate",
    "lastDetectedStartDate",
    "legacyNameList",
    "maxOccurrenceCount",
    "mergeExtra",
    "mergeKey",
    "minOccurrenceCount",
    "ownerNameList",
    "ownerNamePattern",
    "severityNameList",
    "statusNameList",
    "streamExcludeNameList",
    "streamExcludeQualifier",
    "streamIncludeNameList",
    "streamIncludeQualifier"
})
public class SnapshotScopeDefectFilterSpecDataObj {

    @XmlElement(nillable = true)
    protected List<String> actionNameList;
    @XmlElement(nillable = true)
    protected List<AttributeDefinitionValueFilterMapDataObj> attributeDefinitionValueFilterMap;
    @XmlElement(nillable = true)
    protected List<String> checkerCategoryList;
    @XmlElement(nillable = true)
    protected List<String> checkerList;
    @XmlElement(nillable = true)
    protected List<String> checkerTypeList;
    @XmlElement(nillable = true)
    protected List<Long> cidList;
    @XmlElement(nillable = true)
    protected List<String> classificationNameList;
    protected Boolean componentIdExclude;
    @XmlElement(nillable = true)
    protected List<ComponentIdDataObj> componentIdList;
    @XmlElement(nillable = true)
    protected List<Long> cweList;
    protected String externalReference;
    protected String fileName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetectedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetectedStartDate;
    @XmlElement(nillable = true)
    protected List<String> fixTargetNameList;
    protected String functionMergeName;
    protected String functionName;
    @XmlElement(nillable = true)
    protected List<String> impactNameList;
    protected String issueComparison;
    @XmlElement(nillable = true)
    protected List<String> issueKindList;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetectedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetectedStartDate;
    @XmlElement(nillable = true)
    protected List<String> legacyNameList;
    protected Integer maxOccurrenceCount;
    protected String mergeExtra;
    protected String mergeKey;
    protected Integer minOccurrenceCount;
    @XmlElement(nillable = true)
    protected List<String> ownerNameList;
    protected String ownerNamePattern;
    @XmlElement(nillable = true)
    protected List<String> severityNameList;
    @XmlElement(nillable = true)
    protected List<String> statusNameList;
    @XmlElement(nillable = true)
    protected List<StreamIdDataObj> streamExcludeNameList;
    @XmlElement(defaultValue = "ALL")
    protected String streamExcludeQualifier;
    @XmlElement(nillable = true)
    protected List<StreamIdDataObj> streamIncludeNameList;
    @XmlElement(defaultValue = "ANY")
    protected String streamIncludeQualifier;

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
     * Gets the value of the checkerCategoryList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checkerCategoryList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheckerCategoryList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCheckerCategoryList() {
        if (checkerCategoryList == null) {
            checkerCategoryList = new ArrayList<String>();
        }
        return this.checkerCategoryList;
    }

    /**
     * Gets the value of the checkerList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checkerList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheckerList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCheckerList() {
        if (checkerList == null) {
            checkerList = new ArrayList<String>();
        }
        return this.checkerList;
    }

    /**
     * Gets the value of the checkerTypeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the checkerTypeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCheckerTypeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCheckerTypeList() {
        if (checkerTypeList == null) {
            checkerTypeList = new ArrayList<String>();
        }
        return this.checkerTypeList;
    }

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
     * Gets the value of the cweList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cweList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCweList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getCweList() {
        if (cweList == null) {
            cweList = new ArrayList<Long>();
        }
        return this.cweList;
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
     * Gets the value of the fileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the value of the fileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFileName(String value) {
        this.fileName = value;
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
     * Gets the value of the functionMergeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionMergeName() {
        return functionMergeName;
    }

    /**
     * Sets the value of the functionMergeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionMergeName(String value) {
        this.functionMergeName = value;
    }

    /**
     * Gets the value of the functionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * Sets the value of the functionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionName(String value) {
        this.functionName = value;
    }

    /**
     * Gets the value of the impactNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the impactNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImpactNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getImpactNameList() {
        if (impactNameList == null) {
            impactNameList = new ArrayList<String>();
        }
        return this.impactNameList;
    }

    /**
     * Gets the value of the issueComparison property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueComparison() {
        return issueComparison;
    }

    /**
     * Sets the value of the issueComparison property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueComparison(String value) {
        this.issueComparison = value;
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
     * Gets the value of the legacyNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the legacyNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLegacyNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getLegacyNameList() {
        if (legacyNameList == null) {
            legacyNameList = new ArrayList<String>();
        }
        return this.legacyNameList;
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
     * Gets the value of the mergeExtra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMergeExtra() {
        return mergeExtra;
    }

    /**
     * Sets the value of the mergeExtra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMergeExtra(String value) {
        this.mergeExtra = value;
    }

    /**
     * Gets the value of the mergeKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMergeKey() {
        return mergeKey;
    }

    /**
     * Sets the value of the mergeKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMergeKey(String value) {
        this.mergeKey = value;
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
     * Gets the value of the streamExcludeNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamExcludeNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamExcludeNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getStreamExcludeNameList() {
        if (streamExcludeNameList == null) {
            streamExcludeNameList = new ArrayList<StreamIdDataObj>();
        }
        return this.streamExcludeNameList;
    }

    /**
     * Gets the value of the streamExcludeQualifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreamExcludeQualifier() {
        return streamExcludeQualifier;
    }

    /**
     * Sets the value of the streamExcludeQualifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreamExcludeQualifier(String value) {
        this.streamExcludeQualifier = value;
    }

    /**
     * Gets the value of the streamIncludeNameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamIncludeNameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamIncludeNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getStreamIncludeNameList() {
        if (streamIncludeNameList == null) {
            streamIncludeNameList = new ArrayList<StreamIdDataObj>();
        }
        return this.streamIncludeNameList;
    }

    /**
     * Gets the value of the streamIncludeQualifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreamIncludeQualifier() {
        return streamIncludeQualifier;
    }

    /**
     * Sets the value of the streamIncludeQualifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreamIncludeQualifier(String value) {
        this.streamIncludeQualifier = value;
    }

}
