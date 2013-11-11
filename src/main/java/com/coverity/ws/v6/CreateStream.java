
package com.coverity.ws.v6;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for createStream complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="createStream">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streamSpec" type="{http://ws.coverity.com/v6}streamSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createStream", propOrder = {
    "streamSpec"
})
public class CreateStream {

    protected StreamSpecDataObj streamSpec;

    /**
     * Gets the value of the streamSpec property.
     * 
     * @return
     *     possible object is
     *     {@link StreamSpecDataObj }
     *     
     */
    public StreamSpecDataObj getStreamSpec() {
        return streamSpec;
    }

    /**
     * Sets the value of the streamSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamSpecDataObj }
     *     
     */
    public void setStreamSpec(StreamSpecDataObj value) {
        this.streamSpec = value;
    }

}
