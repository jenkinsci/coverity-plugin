
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateTriageForCIDsInTriageStore complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateTriageForCIDsInTriageStore">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="triageStore" type="{http://ws.coverity.com/v9}triageStoreIdDataObj" minOccurs="0"/>
 *         &lt;element name="mergedDefectIdDataObjs" type="{http://ws.coverity.com/v9}mergedDefectIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="defectState" type="{http://ws.coverity.com/v9}defectStateSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateTriageForCIDsInTriageStore", propOrder = {
    "triageStore",
    "mergedDefectIdDataObjs",
    "defectState"
})
public class UpdateTriageForCIDsInTriageStore {

    protected TriageStoreIdDataObj triageStore;
    protected List<MergedDefectIdDataObj> mergedDefectIdDataObjs;
    protected DefectStateSpecDataObj defectState;

    /**
     * Gets the value of the triageStore property.
     * 
     * @return
     *     possible object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public TriageStoreIdDataObj getTriageStore() {
        return triageStore;
    }

    /**
     * Sets the value of the triageStore property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public void setTriageStore(TriageStoreIdDataObj value) {
        this.triageStore = value;
    }

    /**
     * Gets the value of the mergedDefectIdDataObjs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mergedDefectIdDataObjs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMergedDefectIdDataObjs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MergedDefectIdDataObj }
     * 
     * 
     */
    public List<MergedDefectIdDataObj> getMergedDefectIdDataObjs() {
        if (mergedDefectIdDataObjs == null) {
            mergedDefectIdDataObjs = new ArrayList<MergedDefectIdDataObj>();
        }
        return this.mergedDefectIdDataObjs;
    }

    /**
     * Gets the value of the defectState property.
     * 
     * @return
     *     possible object is
     *     {@link DefectStateSpecDataObj }
     *     
     */
    public DefectStateSpecDataObj getDefectState() {
        return defectState;
    }

    /**
     * Sets the value of the defectState property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefectStateSpecDataObj }
     *     
     */
    public void setDefectState(DefectStateSpecDataObj value) {
        this.defectState = value;
    }

}
