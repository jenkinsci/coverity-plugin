
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for streamFunctionDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamFunctionDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="astDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="astHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="calleesDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="calleesHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="filePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="globalsDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="globalsHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mangledName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="summaryDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="summaryHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="typesDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="typesHash" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamFunctionDataObj", propOrder = {
    "astDate",
    "astHash",
    "calleesDate",
    "calleesHash",
    "filePath",
    "globalsDate",
    "globalsHash",
    "mangledName",
    "summaryDate",
    "summaryHash",
    "typesDate",
    "typesHash"
})
public class StreamFunctionDataObj {

    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar astDate;
    protected String astHash;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar calleesDate;
    protected String calleesHash;
    protected String filePath;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar globalsDate;
    protected String globalsHash;
    protected String mangledName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar summaryDate;
    protected String summaryHash;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar typesDate;
    protected String typesHash;

    /**
     * Gets the value of the astDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAstDate() {
        return astDate;
    }

    /**
     * Sets the value of the astDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAstDate(XMLGregorianCalendar value) {
        this.astDate = value;
    }

    /**
     * Gets the value of the astHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAstHash() {
        return astHash;
    }

    /**
     * Sets the value of the astHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAstHash(String value) {
        this.astHash = value;
    }

    /**
     * Gets the value of the calleesDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCalleesDate() {
        return calleesDate;
    }

    /**
     * Sets the value of the calleesDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCalleesDate(XMLGregorianCalendar value) {
        this.calleesDate = value;
    }

    /**
     * Gets the value of the calleesHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalleesHash() {
        return calleesHash;
    }

    /**
     * Sets the value of the calleesHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalleesHash(String value) {
        this.calleesHash = value;
    }

    /**
     * Gets the value of the filePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the value of the filePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilePath(String value) {
        this.filePath = value;
    }

    /**
     * Gets the value of the globalsDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGlobalsDate() {
        return globalsDate;
    }

    /**
     * Sets the value of the globalsDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGlobalsDate(XMLGregorianCalendar value) {
        this.globalsDate = value;
    }

    /**
     * Gets the value of the globalsHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGlobalsHash() {
        return globalsHash;
    }

    /**
     * Sets the value of the globalsHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGlobalsHash(String value) {
        this.globalsHash = value;
    }

    /**
     * Gets the value of the mangledName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMangledName() {
        return mangledName;
    }

    /**
     * Sets the value of the mangledName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMangledName(String value) {
        this.mangledName = value;
    }

    /**
     * Gets the value of the summaryDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSummaryDate() {
        return summaryDate;
    }

    /**
     * Sets the value of the summaryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSummaryDate(XMLGregorianCalendar value) {
        this.summaryDate = value;
    }

    /**
     * Gets the value of the summaryHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSummaryHash() {
        return summaryHash;
    }

    /**
     * Sets the value of the summaryHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSummaryHash(String value) {
        this.summaryHash = value;
    }

    /**
     * Gets the value of the typesDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTypesDate() {
        return typesDate;
    }

    /**
     * Sets the value of the typesDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTypesDate(XMLGregorianCalendar value) {
        this.typesDate = value;
    }

    /**
     * Gets the value of the typesHash property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypesHash() {
        return typesHash;
    }

    /**
     * Sets the value of the typesHash property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypesHash(String value) {
        this.typesHash = value;
    }

}
