
package com.coverity.ws.v6;

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
 *         &lt;element name="groupId" type="{http://ws.coverity.com/v6}groupIdDataObj"/>
 *         &lt;element name="groupRole" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "groupId",
    "groupRole"
})
public class GroupPermissionDataObj {

    @XmlElement(required = true)
    protected GroupIdDataObj groupId;
    @XmlElement(required = true)
    protected String groupRole;

    /**
     * Gets the value of the groupId property.
     * 
     * @return
     *     possible object is
     *     {@link GroupIdDataObj }
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
     *     {@link GroupIdDataObj }
     *     
     */
    public void setGroupId(GroupIdDataObj value) {
        this.groupId = value;
    }

    /**
     * Gets the value of the groupRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupRole() {
        return groupRole;
    }

    /**
     * Sets the value of the groupRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupRole(String value) {
        this.groupRole = value;
    }

}
