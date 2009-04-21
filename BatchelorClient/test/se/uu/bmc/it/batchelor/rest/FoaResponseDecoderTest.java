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

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.ContentHandlerFactory;
import java.io.FileNotFoundException;
import java.io.Reader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import se.uu.bmc.it.batchelor.rest.schema.Result;
import se.uu.bmc.it.batchelor.rest.schema.Error;
import se.uu.bmc.it.batchelor.rest.schema.File;
import se.uu.bmc.it.batchelor.rest.schema.Job;
import se.uu.bmc.it.batchelor.rest.schema.Link;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class FoaResponseDecoderTest {

    public FoaResponseDecoderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
        HttpURLConnection.setContentHandlerFactory(factory);
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
     * Test of getContent method, of class FoaResponseDecoder.
     */
    @Test
    public void testGetContent() {
        System.out.println("(i) *** FoaResponseDecoderTest -> getContent()");
        try {

            // Decode a version message:
            URL url = new URL("http://localhost/batchelor/ws/rest/version?encode=foa");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Result result = (Result) connection.getContent();
            assertNull(result.getError());
            assertEquals("success", result.getState());
            assertEquals("version", result.getType());
            assertNotNull(result.getVersion());
            connection.disconnect();
            System.out.println("(+) Successful decoded version message.");

            // Decode a job message:
            url = new URL("http://localhost/batchelor/ws/rest/queue/all/data?encode=foa");
            connection = (HttpURLConnection) url.openConnection();
            result = (Result) connection.getContent();
            assertNull(result.getError());
            assertEquals("success", result.getState());
            assertEquals("job", result.getType());
            assertNotNull(result.getJob());
            for (Job job : result.getJob()) {
                assertNotNull(job.getTimezone());
                assertNotNull(job.getJobid());
                assertNotNull(job.getResult());
            }
            connection.disconnect();
            System.out.println("(+) Successful decoded job message.");

            // Decode a link message:
            url = new URL("http://localhost/batchelor/ws/rest/result?encode=foa");
            connection = (HttpURLConnection) url.openConnection();
            result = (Result) connection.getContent();
            assertNull(result.getError());
            assertEquals("success", result.getState());
            assertEquals("link", result.getType());
            assertNotNull(result.getLink());
            for (Link link : result.getLink()) {
                assertNotNull(link.getHref());
            }
            connection.disconnect();
            System.out.println("(+) Successful decoded link message.");

            // Decode a file object from previous link object:
            url = new URL(result.getLink().get(0).getHref() + "/indata?encode=foa");
            connection = (HttpURLConnection) url.openConnection();
            result = (Result) connection.getContent();
            assertNull(result.getError());
            assertEquals("success", result.getState());
            assertEquals("file", result.getType());
            assertNotNull(result.getFile());
            File file = result.getFile();
            assertNull(file.getContent());
            assertNotNull(file.getEncoding());
            assertNotNull(file.getInputStreamReader());
            assertNotNull(file.getName());
            assertNotNull(file.getSize());
            assertTrue(file.getSize() != 0);
            // Test read size number of bytes from input stream:
            Reader stream = (Reader) file.getInputStreamReader();
            System.out.println("(i) Content of file " + file.getName());
            for (long i = 0; i < file.getSize(); ++i) {
                int ch = stream.read();
                assertTrue(ch != -1);
                System.out.print((char) ch);
            }
            connection.disconnect();
            System.out.println("(+) Successful decoded file message.");

            // Trying to access a invalid REST URI should return an error:
            url = new URL("http://localhost/batchelor/ws/rest/missing?encode=foa");
            connection = (HttpURLConnection) url.openConnection();
            result = (Result) connection.getContent();
            assertNotNull(result.getError());
            assertEquals("failed", result.getState());
            assertEquals("error", result.getType());
            connection.disconnect();
            System.out.println("(+) Successful received error on invalid URI.");

            // Trying to read a missing page will throw an exception:
            url = new URL("http://localhost/batchelor/notfound.html?encode=foa");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                result = (Result) connection.getContent();
            } catch (FileNotFoundException e) {
                System.out.printf("Catched expected FileNotFoundException:\n%s (%d %s)\n",
                    e.getMessage(),
                    connection.getResponseCode(),
                    connection.getResponseMessage());
            }
            connection.disconnect();

            // Trying to read invalid content will throw an exception:
            url = new URL("http://localhost/batchelor/?encode=foa");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            try {
                // This will throw an exception because no content handler is
                // registered to handle MIME type text/html.
                result = (Result) connection.getContent();
            } catch (ClassCastException e) {
                System.out.printf("Catched expected ClassCastException:\n%s (%d %s)\n",
                    e.getMessage(),
                    connection.getResponseCode(),
                    connection.getResponseMessage());
            }
            connection.disconnect();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}