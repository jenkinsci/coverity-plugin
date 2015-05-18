
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for attributeValueDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attributeValueDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeValueId" type="{http://ws.coverity.com/v9}attributeValueIdDataObj" minOccurs="0"/>
 *         &lt;element name="deprecated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="issueKindList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributeValueDataObj", propOrder = {
    "attributeValueId",
    "deprecated",
    "displayName",
    "issueKindList"
})
public class AttributeValueDataObj {

    protected AttributeValueIdDataObj attributeValueId;
    protected boolean deprecated;
    protected String displayName;
    @XmlElement(nillable = true)
    protected List<String> issueKindList;

    /**
     * Gets the value of the attributeValueId property.
     * 
     * @return
     *     possible object is
     *     {@link AttributeValueIdDataObj }
     *     
     */
    public AttributeValueIdDataObj getAttributeValueId() {
        return attributeValueId;
    }

    /**
     * Sets the value of the attributeValueId property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttributeValueIdDataObj }
     *     
     */
    public void setAttributeValueId(AttributeValueIdDataObj value) {
        this.attributeValueId = value;
    }

    /**
     * Gets the value of the deprecated property.
     * 
     */
    public boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Sets the value of the deprecated property.
     * 
     */
    public void setDeprecated(boolean value) {
        this.deprecated = value;
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
     * Gets the value of the issueKindList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the issueKindList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIssueKindList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIssueKindList() {
        if (issueKindList == null) {
            issueKindList = new ArrayList<String>();
        }
        return this.issueKindList;
    }

}
