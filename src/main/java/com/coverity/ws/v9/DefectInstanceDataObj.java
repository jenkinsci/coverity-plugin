
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for defectInstanceDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="defectInstanceDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="events" type="{http://ws.coverity.com/v9}eventDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="properties" type="{http://ws.coverity.com/v9}propertyDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="category" type="{http://ws.coverity.com/v9}localizedValueDataObj" minOccurs="0"/>
 *         &lt;element name="checkerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="component" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cwe" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eventSetCaptions" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="extra" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="function" type="{http://ws.coverity.com/v9}functionInfoDataObj" minOccurs="0"/>
 *         &lt;element name="id" type="{http://ws.coverity.com/v9}defectInstanceIdDataObj" minOccurs="0"/>
 *         &lt;element name="impact" type="{http://ws.coverity.com/v9}localizedValueDataObj" minOccurs="0"/>
 *         &lt;element name="issueKinds" type="{http://ws.coverity.com/v9}localizedValueDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="localEffect" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="longDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://ws.coverity.com/v9}localizedValueDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "defectInstanceDataObj", propOrder = {
    "events",
    "properties",
    "category",
    "checkerName",
    "component",
    "cwe",
    "domain",
    "eventSetCaptions",
    "extra",
    "function",
    "id",
    "impact",
    "issueKinds",
    "localEffect",
    "longDescription",
    "type"
})
public class DefectInstanceDataObj {

    @XmlElement(nillable = true)
    protected List<EventDataObj> events;
    @XmlElement(nillable = true)
    protected List<PropertyDataObj> properties;
    protected LocalizedValueDataObj category;
    protected String checkerName;
    protected String component;
    protected Integer cwe;
    protected String domain;
    @XmlElement(nillable = true)
    protected List<String> eventSetCaptions;
    protected String extra;
    protected FunctionInfoDataObj function;
    protected DefectInstanceIdDataObj id;
    protected LocalizedValueDataObj impact;
    @XmlElement(nillable = true)
    protected List<LocalizedValueDataObj> issueKinds;
    protected String localEffect;
    protected String longDescription;
    protected LocalizedValueDataObj type;

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
     * Gets the value of the properties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyDataObj }
     * 
     * 
     */
    public List<PropertyDataObj> getProperties() {
        if (properties == null) {
            properties = new ArrayList<PropertyDataObj>();
        }
        return this.properties;
    }

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link LocalizedValueDataObj }
     *     
     */
    public LocalizedValueDataObj getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalizedValueDataObj }
     *     
     */
    public void setCategory(LocalizedValueDataObj value) {
        this.category = value;
    }

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
     * Gets the value of the component property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComponent() {
        return component;
    }

    /**
     * Sets the value of the component property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComponent(String value) {
        this.component = value;
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
     * Gets the value of the eventSetCaptions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventSetCaptions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventSetCaptions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEventSetCaptions() {
        if (eventSetCaptions == null) {
            eventSetCaptions = new ArrayList<String>();
        }
        return this.eventSetCaptions;
    }

    /**
     * Gets the value of the extra property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtra() {
        return extra;
    }

    /**
     * Sets the value of the extra property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtra(String value) {
        this.extra = value;
    }

    /**
     * Gets the value of the function property.
     * 
     * @return
     *     possible object is
     *     {@link FunctionInfoDataObj }
     *     
     */
    public FunctionInfoDataObj getFunction() {
        return function;
    }

    /**
     * Sets the value of the function property.
     * 
     * @param value
     *     allowed object is
     *     {@link FunctionInfoDataObj }
     *     
     */
    public void setFunction(FunctionInfoDataObj value) {
        this.function = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link DefectInstanceIdDataObj }
     *     
     */
    public DefectInstanceIdDataObj getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefectInstanceIdDataObj }
     *     
     */
    public void setId(DefectInstanceIdDataObj value) {
        this.id = value;
    }

    /**
     * Gets the value of the impact property.
     * 
     * @return
     *     possible object is
     *     {@link LocalizedValueDataObj }
     *     
     */
    public LocalizedValueDataObj getImpact() {
        return impact;
    }

    /**
     * Sets the value of the impact property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalizedValueDataObj }
     *     
     */
    public void setImpact(LocalizedValueDataObj value) {
        this.impact = value;
    }

    /**
     * Gets the value of the issueKinds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issueKinds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssueKinds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalizedValueDataObj }
     * 
     * 
     */
    public List<LocalizedValueDataObj> getIssueKinds() {
        if (issueKinds == null) {
            issueKinds = new ArrayList<LocalizedValueDataObj>();
        }
        return this.issueKinds;
    }

    /**
     * Gets the value of the localEffect property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalEffect() {
        return localEffect;
    }

    /**
     * Sets the value of the localEffect property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalEffect(String value) {
        this.localEffect = value;
    }

    /**
     * Gets the value of the longDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Sets the value of the longDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongDescription(String value) {
        this.longDescription = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link LocalizedValueDataObj }
     *     
     */
    public LocalizedValueDataObj getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalizedValueDataObj }
     *     
     */
    public void setType(LocalizedValueDataObj value) {
        this.type = value;
    }

}
