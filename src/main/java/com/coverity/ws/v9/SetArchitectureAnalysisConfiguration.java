
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setArchitectureAnalysisConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setArchitectureAnalysisConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="architectureAnalysisConfiguration" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setArchitectureAnalysisConfiguration", propOrder = {
    "architectureAnalysisConfiguration"
})
public class SetArchitectureAnalysisConfiguration {

    protected String architectureAnalysisConfiguration;

    /**
     * Gets the value of the architectureAnalysisConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArchitectureAnalysisConfiguration() {
        return architectureAnalysisConfiguration;
    }

    /**
     * Sets the value of the architectureAnalysisConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchitectureAnalysisConfiguration(String value) {
        this.architectureAnalysisConfiguration = value;
    }

}
