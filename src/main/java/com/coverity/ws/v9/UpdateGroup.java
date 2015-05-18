
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groupId" type="{http://ws.coverity.com/v9}groupIdDataObj" minOccurs="0"/>
 *         &lt;element name="groupSpec" type="{http://ws.coverity.com/v9}groupSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateGroup", propOrder = {
    "groupId",
    "groupSpec"
})
public class UpdateGroup {

    protected GroupIdDataObj groupId;
    protected GroupSpecDataObj groupSpec;

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
     * Gets the value of the groupSpec property.
     * 
     * @return
     *     possible object is
     *     {@link GroupSpecDataObj }
     *     
     */
    public GroupSpecDataObj getGroupSpec() {
        return groupSpec;
    }

    /**
     * Sets the value of the groupSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupSpecDataObj }
     *     
     */
    public void setGroupSpec(GroupSpecDataObj value) {
        this.groupSpec = value;
    }

}
