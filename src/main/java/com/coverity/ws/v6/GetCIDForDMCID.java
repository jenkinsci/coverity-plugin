
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCIDForDMCID complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCIDForDMCID">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dmCid" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="databaseDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCIDForDMCID", propOrder = {
    "dmCid",
    "databaseDescription"
})
public class GetCIDForDMCID {

    protected Long dmCid;
    protected String databaseDescription;

    /**
     * Gets the value of the dmCid property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDmCid() {
        return dmCid;
    }

    /**
     * Sets the value of the dmCid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDmCid(Long value) {
        this.dmCid = value;
    }

    /**
     * Gets the value of the databaseDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatabaseDescription() {
        return databaseDescription;
    }

    /**
     * Sets the value of the databaseDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatabaseDescription(String value) {
        this.databaseDescription = value;
    }

}
