
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loggingConfigurationDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loggingConfigurationDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accessControlLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="backgroundLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="commitLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="configurationLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="databaseLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="frameworkLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="internalLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="notificationLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="performanceLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="policyManagerLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="remoteConfigLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="requestPerformanceLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="skeletonizationLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="testAdvisorLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="triageLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="triageSynchLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="webLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="webServicesLogging" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loggingConfigurationDataObj", propOrder = {
    "accessControlLogging",
    "backgroundLogging",
    "commitLogging",
    "configurationLogging",
    "databaseLogging",
    "frameworkLogging",
    "internalLogging",
    "notificationLogging",
    "performanceLogging",
    "policyManagerLogging",
    "remoteConfigLogging",
    "requestPerformanceLogging",
    "skeletonizationLogging",
    "testAdvisorLogging",
    "triageLogging",
    "triageSynchLogging",
    "webLogging",
    "webServicesLogging"
})
public class LoggingConfigurationDataObj {

    protected Boolean accessControlLogging;
    protected Boolean backgroundLogging;
    protected Boolean commitLogging;
    protected Boolean configurationLogging;
    protected Boolean databaseLogging;
    protected Boolean frameworkLogging;
    protected Boolean internalLogging;
    protected Boolean notificationLogging;
    protected Boolean performanceLogging;
    protected Boolean policyManagerLogging;
    protected Boolean remoteConfigLogging;
    protected Boolean requestPerformanceLogging;
    protected Boolean skeletonizationLogging;
    protected Boolean testAdvisorLogging;
    protected Boolean triageLogging;
    protected Boolean triageSynchLogging;
    protected Boolean webLogging;
    protected Boolean webServicesLogging;

    /**
     * Gets the value of the accessControlLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccessControlLogging() {
        return accessControlLogging;
    }

    /**
     * Sets the value of the accessControlLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccessControlLogging(Boolean value) {
        this.accessControlLogging = value;
    }

    /**
     * Gets the value of the backgroundLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBackgroundLogging() {
        return backgroundLogging;
    }

    /**
     * Sets the value of the backgroundLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBackgroundLogging(Boolean value) {
        this.backgroundLogging = value;
    }

    /**
     * Gets the value of the commitLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCommitLogging() {
        return commitLogging;
    }

    /**
     * Sets the value of the commitLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCommitLogging(Boolean value) {
        this.commitLogging = value;
    }

    /**
     * Gets the value of the configurationLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConfigurationLogging() {
        return configurationLogging;
    }

    /**
     * Sets the value of the configurationLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConfigurationLogging(Boolean value) {
        this.configurationLogging = value;
    }

    /**
     * Gets the value of the databaseLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDatabaseLogging() {
        return databaseLogging;
    }

    /**
     * Sets the value of the databaseLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDatabaseLogging(Boolean value) {
        this.databaseLogging = value;
    }

    /**
     * Gets the value of the frameworkLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFrameworkLogging() {
        return frameworkLogging;
    }

    /**
     * Sets the value of the frameworkLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFrameworkLogging(Boolean value) {
        this.frameworkLogging = value;
    }

    /**
     * Gets the value of the internalLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInternalLogging() {
        return internalLogging;
    }

    /**
     * Sets the value of the internalLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInternalLogging(Boolean value) {
        this.internalLogging = value;
    }

    /**
     * Gets the value of the notificationLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNotificationLogging() {
        return notificationLogging;
    }

    /**
     * Sets the value of the notificationLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNotificationLogging(Boolean value) {
        this.notificationLogging = value;
    }

    /**
     * Gets the value of the performanceLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPerformanceLogging() {
        return performanceLogging;
    }

    /**
     * Sets the value of the performanceLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPerformanceLogging(Boolean value) {
        this.performanceLogging = value;
    }

    /**
     * Gets the value of the policyManagerLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPolicyManagerLogging() {
        return policyManagerLogging;
    }

    /**
     * Sets the value of the policyManagerLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPolicyManagerLogging(Boolean value) {
        this.policyManagerLogging = value;
    }

    /**
     * Gets the value of the remoteConfigLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRemoteConfigLogging() {
        return remoteConfigLogging;
    }

    /**
     * Sets the value of the remoteConfigLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRemoteConfigLogging(Boolean value) {
        this.remoteConfigLogging = value;
    }

    /**
     * Gets the value of the requestPerformanceLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequestPerformanceLogging() {
        return requestPerformanceLogging;
    }

    /**
     * Sets the value of the requestPerformanceLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequestPerformanceLogging(Boolean value) {
        this.requestPerformanceLogging = value;
    }

    /**
     * Gets the value of the skeletonizationLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSkeletonizationLogging() {
        return skeletonizationLogging;
    }

    /**
     * Sets the value of the skeletonizationLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSkeletonizationLogging(Boolean value) {
        this.skeletonizationLogging = value;
    }

    /**
     * Gets the value of the testAdvisorLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTestAdvisorLogging() {
        return testAdvisorLogging;
    }

    /**
     * Sets the value of the testAdvisorLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTestAdvisorLogging(Boolean value) {
        this.testAdvisorLogging = value;
    }

    /**
     * Gets the value of the triageLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTriageLogging() {
        return triageLogging;
    }

    /**
     * Sets the value of the triageLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTriageLogging(Boolean value) {
        this.triageLogging = value;
    }

    /**
     * Gets the value of the triageSynchLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTriageSynchLogging() {
        return triageSynchLogging;
    }

    /**
     * Sets the value of the triageSynchLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTriageSynchLogging(Boolean value) {
        this.triageSynchLogging = value;
    }

    /**
     * Gets the value of the webLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWebLogging() {
        return webLogging;
    }

    /**
     * Sets the value of the webLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWebLogging(Boolean value) {
        this.webLogging = value;
    }

    /**
     * Gets the value of the webServicesLogging property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWebServicesLogging() {
        return webServicesLogging;
    }

    /**
     * Sets the value of the webServicesLogging property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWebServicesLogging(Boolean value) {
        this.webServicesLogging = value;
    }

}
