
package com.coverity.ws.v9;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for updateStreamDefects complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="updateStreamDefects">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streamDefectIds" type="{http://ws.coverity.com/v9}streamDefectIdDataObj" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="defectStateSpec" type="{http://ws.coverity.com/v9}defectStateSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateStreamDefects", propOrder = {
    "streamDefectIds",
    "defectStateSpec"
})
public class UpdateStreamDefects {

    protected List<StreamDefectIdDataObj> streamDefectIds;
    protected DefectStateSpecDataObj defectStateSpec;

    /**
     * Gets the value of the streamDefectIds property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the streamDefectIds property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStreamDefectIds().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StreamDefectIdDataObj }
     * 
     * 
     */
    public List<StreamDefectIdDataObj> getStreamDefectIds() {
        if (streamDefectIds == null) {
            streamDefectIds = new ArrayList<StreamDefectIdDataObj>();
        }
        return this.streamDefectIds;
    }

    /**
     * Gets the value of the defectStateSpec property.
     * 
     * @return
     *     possible object is
     *     {@link DefectStateSpecDataObj }
     *     
     */
    public DefectStateSpecDataObj getDefectStateSpec() {
        return defectStateSpec;
    }

    /**
     * Sets the value of the defectStateSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link DefectStateSpecDataObj }
     *     
     */
    public void setDefectStateSpec(DefectStateSpecDataObj value) {
        this.defectStateSpec = value;
    }

}
