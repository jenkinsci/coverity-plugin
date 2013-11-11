
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for configurationDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="configurationDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="commitPort" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="dbDialect" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dbDriver" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maindbName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maindbUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="maindbUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configurationDataObj", propOrder = {
    "commitPort",
    "dbDialect",
    "dbDriver",
    "maindbName",
    "maindbUrl",
    "maindbUser"
})
public class ConfigurationDataObj {

    protected Long commitPort;
    protected String dbDialect;
    protected String dbDriver;
    protected String maindbName;
    protected String maindbUrl;
    protected String maindbUser;

    /**
     * Gets the value of the commitPort property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCommitPort() {
        return commitPort;
    }

    /**
     * Sets the value of the commitPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCommitPort(Long value) {
        this.commitPort = value;
    }

    /**
     * Gets the value of the dbDialect property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbDialect() {
        return dbDialect;
    }

    /**
     * Sets the value of the dbDialect property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbDialect(String value) {
        this.dbDialect = value;
    }

    /**
     * Gets the value of the dbDriver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbDriver() {
        return dbDriver;
    }

    /**
     * Sets the value of the dbDriver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbDriver(String value) {
        this.dbDriver = value;
    }

    /**
     * Gets the value of the maindbName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaindbName() {
        return maindbName;
    }

    /**
     * Sets the value of the maindbName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaindbName(String value) {
        this.maindbName = value;
    }

    /**
     * Gets the value of the maindbUrl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaindbUrl() {
        return maindbUrl;
    }

    /**
     * Sets the value of the maindbUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaindbUrl(String value) {
        this.maindbUrl = value;
    }

    /**
     * Gets the value of the maindbUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaindbUser() {
        return maindbUser;
    }

    /**
     * Sets the value of the maindbUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaindbUser(String value) {
        this.maindbUser = value;
    }

}
