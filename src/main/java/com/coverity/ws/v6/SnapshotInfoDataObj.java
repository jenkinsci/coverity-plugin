
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
 * <p>Java class for snapshotInfoDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="snapshotInfoDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="analysisCommandLine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="analysisConfiguration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="analysisHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="analysisInternalVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="analysisTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="analysisVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="anaysisIntermediateDir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildCommandLine" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildConfiguration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildIntermediateDir" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildTime" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="commitUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateCreated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enabledCheckers" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="snapshotId" type="{http://ws.coverity.com/v6}snapshotIdDataObj"/>
 *         &lt;element name="sourceVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="target" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "snapshotInfoDataObj", propOrder = {
    "analysisCommandLine",
    "analysisConfiguration",
    "analysisHost",
    "analysisInternalVersion",
    "analysisTime",
    "analysisVersion",
    "anaysisIntermediateDir",
    "buildCommandLine",
    "buildConfiguration",
    "buildHost",
    "buildIntermediateDir",
    "buildTime",
    "commitUser",
    "dateCreated",
    "description",
    "enabledCheckers",
    "snapshotId",
    "sourceVersion",
    "target"
})
public class SnapshotInfoDataObj {

    protected String analysisCommandLine;
    protected String analysisConfiguration;
    protected String analysisHost;
    protected String analysisInternalVersion;
    protected Long analysisTime;
    protected String analysisVersion;
    protected String anaysisIntermediateDir;
    protected String buildCommandLine;
    protected String buildConfiguration;
    protected String buildHost;
    protected String buildIntermediateDir;
    protected Long buildTime;
    protected String commitUser;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateCreated;
    protected String description;
    @XmlElement(nillable = true)
    protected List<String> enabledCheckers;
    @XmlElement(required = true)
    protected SnapshotIdDataObj snapshotId;
    protected String sourceVersion;
    protected String target;

    /**
     * Gets the value of the analysisCommandLine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisCommandLine() {
        return analysisCommandLine;
    }

    /**
     * Sets the value of the analysisCommandLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisCommandLine(String value) {
        this.analysisCommandLine = value;
    }

    /**
     * Gets the value of the analysisConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisConfiguration() {
        return analysisConfiguration;
    }

    /**
     * Sets the value of the analysisConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisConfiguration(String value) {
        this.analysisConfiguration = value;
    }

    /**
     * Gets the value of the analysisHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisHost() {
        return analysisHost;
    }

    /**
     * Sets the value of the analysisHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisHost(String value) {
        this.analysisHost = value;
    }

    /**
     * Gets the value of the analysisInternalVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisInternalVersion() {
        return analysisInternalVersion;
    }

    /**
     * Sets the value of the analysisInternalVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisInternalVersion(String value) {
        this.analysisInternalVersion = value;
    }

    /**
     * Gets the value of the analysisTime property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAnalysisTime() {
        return analysisTime;
    }

    /**
     * Sets the value of the analysisTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAnalysisTime(Long value) {
        this.analysisTime = value;
    }

    /**
     * Gets the value of the analysisVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisVersion() {
        return analysisVersion;
    }

    /**
     * Sets the value of the analysisVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisVersion(String value) {
        this.analysisVersion = value;
    }

    /**
     * Gets the value of the anaysisIntermediateDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnaysisIntermediateDir() {
        return anaysisIntermediateDir;
    }

    /**
     * Sets the value of the anaysisIntermediateDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnaysisIntermediateDir(String value) {
        this.anaysisIntermediateDir = value;
    }

    /**
     * Gets the value of the buildCommandLine property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildCommandLine() {
        return buildCommandLine;
    }

    /**
     * Sets the value of the buildCommandLine property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildCommandLine(String value) {
        this.buildCommandLine = value;
    }

    /**
     * Gets the value of the buildConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildConfiguration() {
        return buildConfiguration;
    }

    /**
     * Sets the value of the buildConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildConfiguration(String value) {
        this.buildConfiguration = value;
    }

    /**
     * Gets the value of the buildHost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildHost() {
        return buildHost;
    }

    /**
     * Sets the value of the buildHost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildHost(String value) {
        this.buildHost = value;
    }

    /**
     * Gets the value of the buildIntermediateDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildIntermediateDir() {
        return buildIntermediateDir;
    }

    /**
     * Sets the value of the buildIntermediateDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildIntermediateDir(String value) {
        this.buildIntermediateDir = value;
    }

    /**
     * Gets the value of the buildTime property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBuildTime() {
        return buildTime;
    }

    /**
     * Sets the value of the buildTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBuildTime(Long value) {
        this.buildTime = value;
    }

    /**
     * Gets the value of the commitUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommitUser() {
        return commitUser;
    }

    /**
     * Sets the value of the commitUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommitUser(String value) {
        this.commitUser = value;
    }

    /**
     * Gets the value of the dateCreated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateCreated() {
        return dateCreated;
    }

    /**
     * Sets the value of the dateCreated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateCreated(XMLGregorianCalendar value) {
        this.dateCreated = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the enabledCheckers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enabledCheckers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnabledCheckers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEnabledCheckers() {
        if (enabledCheckers == null) {
            enabledCheckers = new ArrayList<String>();
        }
        return this.enabledCheckers;
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
     * Gets the value of the sourceVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceVersion() {
        return sourceVersion;
    }

    /**
     * Sets the value of the sourceVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceVersion(String value) {
        this.sourceVersion = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

}
