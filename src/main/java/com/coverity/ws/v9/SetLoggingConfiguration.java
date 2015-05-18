
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setLoggingConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setLoggingConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="loggingConfigurationDataObj" type="{http://ws.coverity.com/v9}loggingConfigurationDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setLoggingConfiguration", propOrder = {
    "loggingConfigurationDataObj"
})
public class SetLoggingConfiguration {

    protected LoggingConfigurationDataObj loggingConfigurationDataObj;

    /**
     * Gets the value of the loggingConfigurationDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link LoggingConfigurationDataObj }
     *     
     */
    public LoggingConfigurationDataObj getLoggingConfigurationDataObj() {
        return loggingConfigurationDataObj;
    }

    /**
     * Sets the value of the loggingConfigurationDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoggingConfigurationDataObj }
     *     
     */
    public void setLoggingConfigurationDataObj(LoggingConfigurationDataObj value) {
        this.loggingConfigurationDataObj = value;
    }

}
