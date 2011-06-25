package com.coverity.ws.v3;

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
 *         &lt;element name="analysisStreamIds" type="{http://ws.coverity.com/v3}streamIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="filterSpec" type="{http://ws.coverity.com/v3}mergedDefectFilterSpecDataObj" minOccurs="0"/>
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
    "analysisStreamIds",
    "filterSpec"
})
public class GetCIDsForStreams {

    protected List<StreamIdDataObj> analysisStreamIds;
    protected MergedDefectFilterSpecDataObj filterSpec;

    /**
     * Gets the value of the analysisStreamIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the analysisStreamIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnalysisStreamIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamIdDataObj }
     * 
     * 
     */
    public List<StreamIdDataObj> getAnalysisStreamIds() {
        if (analysisStreamIds == null) {
            analysisStreamIds = new ArrayList<StreamIdDataObj>();
        }
        return this.analysisStreamIds;
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
