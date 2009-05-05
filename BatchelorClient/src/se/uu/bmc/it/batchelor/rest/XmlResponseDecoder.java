/*
 * Web service library for the Batchelor batch job queue.
 * Copyright (C) 2009 by Anders Lövgren and the Computing Department at BMC,
 * Uppsala University.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Send questions, suggestions, bugs or comments to: 
 * Anders Lövgren (lespaul@algonet.se or anders.lovgren@bmc.uu.se)
 * 
 * For more info: http://it.bmc.uu.se/andlov/proj/batchelor/
 */

/*
 * XmlResponseDecoder.java
 *
 * Created: Apr 3, 2009, 10:35:38 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.net.ContentHandler;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import se.uu.bmc.it.batchelor.rest.schema.*;

/**
 * This class extends the ContentHandler class to decode XML encoded REST
 * service response messages from the Batchelor REST service.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 * @see ResponseDecoder
 */
public class XmlResponseDecoder extends ContentHandler {

    /**
     * Return a result object wrapping an error object. This function extends
     * the result error type with local errors.
     * @param message The error message.
     * @return The result object representing the error.
     */
    private Result getError(String message) {

        se.uu.bmc.it.batchelor.rest.schema.Error error;

        ObjectFactory factory = new ObjectFactory();
        Result result = factory.createResult();

        error = factory.createError();
        error.setCode(0);
        error.setMessage(message);
        error.setOrigin("local");

        result.setError(error);
        result.setState("failed");
        
        return result;
    }

    /**
     * Create an result object wrapping the REST service response.
     * @param connection The URL connection to the REST service.
     * @return Next result object decoded from the XML encoded respose message.
     */
    public Object getContent(URLConnection connection) {

        // Use JAXB to unmarshall the input stream to the result object type
        // defined in the XML schema for the REST service.

        try {
            JAXBContext context = JAXBContext.newInstance("se.uu.bmc.it.batchelor.rest.schema");
            Unmarshaller unmarshaller = context.createUnmarshaller();

            InputStream stream = connection.getInputStream();
            Result result = (Result) unmarshaller.unmarshal(stream);
            return result;
        } catch (JAXBException e) {
            return getError("Failed decode response: " + e.getMessage());
        } catch (IOException e) {
            return getError("Failed read response: " + e.getMessage());
        }
        // TODO: should we catch generic exceptions here also?
    }
}
