
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for usersPageDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="usersPageDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="users" type="{http://ws.coverity.com/v6}userDataObj" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "usersPageDataObj", propOrder = {
    "users",
    "totalNumberOfRecords"
})
public class UsersPageDataObj {

    @XmlElement(nillable = true)
    protected List<UserDataObj> users;
    protected Integer totalNumberOfRecords;

    /**
     * Gets the value of the users property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the users property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserDataObj }
     * 
     * 
     */
    public List<UserDataObj> getUsers() {
        if (users == null) {
            users = new ArrayList<UserDataObj>();
        }
        return this.users;
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
