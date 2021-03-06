//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.04 at 04:46:13 PM CEST 
//
package se.uu.bmc.it.batchelor.rest.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="error" type="{http://it.bmc.uu.se/batchelor/rest/200901}error" minOccurs="0"/&gt;
 *         &lt;element name="job" type="{http://it.bmc.uu.se/batchelor/rest/200901}job" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="link" type="{http://it.bmc.uu.se/batchelor/rest/200901}link" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="file" type="{http://it.bmc.uu.se/batchelor/rest/200901}file" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref="{http://it.bmc.uu.se/batchelor/rest/200901}state use="required""/&gt;
 *       &lt;attribute ref="{http://it.bmc.uu.se/batchelor/rest/200901}type use="required""/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "version",
    "status",
    "error",
    "job",
    "link",
    "file"
})
@XmlRootElement(name = "result")
public class Result {

    protected String version;
    protected String status;
    protected Error error;
    protected List<Job> job;
    protected List<Link> link;
    protected File file;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901", required = true)
    protected String state;
    @XmlAttribute(namespace = "http://it.bmc.uu.se/batchelor/rest/200901", required = true)
    protected String type;

    /**
     * Gets the value of the version property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the error property.
     *
     * @return possible object is {@link Error }
     *
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     *
     * @param value allowed object is {@link Error }
     *
     */
    public void setError(Error value) {
        this.error = value;
    }

    /**
     * Gets the value of the job property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <code>set</code> method for the job property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getJob().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Job }
     *
     * @return The list of job objects.
     */
    public List<Job> getJob() {
        if (job == null) {
            job = new ArrayList<>();
        }
        return this.job;
    }

    /**
     * Gets the value of the link property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the link property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLink().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Link }
     *
     * @return The list of link objects.
     */
    public List<Link> getLink() {
        if (link == null) {
            link = new ArrayList<>();
        }
        return this.link;
    }

    /**
     * Gets the value of the file property.
     *
     * @return possible object is {@link File }
     *
     */
    public File getFile() {
        return file;
    }

    /**
     * Sets the value of the file property.
     *
     * @param value allowed object is {@link File }
     *
     */
    public void setFile(File value) {
        this.file = value;
    }

    /**
     * Gets the value of the state property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setType(String value) {
        this.type = value;
    }

}
