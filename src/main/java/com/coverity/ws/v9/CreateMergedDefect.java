
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for createMergedDefect complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createMergedDefect">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mergeKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateOriginated" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="externalPreventVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="internalPreventVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="domainName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createMergedDefect", propOrder = {
    "mergeKey",
    "dateOriginated",
    "externalPreventVersion",
    "internalPreventVersion",
    "checkerName",
    "domainName"
})
public class CreateMergedDefect {

    protected String mergeKey;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOriginated;
    protected String externalPreventVersion;
    protected String internalPreventVersion;
    protected String checkerName;
    protected String domainName;

    /**
     * Gets the value of the mergeKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMergeKey() {
        return mergeKey;
    }

    /**
     * Sets the value of the mergeKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMergeKey(String value) {
        this.mergeKey = value;
    }

    /**
     * Gets the value of the dateOriginated property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOriginated() {
        return dateOriginated;
    }

    /**
     * Sets the value of the dateOriginated property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOriginated(XMLGregorianCalendar value) {
        this.dateOriginated = value;
    }

    /**
     * Gets the value of the externalPreventVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalPreventVersion() {
        return externalPreventVersion;
    }

    /**
     * Sets the value of the externalPreventVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalPreventVersion(String value) {
        this.externalPreventVersion = value;
    }

    /**
     * Gets the value of the internalPreventVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInternalPreventVersion() {
        return internalPreventVersion;
    }

    /**
     * Sets the value of the internalPreventVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInternalPreventVersion(String value) {
        this.internalPreventVersion = value;
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
     * Gets the value of the domainName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * Sets the value of the domainName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainName(String value) {
        this.domainName = value;
    }

}
