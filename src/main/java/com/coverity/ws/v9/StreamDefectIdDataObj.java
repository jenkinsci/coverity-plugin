
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for streamDefectIdDataObj complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="streamDefectIdDataObj">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="defectTriageId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="defectTriageVerNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="verNum" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "streamDefectIdDataObj", propOrder = {
    "defectTriageId",
    "defectTriageVerNum",
    "id",
    "verNum"
})
public class StreamDefectIdDataObj {

    protected long defectTriageId;
    protected int defectTriageVerNum;
    protected long id;
    protected int verNum;

    /**
     * Gets the value of the defectTriageId property.
     * 
     */
    public long getDefectTriageId() {
        return defectTriageId;
    }

    /**
     * Sets the value of the defectTriageId property.
     * 
     */
    public void setDefectTriageId(long value) {
        this.defectTriageId = value;
    }

    /**
     * Gets the value of the defectTriageVerNum property.
     * 
     */
    public int getDefectTriageVerNum() {
        return defectTriageVerNum;
    }

    /**
     * Sets the value of the defectTriageVerNum property.
     * 
     */
    public void setDefectTriageVerNum(int value) {
        this.defectTriageVerNum = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the verNum property.
     * 
     */
    public int getVerNum() {
        return verNum;
    }

    /**
     * Sets the value of the verNum property.
     * 
     */
    public void setVerNum(int value) {
        this.verNum = value;
    }

}
