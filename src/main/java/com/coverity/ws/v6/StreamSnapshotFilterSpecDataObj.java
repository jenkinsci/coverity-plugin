
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for streamSnapshotFilterSpecDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamSnapshotFilterSpecDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="snapshotIdIncludeList" type="{http://ws.coverity.com/v6}snapshotIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="snapshotIdExcludeList" type="{http://ws.coverity.com/v6}snapshotIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="streamId" type="{http://ws.coverity.com/v6}streamIdDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamSnapshotFilterSpecDataObj", propOrder = {
    "snapshotIdIncludeList",
    "snapshotIdExcludeList",
    "streamId"
})
public class StreamSnapshotFilterSpecDataObj {

    @XmlElement(nillable = true)
    protected List<SnapshotIdDataObj> snapshotIdIncludeList;
    @XmlElement(nillable = true)
    protected List<SnapshotIdDataObj> snapshotIdExcludeList;
    protected StreamIdDataObj streamId;

    /**
     * Gets the value of the snapshotIdIncludeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the snapshotIdIncludeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSnapshotIdIncludeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SnapshotIdDataObj }
     * 
     * 
     */
    public List<SnapshotIdDataObj> getSnapshotIdIncludeList() {
        if (snapshotIdIncludeList == null) {
            snapshotIdIncludeList = new ArrayList<SnapshotIdDataObj>();
        }
        return this.snapshotIdIncludeList;
    }

    /**
     * Gets the value of the snapshotIdExcludeList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the snapshotIdExcludeList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSnapshotIdExcludeList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SnapshotIdDataObj }
     * 
     * 
     */
    public List<SnapshotIdDataObj> getSnapshotIdExcludeList() {
        if (snapshotIdExcludeList == null) {
            snapshotIdExcludeList = new ArrayList<SnapshotIdDataObj>();
        }
        return this.snapshotIdExcludeList;
    }

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

}
