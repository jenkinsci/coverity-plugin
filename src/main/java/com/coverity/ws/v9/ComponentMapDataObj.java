
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for componentMapDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="componentMapDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="componentMapId" type="{http://ws.coverity.com/v9}componentMapIdDataObj"/>
 *         &lt;element name="componentPathRules" type="{http://ws.coverity.com/v9}componentPathRuleDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="components" type="{http://ws.coverity.com/v9}componentDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="defectRules" type="{http://ws.coverity.com/v9}componentDefectRuleDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "componentMapDataObj", propOrder = {
    "componentMapId",
    "componentPathRules",
    "components",
    "defectRules",
    "description"
})
public class ComponentMapDataObj {

    @XmlElement(required = true)
    protected ComponentMapIdDataObj componentMapId;
    @XmlElement(nillable = true)
    protected List<ComponentPathRuleDataObj> componentPathRules;
    @XmlElement(nillable = true)
    protected List<ComponentDataObj> components;
    @XmlElement(nillable = true)
    protected List<ComponentDefectRuleDataObj> defectRules;
    protected String description;

    /**
     * Gets the value of the componentMapId property.
     * 
     * @return
     *     possible object is
     *     {@link ComponentMapIdDataObj }
     *     
     */
    public ComponentMapIdDataObj getComponentMapId() {
        return componentMapId;
    }

    /**
     * Sets the value of the componentMapId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComponentMapIdDataObj }
     *     
     */
    public void setComponentMapId(ComponentMapIdDataObj value) {
        this.componentMapId = value;
    }

    /**
     * Gets the value of the componentPathRules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the componentPathRules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponentPathRules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentPathRuleDataObj }
     * 
     * 
     */
    public List<ComponentPathRuleDataObj> getComponentPathRules() {
        if (componentPathRules == null) {
            componentPathRules = new ArrayList<ComponentPathRuleDataObj>();
        }
        return this.componentPathRules;
    }

    /**
     * Gets the value of the components property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the components property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentDataObj }
     * 
     * 
     */
    public List<ComponentDataObj> getComponents() {
        if (components == null) {
            components = new ArrayList<ComponentDataObj>();
        }
        return this.components;
    }

    /**
     * Gets the value of the defectRules property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defectRules property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefectRules().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ComponentDefectRuleDataObj }
     * 
     * 
     */
    public List<ComponentDefectRuleDataObj> getDefectRules() {
        if (defectRules == null) {
            defectRules = new ArrayList<ComponentDefectRuleDataObj>();
        }
        return this.defectRules;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

}
