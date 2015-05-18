
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ldapConfigurationSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ldapConfigurationSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anonymousBind" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="baseDN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bindName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bindPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupFilter" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupFullName" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="groupMember" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupObjectClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groupSearchBase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primary" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="secureConnection" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="serverDomain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serverPort" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tlsEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="userEmail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userFirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userLastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userObjectClass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userSearchBase" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ldapConfigurationSpecDataObj", propOrder = {
    "anonymousBind",
    "baseDN",
    "bindName",
    "bindPassword",
    "displayName",
    "groupFilter",
    "groupFullName",
    "groupMember",
    "groupName",
    "groupObjectClass",
    "groupSearchBase",
    "primary",
    "secureConnection",
    "serverDomain",
    "serverPort",
    "tlsEnabled",
    "userEmail",
    "userFirstName",
    "userLastName",
    "userName",
    "userObjectClass",
    "userSearchBase"
})
public class LdapConfigurationSpecDataObj {

    protected boolean anonymousBind;
    protected String baseDN;
    protected String bindName;
    protected String bindPassword;
    protected String displayName;
    protected String groupFilter;
    protected boolean groupFullName;
    protected String groupMember;
    protected String groupName;
    protected String groupObjectClass;
    protected String groupSearchBase;
    protected boolean primary;
    protected boolean secureConnection;
    protected String serverDomain;
    protected Long serverPort;
    protected boolean tlsEnabled;
    protected String userEmail;
    protected String userFirstName;
    protected String userLastName;
    protected String userName;
    protected String userObjectClass;
    protected String userSearchBase;

    /**
     * Gets the value of the anonymousBind property.
     * 
     */
    public boolean isAnonymousBind() {
        return anonymousBind;
    }

    /**
     * Sets the value of the anonymousBind property.
     * 
     */
    public void setAnonymousBind(boolean value) {
        this.anonymousBind = value;
    }

    /**
     * Gets the value of the baseDN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseDN() {
        return baseDN;
    }

    /**
     * Sets the value of the baseDN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseDN(String value) {
        this.baseDN = value;
    }

    /**
     * Gets the value of the bindName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBindName() {
        return bindName;
    }

    /**
     * Sets the value of the bindName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBindName(String value) {
        this.bindName = value;
    }

    /**
     * Gets the value of the bindPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBindPassword() {
        return bindPassword;
    }

    /**
     * Sets the value of the bindPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBindPassword(String value) {
        this.bindPassword = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the groupFilter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupFilter() {
        return groupFilter;
    }

    /**
     * Sets the value of the groupFilter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupFilter(String value) {
        this.groupFilter = value;
    }

    /**
     * Gets the value of the groupFullName property.
     * 
     */
    public boolean isGroupFullName() {
        return groupFullName;
    }

    /**
     * Sets the value of the groupFullName property.
     * 
     */
    public void setGroupFullName(boolean value) {
        this.groupFullName = value;
    }

    /**
     * Gets the value of the groupMember property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupMember() {
        return groupMember;
    }

    /**
     * Sets the value of the groupMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupMember(String value) {
        this.groupMember = value;
    }

    /**
     * Gets the value of the groupName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the value of the groupName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

    /**
     * Gets the value of the groupObjectClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupObjectClass() {
        return groupObjectClass;
    }

    /**
     * Sets the value of the groupObjectClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupObjectClass(String value) {
        this.groupObjectClass = value;
    }

    /**
     * Gets the value of the groupSearchBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupSearchBase() {
        return groupSearchBase;
    }

    /**
     * Sets the value of the groupSearchBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupSearchBase(String value) {
        this.groupSearchBase = value;
    }

    /**
     * Gets the value of the primary property.
     * 
     */
    public boolean isPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     * 
     */
    public void setPrimary(boolean value) {
        this.primary = value;
    }

    /**
     * Gets the value of the secureConnection property.
     * 
     */
    public boolean isSecureConnection() {
        return secureConnection;
    }

    /**
     * Sets the value of the secureConnection property.
     * 
     */
    public void setSecureConnection(boolean value) {
        this.secureConnection = value;
    }

    /**
     * Gets the value of the serverDomain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerDomain() {
        return serverDomain;
    }

    /**
     * Sets the value of the serverDomain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerDomain(String value) {
        this.serverDomain = value;
    }

    /**
     * Gets the value of the serverPort property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getServerPort() {
        return serverPort;
    }

    /**
     * Sets the value of the serverPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setServerPort(Long value) {
        this.serverPort = value;
    }

    /**
     * Gets the value of the tlsEnabled property.
     * 
     */
    public boolean isTlsEnabled() {
        return tlsEnabled;
    }

    /**
     * Sets the value of the tlsEnabled property.
     * 
     */
    public void setTlsEnabled(boolean value) {
        this.tlsEnabled = value;
    }

    /**
     * Gets the value of the userEmail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets the value of the userEmail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserEmail(String value) {
        this.userEmail = value;
    }

    /**
     * Gets the value of the userFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserFirstName() {
        return userFirstName;
    }

    /**
     * Sets the value of the userFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserFirstName(String value) {
        this.userFirstName = value;
    }

    /**
     * Gets the value of the userLastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserLastName() {
        return userLastName;
    }

    /**
     * Sets the value of the userLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserLastName(String value) {
        this.userLastName = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the userObjectClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserObjectClass() {
        return userObjectClass;
    }

    /**
     * Sets the value of the userObjectClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserObjectClass(String value) {
        this.userObjectClass = value;
    }

    /**
     * Gets the value of the userSearchBase property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserSearchBase() {
        return userSearchBase;
    }

    /**
     * Sets the value of the userSearchBase property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserSearchBase(String value) {
        this.userSearchBase = value;
    }

}
