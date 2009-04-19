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
package se.uu.bmc.it.batchelor.rest;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.ContentHandlerFactory;
import java.net.URLConnection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import se.uu.bmc.it.batchelor.rest.schema.Result;
import se.uu.bmc.it.batchelor.rest.schema.Link;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class HttpServerConnectionTest {

    static final String WS_REST_ROOT_URL = "http://localhost/batchelor/ws/rest";
    static private ContentHandlerFactory factory;
    private HttpServerConnection request;

    public HttpServerConnectionTest() {
        try {
            URL url = new URL(WS_REST_ROOT_URL);
            request = new HttpServerConnection(url);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        factory = ResponseDecoderFactory.getInstance();
        URLConnection.setContentHandlerFactory(factory);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getConnection method, of class HttpServerConnection.
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("(i) *** HttpServerConnectionTest -> getConnection()");
        HttpURLConnection connection = request.getConnection();
        assertNotNull(connection);
    }

    /**
     * Test of openConnection method, of class HttpServerConnection.
     */
    @Test
    public void testOpenConnection() throws Exception {
        System.out.println("(i) *** HttpServerConnectionTest -> openConnection()");
        request.openConnection();
    }

    /**
     * Test of getServerResponse method, of class HttpServerConnection.
     */
    @Test
    public void testGetServerResponse_0args() throws Exception {
        System.out.println("(i) *** HttpServerConnectionTest -> getServerResponse()");
        HttpURLConnection connection = request.getConnection();
        connection.setRequestMethod("GET");
        Result result = request.getServerResponse();
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
        assertEquals("OK", connection.getResponseMessage());
        System.out.printf("(+) HTTP status: %d %s\n",
                connection.getResponseCode(),
                connection.getResponseMessage());
        assertNotNull(result);
        assertEquals(null, result.getError());
        assertEquals("link", result.getType());
        for(Link link : result.getLink()) {
            System.out.printf("(i) Link: href=%s\n", link.getHref());
        }
    }

    /**
     * Test of getServerResponse method, of class HttpServerConnection.
     */
    @Test
    public void testGetServerResponse_URL() throws Exception {
        System.out.println("(i) *** HttpServerConnectionTest -> getServerResponse(URL)");

        request.setURL(new URL(WS_REST_ROOT_URL + "/version"));
        HttpURLConnection connection = request.getConnection();
        connection.setRequestMethod("GET");
        Result result = request.getServerResponse();
        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
        assertEquals("OK", connection.getResponseMessage());
        System.out.printf("(+) HTTP status: %d %s\n",
                connection.getResponseCode(),
                connection.getResponseMessage());
        assertNotNull(result);
        assertEquals(null, result.getError());
        assertEquals("version", result.getType());
        System.out.printf("(+) Type = %s\n", result.getType());
        assertEquals("success", result.getState());
        System.out.printf("(+) State = %s\n", result.getState());
        assertEquals("1.0", result.getVersion());
        System.out.printf("(+) Version = %s\n", result.getVersion());
    }

    /**
     * Test of usingProxy method, of class HttpServerConnection.
     */
    @Test
    public void testUsingProxy() {
        System.out.println("(i) *** HttpServerConnectionTest -> usingProxy()");
        assertEquals(false, request.usingProxy());
    }

    /**
     * Test of disconnect method, of class HttpServerConnection.
     */
    @Test
    public void testDisconnect() throws Exception {
        System.out.println("(i) *** HttpServerConnectionTest -> disconnect()");
        request.disconnect();
    }
}
