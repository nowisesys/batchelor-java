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
 * HttpServerRequest.java
 *
 * Created: Apr 16, 2009, 1:58:05 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.net.HttpURLConnection;
import java.io.IOException;

import se.uu.bmc.it.batchelor.rest.schema.Result;

/**
 * <p>The interface for communicating with the REST service over HTTP(S). Set
 * any connection parameters on the opened connection before calling any
 * methods that connects to the server.
 * <pre>
 * try {
 *     HttpServerRequest request = new HttpServerConnection(url);
 *     HttpURLConnection connection = request.getConnection();  // open 
 *     connection.setRequestMethod("GET");
 *     Result result = request.getServerResponse();             // connects
 * } catch(IOException e) {
 *     // ...
 * }
 * </pre>
 * </p>
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public interface HttpServerRequest {

    /**
     * <p>This method sends a REST service request and returns the server
     * response. This function will automatic connect to the server.</p>
     *
     * <p>The server response is decoded by one of the decoders (XML or FOA)
     * depending on the MIME type set by the server. This function returns
     * the result object that represent the raw decoded server response message.</p>
     *
     * @return The result object representing the server response.
     * @throws java.io.IOException
     */
    Result getServerResponse() throws IOException;

    /**
     * Returns the HTTP(S) server connection. The connection will be opened 
     * (but not yet connected) if its not already opened.
     * @return The opened connection.
     * @throws java.io.IOException
     */
    HttpURLConnection getConnection() throws IOException;

    /**
     * This function closes any open input or output streams, calls disconnect
     * on already connected connection and marks the connection as disconnected.
     */
    void disconnect() throws IOException;
}
