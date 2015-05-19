
package com.coverity.ws.v9;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getMergedDefectsForSnapshotScope complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getMergedDefectsForSnapshotScope">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="projectId" type="{http://ws.coverity.com/v9}projectIdDataObj" minOccurs="0"/>
 *         &lt;element name="filterSpec" type="{http://ws.coverity.com/v9}snapshotScopeDefectFilterSpecDataObj" minOccurs="0"/>
 *         &lt;element name="pageSpec" type="{http://ws.coverity.com/v9}pageSpecDataObj" minOccurs="0"/>
 *         &lt;element name="snapshotScope" type="{http://ws.coverity.com/v9}snapshotScopeSpecDataObj" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMergedDefectsForSnapshotScope", propOrder = {
    "projectId",
    "filterSpec",
    "pageSpec",
    "snapshotScope"
})
public class GetMergedDefectsForSnapshotScope {

    protected ProjectIdDataObj projectId;
    protected SnapshotScopeDefectFilterSpecDataObj filterSpec;
    protected PageSpecDataObj pageSpec;
    protected SnapshotScopeSpecDataObj snapshotScope;

    /**
     * Gets the value of the projectId property.
     * 
     * @return
     *     possible object is
     *     {@link ProjectIdDataObj }
     *     
     */
    public ProjectIdDataObj getProjectId() {
        return projectId;
    }

    /**
     * Sets the value of the projectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProjectIdDataObj }
     *     
     */
    public void setProjectId(ProjectIdDataObj value) {
        this.projectId = value;
    }

    /**
     * Gets the value of the filterSpec property.
     * 
     * @return
     *     possible object is
     *     {@link SnapshotScopeDefectFilterSpecDataObj }
     *     
     */
    public SnapshotScopeDefectFilterSpecDataObj getFilterSpec() {
        return filterSpec;
    }

    /**
     * Sets the value of the filterSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link SnapshotScopeDefectFilterSpecDataObj }
     *     
     */
    public void setFilterSpec(SnapshotScopeDefectFilterSpecDataObj value) {
        this.filterSpec = value;
    }

    /**
     * Gets the value of the pageSpec property.
     * 
     * @return
     *     possible object is
     *     {@link PageSpecDataObj }
     *     
     */
    public PageSpecDataObj getPageSpec() {
        return pageSpec;
    }

    /**
     * Sets the value of the pageSpec property.
     * 
     * @param value
     *     allowed object is
     *     {@link PageSpecDataObj }
     *     
     */
    public void setPageSpec(PageSpecDataObj value) {
        this.pageSpec = value;
    }

    /**
     * Gets the value of the snapshotScope property.
     * 
     * @return
     *     possible object is
     *     {@link SnapshotScopeSpecDataObj }
     *     
     */
    public SnapshotScopeSpecDataObj getSnapshotScope() {
        return snapshotScope;
    }

    /**
     * Sets the value of the snapshotScope property.
     * 
     * @param value
     *     allowed object is
     *     {@link SnapshotScopeSpecDataObj }
     *     
     */
    public void setSnapshotScope(SnapshotScopeSpecDataObj value) {
        this.snapshotScope = value;
    }

}
