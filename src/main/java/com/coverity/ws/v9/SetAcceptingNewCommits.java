
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setAcceptingNewCommits complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setAcceptingNewCommits">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acceptNewCommits" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setAcceptingNewCommits", propOrder = {
    "acceptNewCommits"
})
public class SetAcceptingNewCommits {

    protected boolean acceptNewCommits;

    /**
     * Gets the value of the acceptNewCommits property.
     * 
     */
    public boolean isAcceptNewCommits() {
        return acceptNewCommits;
    }

    /**
     * Sets the value of the acceptNewCommits property.
     * 
     */
    public void setAcceptNewCommits(boolean value) {
        this.acceptNewCommits = value;
    }

}
