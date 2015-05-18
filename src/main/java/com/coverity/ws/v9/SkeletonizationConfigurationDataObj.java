
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for skeletonizationConfigurationDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="skeletonizationConfigurationDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="daysBeforeSkeletonization" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="fridayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="minSnapshotsToKeep" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="mondayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="saturdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="sundayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="thursdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tuesdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="wednesdayEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skeletonizationConfigurationDataObj", propOrder = {
    "daysBeforeSkeletonization",
    "fridayEnabled",
    "minSnapshotsToKeep",
    "mondayEnabled",
    "saturdayEnabled",
    "sundayEnabled",
    "thursdayEnabled",
    "time",
    "tuesdayEnabled",
    "wednesdayEnabled"
})
public class SkeletonizationConfigurationDataObj {

    protected Integer daysBeforeSkeletonization;
    protected Boolean fridayEnabled;
    protected Integer minSnapshotsToKeep;
    protected Boolean mondayEnabled;
    protected Boolean saturdayEnabled;
    protected Boolean sundayEnabled;
    protected Boolean thursdayEnabled;
    protected String time;
    protected Boolean tuesdayEnabled;
    protected Boolean wednesdayEnabled;

    /**
     * Gets the value of the daysBeforeSkeletonization property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDaysBeforeSkeletonization() {
        return daysBeforeSkeletonization;
    }

    /**
     * Sets the value of the daysBeforeSkeletonization property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDaysBeforeSkeletonization(Integer value) {
        this.daysBeforeSkeletonization = value;
    }

    /**
     * Gets the value of the fridayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFridayEnabled() {
        return fridayEnabled;
    }

    /**
     * Sets the value of the fridayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFridayEnabled(Boolean value) {
        this.fridayEnabled = value;
    }

    /**
     * Gets the value of the minSnapshotsToKeep property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMinSnapshotsToKeep() {
        return minSnapshotsToKeep;
    }

    /**
     * Sets the value of the minSnapshotsToKeep property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMinSnapshotsToKeep(Integer value) {
        this.minSnapshotsToKeep = value;
    }

    /**
     * Gets the value of the mondayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isMondayEnabled() {
        return mondayEnabled;
    }

    /**
     * Sets the value of the mondayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMondayEnabled(Boolean value) {
        this.mondayEnabled = value;
    }

    /**
     * Gets the value of the saturdayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSaturdayEnabled() {
        return saturdayEnabled;
    }

    /**
     * Sets the value of the saturdayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSaturdayEnabled(Boolean value) {
        this.saturdayEnabled = value;
    }

    /**
     * Gets the value of the sundayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSundayEnabled() {
        return sundayEnabled;
    }

    /**
     * Sets the value of the sundayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSundayEnabled(Boolean value) {
        this.sundayEnabled = value;
    }

    /**
     * Gets the value of the thursdayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isThursdayEnabled() {
        return thursdayEnabled;
    }

    /**
     * Sets the value of the thursdayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setThursdayEnabled(Boolean value) {
        this.thursdayEnabled = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTime(String value) {
        this.time = value;
    }

    /**
     * Gets the value of the tuesdayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTuesdayEnabled() {
        return tuesdayEnabled;
    }

    /**
     * Sets the value of the tuesdayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTuesdayEnabled(Boolean value) {
        this.tuesdayEnabled = value;
    }

    /**
     * Gets the value of the wednesdayEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWednesdayEnabled() {
        return wednesdayEnabled;
    }

    /**
     * Sets the value of the wednesdayEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWednesdayEnabled(Boolean value) {
        this.wednesdayEnabled = value;
    }

}
