//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.04.21 at 02:17:41 AM CEST 
//


package se.uu.bmc.it.batchelor.rest.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for link complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="link">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://it.bmc.uu.se/batchelor/rest/200901}request"/>
 *       &lt;attribute ref="{http://it.bmc.uu.se/batchelor/xlink/200901}href use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "link")
public class Link {

    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/xlink/200901", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901")
    protected String delete;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901")
    protected String get;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901")
    protected String post;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901")
    protected String put;

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets the value of the delete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelete() {
        return delete;
    }

    /**
     * Sets the value of the delete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelete(String value) {
        this.delete = value;
    }

    /**
     * Gets the value of the get property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGet() {
        return get;
    }

    /**
     * Sets the value of the get property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGet(String value) {
        this.get = value;
    }

    /**
     * Gets the value of the post property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPost() {
        return post;
    }

    /**
     * Sets the value of the post property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPost(String value) {
        this.post = value;
    }

    /**
     * Gets the value of the put property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPut() {
        return put;
    }

    /**
     * Sets the value of the put property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPut(String value) {
        this.put = value;
    }

}
