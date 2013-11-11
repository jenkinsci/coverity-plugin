
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createProject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createProject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="projectSpec" type="{http://ws.coverity.com/v6}projectSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createProject", propOrder = {
    "projectSpec"
})
public class CreateProject {

    protected ProjectSpecDataObj projectSpec;

    /**
     * Gets the value of the projectSpec property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectSpecDataObj }
     *     
     */
    public ProjectSpecDataObj getProjectSpec() {
        return projectSpec;
    }

    /**
     * Sets the value of the projectSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectSpecDataObj }
     *     
     */
    public void setProjectSpec(ProjectSpecDataObj value) {
        this.projectSpec = value;
    }

}
