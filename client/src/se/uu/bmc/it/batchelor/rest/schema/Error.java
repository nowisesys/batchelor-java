//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.04 at 04:46:13 PM CEST 
//
package se.uu.bmc.it.batchelor.rest.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for error complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="error"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://it.bmc.uu.se/batchelor/rest/200901}origin default="remote""/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "error", propOrder = {
    "code",
    "message"
})
public class Error {

    protected int code;
    @XmlElement(required = true)
    protected String message;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901")
    protected String origin;

    /**
     * Gets the value of the code property.
     *
     * @return int
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value The error code.
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the origin property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getOrigin() {
        if (origin == null) {
            return "remote";
        } else {
            return origin;
        }
    }

    /**
     * Sets the value of the origin property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setOrigin(String value) {
        this.origin = value;
    }

}
