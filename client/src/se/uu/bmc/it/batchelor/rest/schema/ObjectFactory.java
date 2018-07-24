//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.3 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.04 at 04:46:13 PM CEST 
//
package se.uu.bmc.it.batchelor.rest.schema;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the se.uu.bmc.it.batchelor.rest.schema package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation
 * for XML content. The Java representation of XML content can consist of schema derived interfaces
 * and classes representing the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: se.uu.bmc.it.batchelor.rest.schema
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Error }
     *
     * @return The error object.
     */
    public Error createError() {
        return new Error();
    }

    /**
     * Create an instance of {@link Job }
     *
     * @return The job object.
     */
    public Job createJob() {
        return new Job();
    }

    /**
     * Create an instance of {@link File }
     *
     * @return The file object.
     */
    public File createFile() {
        return new File();
    }

    /**
     * Create an instance of {@link Link }
     *
     * @return The link object.
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link Result }
     *
     * @return The result objcet.
     */
    public Result createResult() {
        return new Result();
    }

}