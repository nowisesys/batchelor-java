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
 * HttpServerConnection.java
 *
 * Created: Apr 16, 2009, 1:51:24 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.io.IOException;

import se.uu.bmc.it.batchelor.rest.schema.Result;

/**
 * The server connection class implementing the HttpServerRequest interface
 * for the HTTP protocol.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class HttpServerConnection implements HttpServerRequest {

    /**
     * Creates an HTTP server connection using the URL arguement as address.
     * @param url The URL to open.
     */
    public HttpServerConnection(URL url) {
        this.url = url;
        this.proxy = null;
    }

    /**
     * Creates an HTTP server connection using the URL arguement as address.
     * The connection is made thru the proxy server.
     * @param url The URL to open.
     * @param proxy The proxy server to connect thru.
     */
    public HttpServerConnection(URL url, Proxy proxy) {
        this.url = url;
        this.proxy = proxy;
    }

    /**
     * Used internal to open a connection with the server.
     * @throws java.io.IOException
     */
    protected void openConnection() throws IOException {
        if (proxy != null) {
            connection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
    }

    public void setURL(URL url) throws IOException {
        if (connection != null) {
            disconnect();
        }
        this.url = url;
    }

    public void setProxy(Proxy proxy) throws IOException {
        if (connection != null) {
            disconnect();
        }
        this.proxy = proxy;
    }

    public HttpURLConnection getConnection() throws IOException {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    public Result getServerResponse() throws IOException {
        connection = getConnection();
        connection.connect();
        return (Result) connection.getContent();
    }

    public boolean usingProxy() {
        return proxy != null;
    }

    public void disconnect() throws IOException {
        if (connection != null) {
            if (connection.getDoInput()) {
                connection.getInputStream().close();
            }
            if (connection.getDoOutput()) {
                connection.getOutputStream().close();
            }
            connection.disconnect();
            connection = null;
        }
    }
    private URL url;
    private Proxy proxy;
    private HttpURLConnection connection;
}
