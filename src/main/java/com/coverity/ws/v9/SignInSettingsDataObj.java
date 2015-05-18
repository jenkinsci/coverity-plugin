
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for signInSettingsDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="signInSettingsDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allowPasswordRecovery" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="disableLocalPasswordAuth" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="enableLdapAuth" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="enableSessionTimeout" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ldapUserAutoCreate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="limitFailedSignIns" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="maxFailedSignInAttempts" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="maxSessionIdleTime" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="requireLdapGroupMembership" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signInSettingsDataObj", propOrder = {
    "allowPasswordRecovery",
    "disableLocalPasswordAuth",
    "enableLdapAuth",
    "enableSessionTimeout",
    "ldapUserAutoCreate",
    "limitFailedSignIns",
    "maxFailedSignInAttempts",
    "maxSessionIdleTime",
    "requireLdapGroupMembership"
})
public class SignInSettingsDataObj {

    protected Boolean allowPasswordRecovery;
    protected Boolean disableLocalPasswordAuth;
    protected Boolean enableLdapAuth;
    protected Boolean enableSessionTimeout;
    protected Boolean ldapUserAutoCreate;
    protected Boolean limitFailedSignIns;
    protected Integer maxFailedSignInAttempts;
    protected Integer maxSessionIdleTime;
    protected Boolean requireLdapGroupMembership;

    /**
     * Gets the value of the allowPasswordRecovery property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAllowPasswordRecovery() {
        return allowPasswordRecovery;
    }

    /**
     * Sets the value of the allowPasswordRecovery property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAllowPasswordRecovery(Boolean value) {
        this.allowPasswordRecovery = value;
    }

    /**
     * Gets the value of the disableLocalPasswordAuth property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDisableLocalPasswordAuth() {
        return disableLocalPasswordAuth;
    }

    /**
     * Sets the value of the disableLocalPasswordAuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDisableLocalPasswordAuth(Boolean value) {
        this.disableLocalPasswordAuth = value;
    }

    /**
     * Gets the value of the enableLdapAuth property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnableLdapAuth() {
        return enableLdapAuth;
    }

    /**
     * Sets the value of the enableLdapAuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnableLdapAuth(Boolean value) {
        this.enableLdapAuth = value;
    }

    /**
     * Gets the value of the enableSessionTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEnableSessionTimeout() {
        return enableSessionTimeout;
    }

    /**
     * Sets the value of the enableSessionTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnableSessionTimeout(Boolean value) {
        this.enableSessionTimeout = value;
    }

    /**
     * Gets the value of the ldapUserAutoCreate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLdapUserAutoCreate() {
        return ldapUserAutoCreate;
    }

    /**
     * Sets the value of the ldapUserAutoCreate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLdapUserAutoCreate(Boolean value) {
        this.ldapUserAutoCreate = value;
    }

    /**
     * Gets the value of the limitFailedSignIns property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLimitFailedSignIns() {
        return limitFailedSignIns;
    }

    /**
     * Sets the value of the limitFailedSignIns property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLimitFailedSignIns(Boolean value) {
        this.limitFailedSignIns = value;
    }

    /**
     * Gets the value of the maxFailedSignInAttempts property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxFailedSignInAttempts() {
        return maxFailedSignInAttempts;
    }

    /**
     * Sets the value of the maxFailedSignInAttempts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxFailedSignInAttempts(Integer value) {
        this.maxFailedSignInAttempts = value;
    }

    /**
     * Gets the value of the maxSessionIdleTime property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxSessionIdleTime() {
        return maxSessionIdleTime;
    }

    /**
     * Sets the value of the maxSessionIdleTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxSessionIdleTime(Integer value) {
        this.maxSessionIdleTime = value;
    }

    /**
     * Gets the value of the requireLdapGroupMembership property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequireLdapGroupMembership() {
        return requireLdapGroupMembership;
    }

    /**
     * Sets the value of the requireLdapGroupMembership property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequireLdapGroupMembership(Boolean value) {
        this.requireLdapGroupMembership = value;
    }

}
