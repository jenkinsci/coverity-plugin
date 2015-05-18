
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for backupConfigurationDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="backupConfigurationDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="backupLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="backupTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fridayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="mondayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="saturdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="sundayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="thursdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="tuesdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="wednesdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "backupConfigurationDataObj", propOrder = {
    "backupLocation",
    "backupTime",
    "fridayEnabled",
    "mondayEnabled",
    "saturdayEnabled",
    "sundayEnabled",
    "thursdayEnabled",
    "tuesdayEnabled",
    "wednesdayEnabled"
})
public class BackupConfigurationDataObj {

    protected String backupLocation;
    protected String backupTime;
    protected boolean fridayEnabled;
    protected boolean mondayEnabled;
    protected boolean saturdayEnabled;
    protected boolean sundayEnabled;
    protected boolean thursdayEnabled;
    protected boolean tuesdayEnabled;
    protected boolean wednesdayEnabled;

    /**
     * Gets the value of the backupLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackupLocation() {
        return backupLocation;
    }

    /**
     * Sets the value of the backupLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackupLocation(String value) {
        this.backupLocation = value;
    }

    /**
     * Gets the value of the backupTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackupTime() {
        return backupTime;
    }

    /**
     * Sets the value of the backupTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackupTime(String value) {
        this.backupTime = value;
    }

    /**
     * Gets the value of the fridayEnabled property.
     * 
     */
    public boolean isFridayEnabled() {
        return fridayEnabled;
    }

    /**
     * Sets the value of the fridayEnabled property.
     * 
     */
    public void setFridayEnabled(boolean value) {
        this.fridayEnabled = value;
    }

    /**
     * Gets the value of the mondayEnabled property.
     * 
     */
    public boolean isMondayEnabled() {
        return mondayEnabled;
    }

    /**
     * Sets the value of the mondayEnabled property.
     * 
     */
    public void setMondayEnabled(boolean value) {
        this.mondayEnabled = value;
    }

    /**
     * Gets the value of the saturdayEnabled property.
     * 
     */
    public boolean isSaturdayEnabled() {
        return saturdayEnabled;
    }

    /**
     * Sets the value of the saturdayEnabled property.
     * 
     */
    public void setSaturdayEnabled(boolean value) {
        this.saturdayEnabled = value;
    }

    /**
     * Gets the value of the sundayEnabled property.
     * 
     */
    public boolean isSundayEnabled() {
        return sundayEnabled;
    }

    /**
     * Sets the value of the sundayEnabled property.
     * 
     */
    public void setSundayEnabled(boolean value) {
        this.sundayEnabled = value;
    }

    /**
     * Gets the value of the thursdayEnabled property.
     * 
     */
    public boolean isThursdayEnabled() {
        return thursdayEnabled;
    }

    /**
     * Sets the value of the thursdayEnabled property.
     * 
     */
    public void setThursdayEnabled(boolean value) {
        this.thursdayEnabled = value;
    }

    /**
     * Gets the value of the tuesdayEnabled property.
     * 
     */
    public boolean isTuesdayEnabled() {
        return tuesdayEnabled;
    }

    /**
     * Sets the value of the tuesdayEnabled property.
     * 
     */
    public void setTuesdayEnabled(boolean value) {
        this.tuesdayEnabled = value;
    }

    /**
     * Gets the value of the wednesdayEnabled property.
     * 
     */
    public boolean isWednesdayEnabled() {
        return wednesdayEnabled;
    }

    /**
     * Sets the value of the wednesdayEnabled property.
     * 
     */
    public void setWednesdayEnabled(boolean value) {
        this.wednesdayEnabled = value;
    }

}
