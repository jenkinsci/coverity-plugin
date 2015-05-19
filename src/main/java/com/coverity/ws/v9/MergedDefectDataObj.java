
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
 * <p>Java class for mergedDefectDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mergedDefectDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="checkerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="componentName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cwe" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="defectStateAttributeValues" type="{http://ws.coverity.com/v9}defectStateAttributeValueDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="displayCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayImpact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayIssueKind" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filePathname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetected" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="firstDetectedBy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetectedDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetectedSnapshotId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="firstDetectedStream" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetectedTarget" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firstDetectedVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="functionDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="functionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issueKind" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastDetected" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastDetectedDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastDetectedSnapshotId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="lastDetectedStream" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastDetectedTarget" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastDetectedVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastFixed" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="lastTriaged" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="mergeKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="occurrenceCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mergedDefectDataObj", propOrder = {
    "checkerName",
    "cid",
    "componentName",
    "cwe",
    "defectStateAttributeValues",
    "displayCategory",
    "displayImpact",
    "displayIssueKind",
    "displayType",
    "domain",
    "filePathname",
    "firstDetected",
    "firstDetectedBy",
    "firstDetectedDescription",
    "firstDetectedSnapshotId",
    "firstDetectedStream",
    "firstDetectedTarget",
    "firstDetectedVersion",
    "functionDisplayName",
    "functionName",
    "issueKind",
    "lastDetected",
    "lastDetectedDescription",
    "lastDetectedSnapshotId",
    "lastDetectedStream",
    "lastDetectedTarget",
    "lastDetectedVersion",
    "lastFixed",
    "lastTriaged",
    "mergeKey",
    "occurrenceCount"
})
public class MergedDefectDataObj {

    protected String checkerName;
    protected Long cid;
    protected String componentName;
    protected Integer cwe;
    @XmlElement(nillable = true)
    protected List<DefectStateAttributeValueDataObj> defectStateAttributeValues;
    protected String displayCategory;
    protected String displayImpact;
    protected String displayIssueKind;
    protected String displayType;
    protected String domain;
    protected String filePathname;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar firstDetected;
    protected String firstDetectedBy;
    protected String firstDetectedDescription;
    protected Long firstDetectedSnapshotId;
    protected String firstDetectedStream;
    protected String firstDetectedTarget;
    protected String firstDetectedVersion;
    protected String functionDisplayName;
    protected String functionName;
    protected String issueKind;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastDetected;
    protected String lastDetectedDescription;
    protected Long lastDetectedSnapshotId;
    protected String lastDetectedStream;
    protected String lastDetectedTarget;
    protected String lastDetectedVersion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastFixed;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastTriaged;
    protected String mergeKey;
    protected Integer occurrenceCount;

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
     * Gets the value of the componentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Sets the value of the componentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponentName(String value) {
        this.componentName = value;
    }

