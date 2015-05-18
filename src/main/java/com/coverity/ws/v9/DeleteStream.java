
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for deleteStream complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="deleteStream">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streamId" type="{http://ws.coverity.com/v9}streamIdDataObj" minOccurs="0"/>
 *         &lt;element name="onlyIfEmpty" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteStream", propOrder = {
    "streamId",
    "onlyIfEmpty"
})
public class DeleteStream {

    protected StreamIdDataObj streamId;
    protected boolean onlyIfEmpty;

    /**
     * Gets the value of the streamId property.
     * 
     * @return
     *     possible object is
     *     {@link StreamIdDataObj }
     *     
     */
    public StreamIdDataObj getStreamId() {
        return streamId;
    }

    /**
     * Sets the value of the streamId property.
     * 
     * @param value
     *     allowed object is
     *     {@link StreamIdDataObj }
     *     
     */
    public void setStreamId(StreamIdDataObj value) {
        this.streamId = value;
    }

    /**
     * Gets the value of the onlyIfEmpty property.
     * 
     */
    public boolean isOnlyIfEmpty() {
        return onlyIfEmpty;
    }

    /**
     * Sets the value of the onlyIfEmpty property.
     * 
     */
    public void setOnlyIfEmpty(boolean value) {
        this.onlyIfEmpty = value;
    }

}
