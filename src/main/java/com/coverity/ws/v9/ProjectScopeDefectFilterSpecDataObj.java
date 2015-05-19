
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
 * <p>Java class for projectScopeDefectFilterSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="projectScopeDefectFilterSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="actionNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerCategoryList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="checkerTypeList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cidList" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="classificationNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="componentIdExclude" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="componentIdList" type="{http://ws.coverity.com/v9}componentIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cweList" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="firstDetectedBy" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="firstDetectedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="firstDetectedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fixTargetNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="impactNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="issueKindList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lastDetectedEndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastDetectedStartDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="legacyNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ownerNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ownerNamePattern" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="severityNameList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "projectScopeDefectFilterSpecDataObj", propOrder = {
    "actionNameList",
    "checkerCategoryList",
    "checkerList",
    "checkerTypeList",
    "cidList",
    "classificationNameList",
    "componentIdExclude",
    "componentIdList",
    "cweList",
    "firstDetectedBy",
    "firstDetectedEndDate",
    "firstDetectedStartDate",
    "fixTargetNameList",
    "impactNameList",
    "issueKindList",
    "lastDetectedEndDate",
    "lastDetectedStartDate",
    "legacyNameList",
    "ownerNameList",
    "ownerNamePattern",
    "severityNameList"
})
public class ProjectScopeDefectFilterSpecDataObj {

    @XmlElement(nillable = true)
    protected List<String> actionNameList;
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
    @XmlElement(nillable = true)
    protected List<String> firstDetectedBy;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetectedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetectedStartDate;
    @XmlElement(nillable = true)
    protected List<String> fixTargetNameList;
    @XmlElement(nillable = true)
    protected List<String> impactNameList;
    @XmlElement(nillable = true)
    protected List<String> issueKindList;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetectedEndDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetectedStartDate;
    @XmlElement(nillable = true)
    protected List<String> legacyNameList;
    @XmlElement(nillable = true)
    protected List<String> ownerNameList;
    protected String ownerNamePattern;
    @XmlElement(nillable = true)
    protected List<String> severityNameList;

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
     * Gets the value of the firstDetectedBy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the firstDetectedBy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFirstDetectedBy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getFirstDetectedBy() {
        if (firstDetectedBy == null) {
            firstDetectedBy = new ArrayList<String>();
        }
        return this.firstDetectedBy;
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

}
