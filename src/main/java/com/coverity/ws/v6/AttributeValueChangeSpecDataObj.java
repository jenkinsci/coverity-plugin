
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for attributeValueChangeSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attributeValueChangeSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeValueIds" type="{http://ws.coverity.com/v6}attributeValueIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="attributeValues" type="{http://ws.coverity.com/v6}attributeValueSpecDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attributeValueChangeSpecDataObj", propOrder = {
    "attributeValueIds",
    "attributeValues"
})
public class AttributeValueChangeSpecDataObj {

    @XmlElement(nillable = true)
    protected List<AttributeValueIdDataObj> attributeValueIds;
    @XmlElement(nillable = true)
    protected List<AttributeValueSpecDataObj> attributeValues;

    /**
     * Gets the value of the attributeValueIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeValueIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeValueIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeValueIdDataObj }
     * 
     * 
     */
    public List<AttributeValueIdDataObj> getAttributeValueIds() {
        if (attributeValueIds == null) {
            attributeValueIds = new ArrayList<AttributeValueIdDataObj>();
        }
        return this.attributeValueIds;
    }

    /**
     * Gets the value of the attributeValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeValueSpecDataObj }
     * 
     * 
     */
    public List<AttributeValueSpecDataObj> getAttributeValues() {
        if (attributeValues == null) {
            attributeValues = new ArrayList<AttributeValueSpecDataObj>();
        }
        return this.attributeValues;
    }

}
