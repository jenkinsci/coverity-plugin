
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mergeTriageStores complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mergeTriageStores">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="srcTriageStoreIds" type="{http://ws.coverity.com/v9}triageStoreIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="triageStoreId" type="{http://ws.coverity.com/v9}triageStoreIdDataObj" minOccurs="0"/>
 *         &lt;element name="deleteSourceStores" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="assignStreamsToTargetStore" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mergeTriageStores", propOrder = {
    "srcTriageStoreIds",
    "triageStoreId",
    "deleteSourceStores",
    "assignStreamsToTargetStore"
})
public class MergeTriageStores {

    protected List<TriageStoreIdDataObj> srcTriageStoreIds;
    protected TriageStoreIdDataObj triageStoreId;
    protected boolean deleteSourceStores;
    protected boolean assignStreamsToTargetStore;

    /**
     * Gets the value of the srcTriageStoreIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the srcTriageStoreIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSrcTriageStoreIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TriageStoreIdDataObj }
     * 
     * 
     */
    public List<TriageStoreIdDataObj> getSrcTriageStoreIds() {
        if (srcTriageStoreIds == null) {
            srcTriageStoreIds = new ArrayList<TriageStoreIdDataObj>();
        }
        return this.srcTriageStoreIds;
    }

    /**
     * Gets the value of the triageStoreId property.
     * 
     * @return
     *     possible object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public TriageStoreIdDataObj getTriageStoreId() {
        return triageStoreId;
    }

    /**
     * Sets the value of the triageStoreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link TriageStoreIdDataObj }
     *     
     */
    public void setTriageStoreId(TriageStoreIdDataObj value) {
        this.triageStoreId = value;
    }

    /**
     * Gets the value of the deleteSourceStores property.
     * 
     */
    public boolean isDeleteSourceStores() {
        return deleteSourceStores;
    }

    /**
     * Sets the value of the deleteSourceStores property.
     * 
     */
    public void setDeleteSourceStores(boolean value) {
        this.deleteSourceStores = value;
    }

    /**
     * Gets the value of the assignStreamsToTargetStore property.
     * 
     */
    public boolean isAssignStreamsToTargetStore() {
        return assignStreamsToTargetStore;
    }

    /**
     * Sets the value of the assignStreamsToTargetStore property.
     * 
     */
    public void setAssignStreamsToTargetStore(boolean value) {
        this.assignStreamsToTargetStore = value;
    }

}
