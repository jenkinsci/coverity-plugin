
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for eventDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="eventDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="events" type="{http://ws.coverity.com/v9}eventDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="eventDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eventKind" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eventNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="eventSet" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="eventTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fileId" type="{http://ws.coverity.com/v9}fileIdDataObj" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="lineNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="main" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="moreInformationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pathCondition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="polarity" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eventDataObj", propOrder = {
    "events",
    "eventDescription",
    "eventKind",
    "eventNumber",
    "eventSet",
    "eventTag",
    "fileId",
    "id",
    "lineNumber",
    "main",
    "moreInformationId",
    "pathCondition",
    "polarity"
})
public class EventDataObj {

    @XmlElement(nillable = true)
    protected List<EventDataObj> events;
    protected String eventDescription;
    protected String eventKind;
    protected int eventNumber;
    protected int eventSet;
    protected String eventTag;
    protected FileIdDataObj fileId;
    protected Long id;
    protected int lineNumber;
    protected boolean main;
    protected String moreInformationId;
    protected String pathCondition;
    protected boolean polarity;

    /**
     * Gets the value of the events property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the events property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEvents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventDataObj }
     * 
     * 
     */
    public List<EventDataObj> getEvents() {
        if (events == null) {
            events = new ArrayList<EventDataObj>();
        }
        return this.events;
    }

    /**
     * Gets the value of the eventDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventDescription() {
        return eventDescription;
    }

    /**
     * Sets the value of the eventDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventDescription(String value) {
        this.eventDescription = value;
    }

    /**
     * Gets the value of the eventKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventKind() {
        return eventKind;
    }

    /**
     * Sets the value of the eventKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventKind(String value) {
        this.eventKind = value;
    }

    /**
     * Gets the value of the eventNumber property.
     * 
     */
    public int getEventNumber() {
        return eventNumber;
    }

    /**
     * Sets the value of the eventNumber property.
     * 
     */
    public void setEventNumber(int value) {
        this.eventNumber = value;
    }

    /**
     * Gets the value of the eventSet property.
     * 
     */
    public int getEventSet() {
        return eventSet;
    }

    /**
     * Sets the value of the eventSet property.
     * 
     */
    public void setEventSet(int value) {
        this.eventSet = value;
    }

    /**
     * Gets the value of the eventTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventTag() {
        return eventTag;
    }

    /**
     * Sets the value of the eventTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventTag(String value) {
        this.eventTag = value;
    }

    /**
     * Gets the value of the fileId property.
     * 
     * @return
     *     possible object is
     *     {@link FileIdDataObj }
     *     
     */
    public FileIdDataObj getFileId() {
        return fileId;
    }

    /**
     * Sets the value of the fileId property.
     * 
     * @param value
     *     allowed object is
     *     {@link FileIdDataObj }
     *     
     */
    public void setFileId(FileIdDataObj value) {
        this.fileId = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the lineNumber property.
     * 
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the value of the lineNumber property.
     * 
     */
    public void setLineNumber(int value) {
        this.lineNumber = value;
    }

    /**
     * Gets the value of the main property.
     * 
     */
    public boolean isMain() {
        return main;
    }

    /**
     * Sets the value of the main property.
     * 
     */
    public void setMain(boolean value) {
        this.main = value;
    }

    /**
     * Gets the value of the moreInformationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoreInformationId() {
        return moreInformationId;
    }

    /**
     * Sets the value of the moreInformationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoreInformationId(String value) {
        this.moreInformationId = value;
    }

    /**
     * Gets the value of the pathCondition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPathCondition() {
        return pathCondition;
    }

    /**
     * Sets the value of the pathCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPathCondition(String value) {
        this.pathCondition = value;
    }

    /**
     * Gets the value of the polarity property.
     * 
     */
    public boolean isPolarity() {
        return polarity;
    }

    /**
     * Sets the value of the polarity property.
     * 
     */
    public void setPolarity(boolean value) {
        this.polarity = value;
    }

}
