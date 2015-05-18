
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setBackupConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setBackupConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="backupConfigurationDataObj" type="{http://ws.coverity.com/v9}backupConfigurationDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setBackupConfiguration", propOrder = {
    "backupConfigurationDataObj"
})
public class SetBackupConfiguration {

    protected BackupConfigurationDataObj backupConfigurationDataObj;

    /**
     * Gets the value of the backupConfigurationDataObj property.
     * 
     * @return
     *     possible object is
     *     {@link BackupConfigurationDataObj }
     *     
     */
    public BackupConfigurationDataObj getBackupConfigurationDataObj() {
        return backupConfigurationDataObj;
    }

    /**
     * Sets the value of the backupConfigurationDataObj property.
     * 
     * @param value
     *     allowed object is
     *     {@link BackupConfigurationDataObj }
     *     
     */
    public void setBackupConfigurationDataObj(BackupConfigurationDataObj value) {
        this.backupConfigurationDataObj = value;
    }

}
