
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for streamSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allowCommitWithoutPassword" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="analysisVersionOverride" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autoDeleteOnExpiry" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="componentMapId" type="{http://ws.coverity.com/v9}componentMapIdDataObj" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enableDesktopAnalysis" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="outdated" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="pluginVersionOverride" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="roleAssignments" type="{http://ws.coverity.com/v9}roleAssignmentDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="summaryExpirationDays" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="triageStoreId" type="{http://ws.coverity.com/v9}triageStoreIdDataObj" minOccurs="0"/>
 *         &lt;element name="versionMismatchMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamSpecDataObj", propOrder = {
    "allowCommitWithoutPassword",
    "analysisVersionOverride",
    "autoDeleteOnExpiry",
    "componentMapId",
    "description",
    "enableDesktopAnalysis",
    "language",
    "name",
    "outdated",
    "pluginVersionOverride",
    "roleAssignments",
    "summaryExpirationDays",
    "triageStoreId",
    "versionMismatchMessage"
})
public class StreamSpecDataObj {

    protected Boolean allowCommitWithoutPassword;
    protected String analysisVersionOverride;
    protected Boolean autoDeleteOnExpiry;
    protected ComponentMapIdDataObj componentMapId;
    protected String description;
    protected Boolean enableDesktopAnalysis;
    protected String language;
    protected String name;
    protected Boolean outdated;
    protected String pluginVersionOverride;
    @XmlElement(nillable = true)
    protected List<RoleAssignmentDataObj> roleAssignments;
    protected Integer summaryExpirationDays;
    protected TriageStoreIdDataObj triageStoreId;
    protected String versionMismatchMessage;

    /**
     * Gets the value of the allowCommitWithoutPassword property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAllowCommitWithoutPassword() {
        return allowCommitWithoutPassword;
    }

    /**
     * Sets the value of the allowCommitWithoutPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAllowCommitWithoutPassword(Boolean value) {
        this.allowCommitWithoutPassword = value;
    }

    /**
     * Gets the value of the analysisVersionOverride property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalysisVersionOverride() {
        return analysisVersionOverride;
    }

    /**
     * Sets the value of the analysisVersionOverride property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalysisVersionOverride(String value) {
        this.analysisVersionOverride = value;
    }

    /**
     * Gets the value of the autoDeleteOnExpiry property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoDeleteOnExpiry() {
        return autoDeleteOnExpiry;
    }

    /**
     * Sets the value of the autoDeleteOnExpiry property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoDeleteOnExpiry(Boolean value) {
        this.autoDeleteOnExpiry = value;
    }

    /**
     * Gets the value of the componentMapId property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentMapIdDataObj }
     *     
     */
    public ComponentMapIdDataObj getComponentMapId() {
        return componentMapId;
    }

    /**
     * Sets the value of the componentMapId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentMapIdDataObj }
     *     
     */
    public void setComponentMapId(ComponentMapIdDataObj value) {
        this.componentMapId = value;
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
     * Gets the value of the enableDesktopAnalysis property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnableDesktopAnalysis() {
        return enableDesktopAnalysis;
    }

    /**
     * Sets the value of the enableDesktopAnalysis property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnableDesktopAnalysis(Boolean value) {
        this.enableDesktopAnalysis = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the outdated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOutdated() {
        return outdated;
    }

    /**
     * Sets the value of the outdated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOutdated(Boolean value) {
        this.outdated = value;
    }

    /**
     * Gets the value of the pluginVersionOverride property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPluginVersionOverride() {
        return pluginVersionOverride;
    }

    /**
     * Sets the value of the pluginVersionOverride property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPluginVersionOverride(String value) {
        this.pluginVersionOverride = value;
    }

    /**
     * Gets the value of the roleAssignments property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roleAssignments property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoleAssignments().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoleAssignmentDataObj }
     * 
     * 
     */
    public List<RoleAssignmentDataObj> getRoleAssignments() {
        if (roleAssignments == null) {
            roleAssignments = new ArrayList<RoleAssignmentDataObj>();
        }
        return this.roleAssignments;
    }

    /**
     * Gets the value of the summaryExpirationDays property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSummaryExpirationDays() {
        return summaryExpirationDays;
    }

    /**
     * Sets the value of the summaryExpirationDays property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSummaryExpirationDays(Integer value) {
        this.summaryExpirationDays = value;
    }

    /**
     * Gets the value of the triageStoreId property.
     * 
     * @return
     *     possible object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public TriageStoreIdDataObj getTriageStoreId() {
        return triageStoreId;
    }

    /**
     * Sets the value of the triageStoreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public void setTriageStoreId(TriageStoreIdDataObj value) {
        this.triageStoreId = value;
    }

    /**
     * Gets the value of the versionMismatchMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionMismatchMessage() {
        return versionMismatchMessage;
    }

    /**
     * Sets the value of the versionMismatchMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionMismatchMessage(String value) {
        this.versionMismatchMessage = value;
    }

}
