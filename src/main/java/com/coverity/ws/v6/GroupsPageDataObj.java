
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for groupsPageDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="groupsPageDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groups" type="{http://ws.coverity.com/v6}groupDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="totalNumberOfRecords" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupsPageDataObj", propOrder = {
    "groups",
    "totalNumberOfRecords"
})
public class GroupsPageDataObj {

    @XmlElement(nillable = true)
    protected List<GroupDataObj> groups;
    protected Integer totalNumberOfRecords;

    /**
     * Gets the value of the groups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GroupDataObj }
     * 
     * 
     */
    public List<GroupDataObj> getGroups() {
        if (groups == null) {
            groups = new ArrayList<GroupDataObj>();
        }
        return this.groups;
    }

    /**
     * Gets the value of the totalNumberOfRecords property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    /**
     * Sets the value of the totalNumberOfRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalNumberOfRecords(Integer value) {
        this.totalNumberOfRecords = value;
    }

}
