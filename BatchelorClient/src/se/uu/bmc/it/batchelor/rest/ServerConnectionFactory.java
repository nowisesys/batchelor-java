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
 * ServerConnectionFactory.java
 *
 * Created: Apr 21, 2009, 11:53:24 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.net.URL;
import java.rmi.RemoteException;

/**
 * This class creates HttpServerConnection or HttpsServerConnection objects.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class ServerConnectionFactory {

    /**
     * Get reference to the singleton ServerConnectionFactory object.
     * @return The factory object
     */
    static ServerConnectionFactory getFactory() {
        if (factory == null) {
            factory = new ServerConnectionFactory();
        }
        return factory;
    }

    /**
     * Create an HttpServerConnection object suitable for communication with
     * the REST service defined as endpoint by the url argument.
     * @param url The endpoint url.
     * @return An HTTP server connection object.
     */
    HttpServerRequest createHttpServerConnection(URL url) throws RemoteException {
        if (url.getProtocol().compareTo("http") == 0) {
            return new HttpServerConnection(url);
        }
        throw new RemoteException("No HttpServerRequest implementation exists for protocol " + url.getProtocol());
    }
    private static ServerConnectionFactory factory;
}