    /**
     * Gets the value of the cwe property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCwe() {
        return cwe;
    }

    /**
     * Sets the value of the cwe property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCwe(Integer value) {
        this.cwe = value;
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
     * Gets the value of the displayCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayCategory() {
        return displayCategory;
    }

    /**
     * Sets the value of the displayCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayCategory(String value) {
        this.displayCategory = value;
    }

    /**
     * Gets the value of the displayImpact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayImpact() {
        return displayImpact;
    }

    /**
     * Sets the value of the displayImpact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayImpact(String value) {
        this.displayImpact = value;
    }

    /**
     * Gets the value of the displayIssueKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayIssueKind() {
        return displayIssueKind;
    }

    /**
     * Sets the value of the displayIssueKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayIssueKind(String value) {
        this.displayIssueKind = value;
    }

    /**
     * Gets the value of the displayType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayType() {
        return displayType;
    }

    /**
     * Sets the value of the displayType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayType(String value) {
        this.displayType = value;
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
     * Gets the value of the filePathname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilePathname() {
        return filePathname;
    }

    /**
     * Sets the value of the filePathname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilePathname(String value) {
        this.filePathname = value;
    }

    /**
     * Gets the value of the firstDetected property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFirstDetected() {
        return firstDetected;
    }

    /**
     * Sets the value of the firstDetected property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFirstDetected(XMLGregorianCalendar value) {
        this.firstDetected = value;
    }

    /**
     * Gets the value of the firstDetectedBy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstDetectedBy() {
        return firstDetectedBy;
    }

    /**
     * Sets the value of the firstDetectedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstDetectedBy(String value) {
        this.firstDetectedBy = value;
    }

    /**
     * Gets the value of the firstDetectedDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstDetectedDescription() {
        return firstDetectedDescription;
    }

    /**
     * Sets the value of the firstDetectedDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstDetectedDescription(String value) {
        this.firstDetectedDescription = value;
    }

    /**
     * Gets the value of the firstDetectedSnapshotId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFirstDetectedSnapshotId() {
        return firstDetectedSnapshotId;
    }

    /**
     * Sets the value of the firstDetectedSnapshotId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFirstDetectedSnapshotId(Long value) {
        this.firstDetectedSnapshotId = value;
    }

    /**
     * Gets the value of the firstDetectedStream property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstDetectedStream() {
        return firstDetectedStream;
    }

    /**
     * Sets the value of the firstDetectedStream property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstDetectedStream(String value) {
        this.firstDetectedStream = value;
    }

    /**
     * Gets the value of the firstDetectedTarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstDetectedTarget() {
        return firstDetectedTarget;
    }

    /**
     * Sets the value of the firstDetectedTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstDetectedTarget(String value) {
        this.firstDetectedTarget = value;
    }

    /**
     * Gets the value of the firstDetectedVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstDetectedVersion() {
        return firstDetectedVersion;
    }

    /**
     * Sets the value of the firstDetectedVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstDetectedVersion(String value) {
        this.firstDetectedVersion = value;
    }

    /**
     * Gets the value of the functionDisplayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFunctionDisplayName() {
        return functionDisplayName;
    }

    /**
     * Sets the value of the functionDisplayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFunctionDisplayName(String value) {
        this.functionDisplayName = value;
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
     * Gets the value of the issueKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueKind() {
        return issueKind;
    }

    /**
     * Sets the value of the issueKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueKind(String value) {
        this.issueKind = value;
    }

    /**
     * Gets the value of the lastDetected property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastDetected() {
        return lastDetected;
    }

    /**
     * Sets the value of the lastDetected property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastDetected(XMLGregorianCalendar value) {
        this.lastDetected = value;
    }

    /**
     * Gets the value of the lastDetectedDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastDetectedDescription() {
        return lastDetectedDescription;
    }

    /**
     * Sets the value of the lastDetectedDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastDetectedDescription(String value) {
        this.lastDetectedDescription = value;
    }

    /**
     * Gets the value of the lastDetectedSnapshotId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLastDetectedSnapshotId() {
        return lastDetectedSnapshotId;
    }

    /**
     * Sets the value of the lastDetectedSnapshotId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLastDetectedSnapshotId(Long value) {
        this.lastDetectedSnapshotId = value;
    }

    /**
     * Gets the value of the lastDetectedStream property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastDetectedStream() {
        return lastDetectedStream;
    }

    /**
     * Sets the value of the lastDetectedStream property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastDetectedStream(String value) {
        this.lastDetectedStream = value;
    }

    /**
     * Gets the value of the lastDetectedTarget property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastDetectedTarget() {
        return lastDetectedTarget;
    }

    /**
     * Sets the value of the lastDetectedTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastDetectedTarget(String value) {
        this.lastDetectedTarget = value;
    }

    /**
     * Gets the value of the lastDetectedVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastDetectedVersion() {
        return lastDetectedVersion;
    }

    /**
     * Sets the value of the lastDetectedVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastDetectedVersion(String value) {
        this.lastDetectedVersion = value;
    }

    /**
     * Gets the value of the lastFixed property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastFixed() {
        return lastFixed;
    }

    /**
     * Sets the value of the lastFixed property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastFixed(XMLGregorianCalendar value) {
        this.lastFixed = value;
    }

    /**
     * Gets the value of the lastTriaged property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastTriaged() {
        return lastTriaged;
    }

    /**
     * Sets the value of the lastTriaged property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastTriaged(XMLGregorianCalendar value) {
        this.lastTriaged = value;
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
     * Gets the value of the occurrenceCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOccurrenceCount() {
        return occurrenceCount;
    }

    /**
     * Sets the value of the occurrenceCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOccurrenceCount(Integer value) {
        this.occurrenceCount = value;
    }

}
