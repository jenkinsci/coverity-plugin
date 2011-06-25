package com.coverity.ws.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for groupDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="groupDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="local" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="name" type="{http://ws.coverity.com/v3}groupIdDataObj" minOccurs="0"/>
 *         &lt;element name="roles" type="{http://ws.coverity.com/v3}roleDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="syncEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupDataObj", propOrder = {
    "local",
    "name",
    "roles",
    "syncEnabled"
})
public class GroupDataObj {

    protected boolean local;
    protected GroupIdDataObj name;
    @XmlElement(nillable = true)
    protected List<RoleDataObj> roles;
    protected boolean syncEnabled;

    /**
     * Gets the value of the local property.
     * 
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * Sets the value of the local property.
     * 
     */
    public void setLocal(boolean value) {
        this.local = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link GroupIdDataObj }
     *     
     */
    public GroupIdDataObj getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupIdDataObj }
     *     
     */
    public void setName(GroupIdDataObj value) {
        this.name = value;
    }

    /**
     * Gets the value of the roles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the roles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRoles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RoleDataObj }
     * 
     * 
     */
    public List<RoleDataObj> getRoles() {
        if (roles == null) {
            roles = new ArrayList<RoleDataObj>();
        }
        return this.roles;
    }

    /**
     * Gets the value of the syncEnabled property.
     * 
     */
    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    /**
     * Sets the value of the syncEnabled property.
     * 
     */
    public void setSyncEnabled(boolean value) {
        this.syncEnabled = value;
    }

}
