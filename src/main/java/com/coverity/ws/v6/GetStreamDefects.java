
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getStreamDefects complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getStreamDefects">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cids" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="filterSpec" type="{http://ws.coverity.com/v6}streamDefectFilterSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStreamDefects", propOrder = {
    "cids",
    "filterSpec"
})
public class GetStreamDefects {

    @XmlElement(type = Long.class)
    protected List<Long> cids;
    protected StreamDefectFilterSpecDataObj filterSpec;

    /**
     * Gets the value of the cids property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cids property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCids().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getCids() {
        if (cids == null) {
            cids = new ArrayList<Long>();
        }
        return this.cids;
    }

    /**
     * Gets the value of the filterSpec property.
     * 
     * @return
     *     possible object is
     *     {@link StreamDefectFilterSpecDataObj }
     *     
     */
    public StreamDefectFilterSpecDataObj getFilterSpec() {
        return filterSpec;
    }

    /**
     * Sets the value of the filterSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamDefectFilterSpecDataObj }
     *     
     */
    public void setFilterSpec(StreamDefectFilterSpecDataObj value) {
        this.filterSpec = value;
    }

}
