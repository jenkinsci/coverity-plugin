package com.coverity.ws.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for groupPermissionDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="groupPermissionDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accessAllowed" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="groupId" type="{http://ws.coverity.com/v3}groupIdDataObj"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "groupPermissionDataObj", propOrder = {
    "accessAllowed",
    "groupId"
})
public class GroupPermissionDataObj {

    protected boolean accessAllowed;
    @XmlElement(required = true)
    protected GroupIdDataObj groupId;

    /**
     * Gets the value of the accessAllowed property.
     * 
     */
    public boolean isAccessAllowed() {
        return accessAllowed;
    }

    /**
     * Sets the value of the accessAllowed property.
     * 
     */
    public void setAccessAllowed(boolean value) {
        this.accessAllowed = value;
    }

    /**
     * Gets the value of the groupId property.
     * 
     * @return
     *     possible object is
     *     {@link com.coverity.ws.v3.GroupIdDataObj }
     *     
     */
    public GroupIdDataObj getGroupId() {
        return groupId;
    }

    /**
     * Sets the value of the groupId property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.coverity.ws.v3.GroupIdDataObj }
     *     
     */
    public void setGroupId(GroupIdDataObj value) {
        this.groupId = value;
    }

}
