
package com.coverity.ws.v6;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCIDsForStreams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCIDsForStreams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streamIds" type="{http://ws.coverity.com/v6}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="filterSpec" type="{http://ws.coverity.com/v6}mergedDefectFilterSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCIDsForStreams", propOrder = {
    "streamIds",
    "filterSpec"
})
public class GetCIDsForStreams {

    protected List<StreamIdDataObj> streamIds;
    protected MergedDefectFilterSpecDataObj filterSpec;

    /**
     * Gets the value of the streamIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getStreamIds() {
        if (streamIds == null) {
            streamIds = new ArrayList<StreamIdDataObj>();
        }
        return this.streamIds;
    }

    /**
     * Gets the value of the filterSpec property.
     * 
     * @return
     *     possible object is
     *     {@link MergedDefectFilterSpecDataObj }
     *     
     */
    public MergedDefectFilterSpecDataObj getFilterSpec() {
        return filterSpec;
    }

    /**
     * Sets the value of the filterSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link MergedDefectFilterSpecDataObj }
     *     
     */
    public void setFilterSpec(MergedDefectFilterSpecDataObj value) {
        this.filterSpec = value;
    }

}
