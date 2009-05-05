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
import se.uu.bmc.it.batchelor.rest.schema.ObjectFactory;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class HttpServerResponseTest {

    final private static int ERROR_CODE = 1;
    final private static String ERROR_STRING = "The error message";
    final private static String ERROR_ORIGIN = "local";
    final private static String FILE_NAME = "Filename";
    final private static String FILE_ENCODING = "binary";
    final private static String FILE_CONTENT = "hello world!";
    final private static long FILE_SIZE = FILE_CONTENT.length();
    final private static String JOB_ID = "Job XYZ";
    final private static String JOB_TIMEZONE = "CET";
    final private static String JOB_STATE = "finished";
    final private static int JOB_FINISHED = 123456789;
    final private static int JOB_STARTED = 234567891;
    final private static int JOB_STDOUT = 345678912;
    final private static int JOB_QUEUED = 456789123;
    final private static int JOB_RESULT = 567891234;
    final private static String LINK_HREF = "http://java.sun.com";
    final private static String LINK_GET = "link";
    final private static String LINK_POST = "job";
    final private static String LINK_PUT = "job";
    final private static String LINK_DELETE = "job";
    final private static String STATUS = "Status OK";
    final private static String VERSION = "1.0";
    private static Error errorObject;
    private static File fileObject;
    private static Job jobObject;
    private static Link linkObject;
    private static Result resultError;
    private static Result resultFile;
    private static Result resultStatus;
    private static Result resultVersion;
    private static Result resultJob;
    private static Result resultLink;

    public HttpServerResponseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ObjectFactory factory = new ObjectFactory();

        errorObject = factory.createError();
        errorObject.setCode(ERROR_CODE);
        errorObject.setMessage(ERROR_STRING);
        errorObject.setOrigin(ERROR_ORIGIN);

        resultError = factory.createResult();
        resultError.setError(errorObject);
        resultError.setState("failed");
        resultError.setType("error");

        fileObject = factory.createFile();
        fileObject.setName(FILE_NAME);
        fileObject.setSize(FILE_SIZE);
        fileObject.setEncoding(FILE_ENCODING);
        fileObject.setContent(FILE_CONTENT);

        resultFile = factory.createResult();
        resultFile.setFile(fileObject);
        resultFile.setState("success");
        resultFile.setType("file");

        resultStatus = factory.createResult();
        resultStatus.setStatus(STATUS);
        resultStatus.setState("success");
        resultStatus.setType("status");

        resultVersion = factory.createResult();
        resultVersion.setVersion(VERSION);
        resultVersion.setState("success");
        resultVersion.setType("version");

        jobObject = factory.createJob();
        jobObject.setFinished(JOB_FINISHED);
        jobObject.setJobid(JOB_ID);
        jobObject.setQueued(JOB_QUEUED);
        jobObject.setResult(JOB_RESULT);
        jobObject.setStarted(JOB_STARTED);
        jobObject.setState(JOB_STATE);
        jobObject.setStdout(JOB_STDOUT);
        jobObject.setTimezone(JOB_TIMEZONE);

        resultJob = factory.createResult();
        resultJob.getJob().add(jobObject);
        resultJob.getJob().add(jobObject);
        resultJob.getJob().add(jobObject);
        resultJob.setState("success");
        resultJob.setType("job");

        linkObject = factory.createLink();
        linkObject.setHref(LINK_HREF);
        linkObject.setGet(LINK_GET);
        linkObject.setPost(LINK_POST);
        linkObject.setPut(LINK_PUT);
        linkObject.setDelete(LINK_DELETE);

        resultLink = factory.createResult();
        resultLink.getLink().add(linkObject);
        resultLink.getLink().add(linkObject);
        resultLink.getLink().add(linkObject);
        resultLink.setState("success");
        resultLink.setType("link");
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
     * Test of getResult method, of class HttpServerResponse.
     */
    @Test
    public void testGetResult() {
        System.out.println("(i) *** HttpServerResponseTest -> getResult()");
        HttpServerResponse response = new HttpServerResponse(resultError);
        assertEquals(resultError, response.getResult());
    }

    /**
     * Test of isErrorObject method, of class HttpServerResponse.
     */
    @Test
    public void testIsErrorObject() {
        System.out.println("(i) *** HttpServerResponseTest -> isErrorObject()");
        HttpServerResponse response = new HttpServerResponse(resultError);
        assertTrue(response.isErrorObject());
        response.setResult(resultFile);
        assertFalse(response.isErrorObject());
    }

    /**
     * Test of isFileObject method, of class HttpServerResponse.
     */
    @Test
    public void testIsFileObject() {
        System.out.println("(i) *** HttpServerResponseTest -> isFileObject()");
        HttpServerResponse response = new HttpServerResponse(resultFile);
        assertTrue(response.isFileObject());
        response.setResult(resultError);
        assertFalse(response.isFileObject());
    }

    /**
     * Test of isJobCollection method, of class HttpServerResponse.
     */
    @Test
    public void testIsJobCollection() {
        System.out.println("(i) *** HttpServerResponseTest -> isJobCollection()");
        HttpServerResponse response = new HttpServerResponse(resultJob);
        assertTrue(response.isJobCollection());
        response.setResult(resultFile);
        assertFalse(response.isJobCollection());
    }

    /**
     * Test of isLinkCollection method, of class HttpServerResponse.
     */
    @Test
    public void testIsLinkCollection() {
        System.out.println("(i) *** HttpServerResponseTest -> isLinkCollection()");
        HttpServerResponse response = new HttpServerResponse(resultLink);
        assertTrue(response.isLinkCollection());
        response.setResult(resultJob);
        assertFalse(response.isLinkCollection());
    }

    /**
     * Test of isStatusObject method, of class HttpServerResponse.
     */
    @Test
    public void testIsStatusObject() {
        System.out.println("(i) *** HttpServerResponseTest -> isStatusObject()");
        HttpServerResponse response = new HttpServerResponse(resultStatus);
        assertTrue(response.isStatusObject());
        response.setResult(resultError);
        assertFalse(response.isStatusObject());
    }

    /**
     * Test of isVersionObject method, of class HttpServerResponse.
     */
    @Test
    public void testIsVersionObject() {
        System.out.println("(i) *** HttpServerResponseTest -> isVersionObject()");
        HttpServerResponse response = new HttpServerResponse(resultVersion);
        assertTrue(response.isVersionObject());
        response.setResult(resultError);
        assertFalse(response.isVersionObject());
    }

    /**
     * Test of getState method, of class HttpServerResponse.
     */
    @Test
    public void testGetState() {
        System.out.println("(i) *** HttpServerResponseTest -> getState()");
        HttpServerResponse response = new HttpServerResponse(resultVersion);
        assertEquals(HttpServerResponse.State.Success, response.getState());
        response.setResult(resultJob);
        assertEquals(HttpServerResponse.State.Success, response.getState());
        response.setResult(resultError);
        assertEquals(HttpServerResponse.State.Failed, response.getState());
    }

    /**
     * Test of getType method, of class HttpServerResponse.
     */
    @Test
    public void testGetType() {
        System.out.println("(i) *** HttpServerResponseTest -> getType()");
        HttpServerResponse response = new HttpServerResponse(resultVersion);
        assertEquals(HttpServerResponse.Type.VersionObject, response.getType());
        response.setResult(resultStatus);
        assertEquals(HttpServerResponse.Type.StatusObject, response.getType());
        response.setResult(resultError);
        assertEquals(HttpServerResponse.Type.ErrorObject, response.getType());
        response.setResult(resultFile);
        assertEquals(HttpServerResponse.Type.FileObject, response.getType());
        response.setResult(resultJob);
        assertEquals(HttpServerResponse.Type.JobCollection, response.getType());
        response.setResult(resultLink);
        assertEquals(HttpServerResponse.Type.LinkCollection, response.getType());
        assertFalse(HttpServerResponse.Type.Unknown == response.getType());
        assertTrue(HttpServerResponse.Type.Unknown != response.getType());
    }

    /**
     * Test of getErrorOrigin method, of class HttpServerResponse.
     */
    @Test
    public void testGetErrorOrigin() {
        System.out.println("(i) *** HttpServerResponseTest -> getErrorOrigin()");
        HttpServerResponse response = new HttpServerResponse(resultError);
        assertEquals(HttpServerResponse.ErrorOrigin.Local, response.getErrorOrigin(response.getResult().getError()));
    }

    /**
     * Test of toString method, of class HttpServerResponse.
     */
    @Test
    public void testToString() {
        System.out.println("(i) *** HttpServerResponseTest -> toString()");
        HttpServerResponse response = new HttpServerResponse(resultVersion);
        System.out.println(response.toString());
        response.setResult(resultStatus);
        System.out.println(response.toString());
        response.setResult(resultError);
        System.out.println(response.toString());
        response.setResult(resultFile);
        System.out.println(response.toString());
        response.setResult(resultLink);
        System.out.println(response.toString());
        response.setResult(resultJob);
        System.out.println(response.toString());
    }
}
