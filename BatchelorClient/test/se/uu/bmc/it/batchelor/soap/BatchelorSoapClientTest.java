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

package se.uu.bmc.it.batchelor.soap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.soap.util.BatchelorSoapLoopback;

import java.rmi.RemoteException;

/**
 * Unit test against local deployed dummy service (BatchelorWebService). This
 * class tests that SoapWebService behaves as expected.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapClientTest {

    private BatchelorSoapClient service;

    public BatchelorSoapClientTest() {
        service = new BatchelorSoapLoopback();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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
     * Test of version method, of class SoapWebService.
     */
    @Test
    public void testVersion() {
        System.out.println("(i) *** BatchelorSoapClientTest -> version()");
        try {
            String result = service.version();
            assertNotNull(result);
            System.out.println("(+) Version: " + result);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of enqueue method, of class SoapWebService.
     */
    @Test
    public void testEnqueue() {
        System.out.println("(i) *** BatchelorSoapClientTest -> enqueue(String)");
        try {
            EnqueueResult[] results = service.enqueue("test");
            assertNotNull(results);
            System.out.println("(+) EnqueueResult:");
            for (EnqueueResult result : results) {
                System.out.println("(i)   " + result.getJobID() + ":");
                System.out.println("(i)\tDate:\t" + result.getDate());
                System.out.println("(i)\tTime:\t" + result.getTime());
                System.out.println("(i)\tStamp:\t" + result.getStamp());
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of dequeue method, of class SoapWebService.
     */
    @Test
    public void testDequeue() {
        System.out.println("(i) *** BatchelorSoapClientTest -> dequeue(JobIdentity)");
        try {
            assertEquals(true, service.dequeue(null));
            assertEquals(true, service.dequeue(new JobIdentity()));
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of queue method, of class SoapWebService.
     */
    @Test
    public void testQueue() {
        System.out.println("(i) *** BatchelorSoapClientTest -> queue(String, String)");
        try {
            QueuedJob[] results;
            results = service.queue(null, null);
            assertNotNull(results);
            results = service.queue("", null);
            assertNotNull(results);
            results = service.queue(null, "");
            assertNotNull(results);
            results = service.queue("", "");
            assertNotNull(results);
            System.out.println("(+) QueuedJob:");
            for (QueuedJob result : results) {
                JobIdentity job = result.getJobIdentity();
                System.out.println("(i)   " + job.getJobID());
                System.out.println("(i)\tResult:\t" + job.getResult());
                System.out.println("(i)\tState:\t" + result.getState());
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of watch method, of class SoapWebService.
     */
    @Test
    public void testWatch() {
        System.out.println("(i) *** BatchelorSoapClientTest -> watch(int)");
        try {
            QueuedJob[] results;
            results = service.watch(0);
            assertNotNull(results);
            results = service.watch(-1);
            assertNotNull(results);
            results = service.watch(1234567890);
            assertNotNull(results);
            System.out.println("(+) QueuedJob:");
            for (QueuedJob result : results) {
                JobIdentity job = result.getJobIdentity();
                System.out.println("(i)   " + job.getJobID());
                System.out.println("(i)\tResult:\t" + job.getResult());
                System.out.println("(i)\tState:\t" + result.getState());
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of suspend method, of class SoapWebService.
     */
    @Test
    public void testSuspend() {
        System.out.println("(i) *** BatchelorSoapClientTest -> suspend(JobIdentity)");
        try {
            assertEquals(true, service.suspend(null));
            assertEquals(true, service.suspend(new JobIdentity()));
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of resume method, of class SoapWebService.
     */
    @Test
    public void testResume() {
        System.out.println("(i) *** BatchelorSoapClientTest -> resume(JobIdentity)");
        try {
            assertEquals(true, service.resume(null));
            assertEquals(true, service.resume(new JobIdentity()));
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of opendir method, of class SoapWebService.
     */
    @Test
    public void testOpendir() {
        System.out.println("(i) *** BatchelorSoapClientTest -> opendir()");
        try {
            JobIdentity[] results = service.opendir();
            assertNotNull(results);
            System.out.println("(+) JobIdentity:");
            for (JobIdentity result : results) {
                System.out.println("(i)   " + result.getJobID());
                System.out.println("(i)\tResult:\t" + result.getResult());
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of readdir method, of class SoapWebService.
     */
    @Test
    public void testReaddir() {
        System.out.println("(i) *** BatchelorSoapClientTest -> readdir(JobIdentity)");
        try {
            String[] results;
            results = service.readdir(null);
            assertNotNull(results);
            results = service.readdir(new JobIdentity());
            assertNotNull(results);
            System.out.println("(+) Files:");
            for (String result : results) {
                System.out.println("(i) " + result);
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of fopen method, of class SoapWebService.
     */
    @Test
    public void testFopen() {
        System.out.println("(i) *** BatchelorSoapClientTest -> fopen(JobIdentity, String)");
        try {
            byte[] result;
            String str;
            result = service.fopen(null, null);
            assertNotNull(result);
            result = service.fopen(null, "");
            assertNotNull(result);
            result = service.fopen(new JobIdentity(), null);
            assertNotNull(result);
            result = service.fopen(new JobIdentity(), "");
            assertNotNull(result);
            str = new String(result);
            System.out.println("(+) Result: " + str);
            assertEquals(100, str.length());
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }
}