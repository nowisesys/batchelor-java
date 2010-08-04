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
import static org.junit.Assume.*;

import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.QueueSortResult;
import se.uu.bmc.it.batchelor.QueueFilterResult;

import java.util.List;
import java.rmi.RemoteException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Unit test against a real server installation of Batchelor. The server
 * installation must have SOAP support enabled. Set a valid URL for the WSDL
 * location (in the urlWSDL string) before starting the test.
 *
 * This test depends on that starting a job makes it run for at least the 
 * time it takes to complete this test module. Add some sleep(1) calls in 
 * utils/script.sh (in the Batchelor installation) to ensure the job don't
 * terminate too soon. The suspend and resume test will fail unless job control
 * is enabled in the Batchelor configuration.
 *
 * Make sure that the indata passed by enqueue() is valid for the remote
 * service, or all sub sequent tests depending on enqueued job will fail.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapRemoteTest {

    private BatchelorSoapClient service;
    private static EnqueueResult queued;
    private static String urlWSDL = "http://localhost/batchelor/ws/schema/wsdl/?wsdl";

    public BatchelorSoapRemoteTest() throws MalformedURLException {
        URL url = new URL(urlWSDL);
        service = new BatchelorSoapClient(url);
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
        System.out.println("(i) *** BatchelorSoapRemoteTest -> version()");
        try {
            String result = service.version();
            assertNotNull(result);
            assertEquals("1.0", result);
            System.out.println("(+) Version: " + result);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of queue method, of class SoapWebService.
     */
    @Test
    public void testQueue() {
        System.out.println("(i) *** BatchelorSoapRemoteTest -> queue(QueueSortResult, QueueFilterResult)");
        try {
            List<QueuedJob> results;
            for (QueueSortResult sort : QueueSortResult.values()) {
                results = service.queue(sort, null);
                assertNotNull(results);
                for (QueueFilterResult filter : QueueFilterResult.values()) {
                    results = service.queue(null, filter);
                    assertNotNull(results);
                    results = service.queue(sort, filter);
                    assertNotNull(results);
                    // Check that filtering is working:
                    if (filter != QueueFilterResult.ALL &&
                        filter != QueueFilterResult.NONE) {
                        for (QueuedJob result : results) {
                            assertEquals(filter.getValue(), result.getState());
                        }
                    }
                }
            }
            results = service.queue(null, null);
            assertNotNull(results);
            results = service.queue(QueueSortResult.NONE, QueueFilterResult.ALL);
            assertNotNull(results);
            System.out.println("(+) QueuedJob:");
            for (QueuedJob result : results) {
                JobIdentity job = result.getJobIdentity();
                assertNotNull(job.getJobID());
                assertNotNull(job.getResult());
                assertNotNull(result.getState());
                System.out.printf("(i) %s\n", result);
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of enqueue method, of class SoapWebService.
     */
    @Test
    public void testEnqueue() {
        System.out.println("(i) *** BatchelorSoapRemoteTest -> enqueue(String)");
        try {
            List<EnqueueResult> results = service.enqueue("Test");
            assertNotNull(results);
            assertTrue(results.size() != 0);
            System.out.println("(+) EnqueueResult:");
            for (EnqueueResult result : results) {
                assertNotNull(result.getJobID());
                assertNotNull(result.getDate());
                assertNotNull(result.getStamp());
                assertNotNull(result.getTime());
                System.out.printf("(i) %s\n", result);
            }
            // Save first queued job and use it to test watch, suspend, resume
            // and dequeue methods.
            queued = results.get(0);
            System.out.println("(i) Saving enqueued job: " + queued);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of stat method, of class SoapWebService.
     */
    @Test
    public void testStat() {
        System.out.println("(i) *** BatchelorSoapRemoteTest -> stat(JobIdentity)");
        try {
            assertEquals(false, service.stat(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        try {
            assertEquals(false, service.stat(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Stat previous queued job:
        try {
            JobIdentity job = new JobIdentity();
            job.setJobID(queued.getJobID());
            job.setResult(queued.getResult());
            QueuedJob result = service.stat(job);
            assumeNotNull(result);
            if (result != null) {
                System.out.println("(i) Successful stat on " + job);
            } else {
                System.out.println("(-) Failed stat " + job);
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
        System.out.println("(i) *** BatchelorSoapRemoteTest -> suspend(JobIdentity)");
        try {
            assertEquals(false, service.suspend(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        try {
            assertEquals(false, service.suspend(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Suspend previous queued job:
        try {
            JobIdentity job = new JobIdentity();
            job.setJobID(queued.getJobID());
            job.setResult(queued.getResult());
            boolean result = service.suspend(job);
            assumeTrue(result);
            if (result) {
                System.out.println("(i) Successful suspened " + job);
            } else {
                System.out.println("(-) Failed suspend " + job);
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of resume method, of class SoapWebService.
     */
    @Test
    public void testResume() {
        System.out.println("(i) *** BatchelorSoapRemoteTest -> resume(JobIdentity)");
        try {
            assertEquals(false, service.suspend(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        try {
            assertEquals(false, service.suspend(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        // Resume previous suspended queued job:
        try {
            JobIdentity job = new JobIdentity();
            job.setJobID(queued.getJobID());
            job.setResult(queued.getResult());
            boolean result = service.resume(job);
            assumeTrue(result);
            if (result) {
                System.out.println("(i) Successful resumed " + job);
            } else {
                System.out.println("(-) Failed resume " + job);
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
        System.out.println("(i) *** BatchelorSoapRemoteTest -> watch(int)");
        try {
            List<QueuedJob> results;
            results = service.watch(0);
            assertNotNull(results);
            results = service.watch(-1);
            assertNotNull(results);
            results = service.watch(1234567890);
            assertNotNull(results);
            results = service.watch(1999999999);  // Sometime in the future
            assertNotNull(results);
            assertTrue(results.isEmpty());
            // Watch previous enqueued job:
            System.out.println("(i) Watching " + queued);
            // Busy wait for job to complete:
            System.out.print("(i) Waiting for job to complete");
            while (results.isEmpty()) {
                results = service.watch(queued.getStamp());
                assertNotNull(results);
                System.out.print(".");
                Thread.sleep(1000);
            }
            System.out.println(" done.");
            assertTrue(results.size() >= 1);
            System.out.println("(+) QueuedJob:");
            for (QueuedJob result : results) {
                JobIdentity job = result.getJobIdentity();
                assertNotNull(job.getJobID());
                assertNotNull(job.getResult());
                assertNotNull(result.getState());
                System.out.printf("(i) %s\n", result);
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        } catch (InterruptedException e) {
            fail("(!) Interrupted exception: " + e.getMessage());
        }
    }

    /**
     * Test of opendir method, of class SoapWebService.
     */
    @Test
    public void testOpendir() {
        System.out.println("(i) *** BatchelorSoapRemoteTest -> opendir()");

        try {
            List<JobIdentity> results = service.opendir();
            assertNotNull(results);
            System.out.println("(+) JobIdentity:");
            for (JobIdentity result : results) {
                assertNotNull(result.getJobID());
                assertNotNull(result.getResult());
                System.out.printf("(i) %s\n", result);
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
        System.out.println("(i) *** BatchelorSoapRemoteTest -> readdir(JobIdentity)");

        try {
            assertEquals(false, service.readdir(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, service.readdir(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Test read list of files in job directory of newly started job.
        try {
            JobIdentity job = new JobIdentity();
            job.setJobID(queued.getJobID());
            job.setResult(queued.getResult());
            List<String> results = service.readdir(job);
            assertNotNull(results);
            System.out.println("(+) Files:");
            for (String result : results) {
                assertNotNull(result);
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
        System.out.println("(i) *** BatchelorSoapRemoteTest -> fopen(JobIdentity, String)");

        // Test that expected exceptions gets thrown:
        try {
            assertEquals(false, service.fopen(null, null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, service.fopen(null, ""));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, service.fopen(new JobIdentity(), null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, service.fopen(new JobIdentity(), ""));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Test get contents of stdout file in previous enqueued job:
        try {
            byte[] result;
            String str;

            JobIdentity job = new JobIdentity();
            job.setJobID(queued.getJobID());
            job.setResult(queued.getResult());
            result = service.fopen(job, "queued");
            assertNotNull(result);
            str = new String(result);
            assertNotNull(str);
            assertTrue(str.length() > 0);
            System.out.println("(+) Result: " + str);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of dequeue method, of class SoapWebService.
     */
    @Test
    public void testDequeue() {
        System.out.println("(i) *** BatchelorSoapRemoteTest -> dequeue(JobIdentity)");

        try {
            assertEquals(false, service.dequeue(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, service.dequeue(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        // Test dequeue previous enqueued job:

        try {
            JobIdentity job = new JobIdentity();
            job.setJobID(queued.getJobID());
            job.setResult(queued.getResult());
            boolean result = service.dequeue(job);
            assumeTrue(result);
            if (result) {
                System.out.println("(i) Successful dequeued " + job);
            } else {
                System.out.println("(-) Failed dequeue " + job);
            }

        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }
}
