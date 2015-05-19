
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getTriageHistory complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getTriageHistory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mergedDefectIdDataObj" type="{http://ws.coverity.com/v9}mergedDefectIdDataObj" minOccurs="0"/>
 *         &lt;element name="triageStoreIds" type="{http://ws.coverity.com/v9}triageStoreIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTriageHistory", propOrder = {
    "mergedDefectIdDataObj",
    "triageStoreIds"
})
public class GetTriageHistory {

    protected MergedDefectIdDataObj mergedDefectIdDataObj;
    protected List<TriageStoreIdDataObj> triageStoreIds;

    /**
     * Gets the value of the mergedDefectIdDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link MergedDefectIdDataObj }
     *     
     */
    public MergedDefectIdDataObj getMergedDefectIdDataObj() {
        return mergedDefectIdDataObj;
    }

    /**
     * Sets the value of the mergedDefectIdDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link MergedDefectIdDataObj }
     *     
     */
    public void setMergedDefectIdDataObj(MergedDefectIdDataObj value) {
        this.mergedDefectIdDataObj = value;
    }

    /**
     * Gets the value of the triageStoreIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the triageStoreIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTriageStoreIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TriageStoreIdDataObj }
     * 
     * 
     */
    public List<TriageStoreIdDataObj> getTriageStoreIds() {
        if (triageStoreIds == null) {
            triageStoreIds = new ArrayList<TriageStoreIdDataObj>();
        }
        return this.triageStoreIds;
    }

}
