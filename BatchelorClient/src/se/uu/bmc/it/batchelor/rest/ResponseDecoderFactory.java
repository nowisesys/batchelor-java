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
 * ResponseDecoderFactory.java
 *
 * Created: Apr 16, 2009, 2:44:07 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.net.ContentHandlerFactory;
import java.net.ContentHandler;

/**
 * <p>Implements the ContentHandlerFactory that returns decoders to decode an
 * REST service response (XML or FOA encoded).</p>
 *
 * <p>A ContentHandlerFactory must be set prior to calling any functions that
 * communicates with the REST service. However, the ContentHandlerFactory is
 * system wide and can only be set once per application. One thing to keep in
 * mind is that content handling is an property of the stream handling within
 * the JVM itself, by register an content handler factory you tell the JVM
 * to call your code instead of its default handlers.</p>
 *
 * <p>Case 1: If your application don't use any other ContentHandlerFactory:</p>
 * <pre>
 * ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
 * URLConnection.setContentHandlerFactory(factory);
 * </pre>
 * 
 * <p>Case 2: If your application use another ContentHandlerFactory, then either
 * decorate your existing class or create a subclass inheriting it:
 * <pre>
 * public class YourContentHandlerFactory implements ContentHandlerFactory {
 *     public ContentHandler createContentHandler(String mimetype) {
 *         ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
 *         ContentHandler handle = factory.createContentHandler(mimetype);
 *         if(handler != null) {
 *             return handler;
 *         }
 *         // ... your code follow here ...
 *     }
 * }
 * </pre>
 *
 * @see java.net.HttpURLConnection
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class ResponseDecoderFactory implements ContentHandlerFactory {

    /**
     * @return The singleton instance of the response decoder factory object.
     */
    public static ContentHandlerFactory getInstance() {
        if (factory == null) {
            factory = new ResponseDecoderFactory();
        }
        return factory;
    }

    /**
     * Don't call this function direct, it should only be called by the JVM.
     * @param mimetype The MIME type.
     * @return The content handler for the MIME type.
     */
    public ContentHandler createContentHandler(String mimetype) {
        if(mimetype.compareTo("text/xml") == 0) {
            return new XmlResponseDecoder();
        } else if(mimetype.compareTo("text/x-foa") == 0) {
            return new FoaResponseDecoder();
        }
        return null;
    }
    static ResponseDecoderFactory factory = null;
}
