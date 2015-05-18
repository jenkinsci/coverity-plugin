
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for snapshotScopeSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="snapshotScopeSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="compareOutdatedStreams" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="compareSelector" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="showOutdatedStreams" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="showSelector" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "snapshotScopeSpecDataObj", propOrder = {
    "compareOutdatedStreams",
    "compareSelector",
    "showOutdatedStreams",
    "showSelector"
})
public class SnapshotScopeSpecDataObj {

    @XmlElement(defaultValue = "false")
    protected Boolean compareOutdatedStreams;
    @XmlElement(defaultValue = "")
    protected String compareSelector;
    @XmlElement(defaultValue = "false")
    protected Boolean showOutdatedStreams;
    @XmlElement(required = true, defaultValue = "last()")
    protected String showSelector;

    /**
     * Gets the value of the compareOutdatedStreams property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCompareOutdatedStreams() {
        return compareOutdatedStreams;
    }

    /**
     * Sets the value of the compareOutdatedStreams property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCompareOutdatedStreams(Boolean value) {
        this.compareOutdatedStreams = value;
    }

    /**
     * Gets the value of the compareSelector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompareSelector() {
        return compareSelector;
    }

    /**
     * Sets the value of the compareSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompareSelector(String value) {
        this.compareSelector = value;
    }

    /**
     * Gets the value of the showOutdatedStreams property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isShowOutdatedStreams() {
        return showOutdatedStreams;
    }

    /**
     * Sets the value of the showOutdatedStreams property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setShowOutdatedStreams(Boolean value) {
        this.showOutdatedStreams = value;
    }

    /**
     * Gets the value of the showSelector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShowSelector() {
        return showSelector;
    }

    /**
     * Sets the value of the showSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShowSelector(String value) {
        this.showSelector = value;
    }

}
