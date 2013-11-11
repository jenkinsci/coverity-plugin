
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for componentDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="componentDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentId" type="{http://ws.coverity.com/v6}componentIdDataObj"/>
 *         &lt;element name="groupPermissions" type="{http://ws.coverity.com/v6}groupPermissionDataObj" maxOccurs="unbounded"/>
 *         &lt;element name="subscribers" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "componentDataObj", propOrder = {
    "componentId",
    "groupPermissions",
    "subscribers"
})
public class ComponentDataObj {

    @XmlElement(required = true)
    protected ComponentIdDataObj componentId;
    @XmlElement(required = true)
    protected List<GroupPermissionDataObj> groupPermissions;
    @XmlElement(nillable = true)
    protected List<String> subscribers;

    /**
     * Gets the value of the componentId property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentIdDataObj }
     *     
     */
    public ComponentIdDataObj getComponentId() {
        return componentId;
    }

    /**
     * Sets the value of the componentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentIdDataObj }
     *     
     */
    public void setComponentId(ComponentIdDataObj value) {
        this.componentId = value;
    }

    /**
     * Gets the value of the groupPermissions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groupPermissions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroupPermissions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GroupPermissionDataObj }
     * 
     * 
     */
    public List<GroupPermissionDataObj> getGroupPermissions() {
        if (groupPermissions == null) {
            groupPermissions = new ArrayList<GroupPermissionDataObj>();
        }
        return this.groupPermissions;
    }

    /**
     * Gets the value of the subscribers property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subscribers property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubscribers().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSubscribers() {
        if (subscribers == null) {
            subscribers = new ArrayList<String>();
        }
        return this.subscribers;
    }

}
