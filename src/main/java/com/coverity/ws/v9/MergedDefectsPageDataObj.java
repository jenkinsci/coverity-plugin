
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mergedDefectsPageDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mergedDefectsPageDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mergedDefectIds" type="{http://ws.coverity.com/v9}mergedDefectIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="mergedDefects" type="{http://ws.coverity.com/v9}mergedDefectDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="totalNumberOfRecords" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mergedDefectsPageDataObj", propOrder = {
    "mergedDefectIds",
    "mergedDefects",
    "totalNumberOfRecords"
})
public class MergedDefectsPageDataObj {

    @XmlElement(nillable = true)
    protected List<MergedDefectIdDataObj> mergedDefectIds;
    @XmlElement(nillable = true)
    protected List<MergedDefectDataObj> mergedDefects;
    protected Integer totalNumberOfRecords;

    /**
     * Gets the value of the mergedDefectIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mergedDefectIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMergedDefectIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MergedDefectIdDataObj }
     * 
     * 
     */
    public List<MergedDefectIdDataObj> getMergedDefectIds() {
        if (mergedDefectIds == null) {
            mergedDefectIds = new ArrayList<MergedDefectIdDataObj>();
        }
        return this.mergedDefectIds;
    }

    /**
     * Gets the value of the mergedDefects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mergedDefects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMergedDefects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MergedDefectDataObj }
     * 
     * 
     */
    public List<MergedDefectDataObj> getMergedDefects() {
        if (mergedDefects == null) {
            mergedDefects = new ArrayList<MergedDefectDataObj>();
        }
        return this.mergedDefects;
    }

    /**
     * Gets the value of the totalNumberOfRecords property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalNumberOfRecords() {
        return totalNumberOfRecords;
    }

    /**
     * Sets the value of the totalNumberOfRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalNumberOfRecords(Integer value) {
        this.totalNumberOfRecords = value;
    }

}
