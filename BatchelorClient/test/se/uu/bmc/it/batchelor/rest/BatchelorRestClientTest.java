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

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.Proxy;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ContentHandlerFactory;
import java.rmi.RemoteException;

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
import se.uu.bmc.it.batchelor.QueueFilterResult;
import se.uu.bmc.it.batchelor.QueueSortResult;

/**
 * Unit test of the REST interface for the Batchelor service
 * running on localhost.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorRestClientTest {

    private static String server = "http://localhost/batchelor/ws/rest";
    private BatchelorRestClient client;
    private static JobIdentity job;
    private static int stamp;

    public BatchelorRestClientTest() throws MalformedURLException {
        client = new BatchelorRestClient(new URL(server));
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

    @Test
    public void testContructors() {
        try {
            URL url = new URL("http://localhost");
            BatchelorRestClient obj;
            obj = new BatchelorRestClient();
            assertEquals(null, obj.getServiceAddress());
            obj = new BatchelorRestClient(url);
            assertEquals(url, obj.getServiceAddress());
        } catch (Exception e) {
            fail("(-) Catched unexpected exception");
        }
    }

    /**
     * Test of setResponseEncoder method, of class BatchelorRestClient.
     */
    @Test
    public void testSetResponseEncoder() {
        System.out.println("(i) *** BatchelorRestClientTest -> setResponseEncoder(ResponseEncoder)");
        for (ResponseEncoder encoder : ResponseEncoder.values()) {
            client.setResponseEncoder(encoder);
        }
        client.setResponseEncoder(ResponseEncoder.DEFAULT);
    }

    /**
     * Test of setServiceAddress method, of class BatchelorRestClient.
     */
    @Test
    public void testSetServiceAddress() {
        System.out.println("(i) *** BatchelorRestClientTest -> setServiceAddress(URL)");
        try {
            client.setServiceAddress(new URL(server));
        } catch (MalformedURLException e) {
            fail("(-) Failed set service address: " + e);
        }
    }

    /**
     * Test of getServiceAddress method, of class BatchelorRestClient.
     */
    @Test
    public void testGetServiceAddress() {
        System.out.println("(i) *** BatchelorRestClientTest -> getServiceAddress()");
        assertEquals(server, client.getServiceAddress().toString());
    }

    /**
     * Test of setProxy method, of class BatchelorRestClient.
     */
    @Test
    public void testSetProxy() {
        System.out.println("(i) *** BatchelorRestClientTest -> setProxy(Proxy)");
        try {
            client.setProxy(null);
            client.setProxy(Proxy.NO_PROXY);
        } catch (Exception e) {
            fail("(-) Failed set proxy: " + e);
        }
    }

    /**
     * Test of version method, of class BatchelorRestClient.
     */
    @Test
    public void testVersion() {
        System.out.println("(i) *** BatchelorRestClientTest -> version()");
        try {
            String result = client.version();
            assertNotNull(result);
            assertEquals("1.0", result);
            System.out.println("(+) Received version " + result);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of queue method, of class BatchelorRestClient.
     */
    @Test
    public void testQueue() {
        System.out.println("(i) *** BatchelorRestClientTest -> queue(QueueSortResult, QueueFilterResult)");
        try {
            List<QueuedJob> results;
            for (QueueSortResult sort : QueueSortResult.values()) {
                results = client.queue(sort, null);
                assertNotNull(results);
                for (QueueFilterResult filter : QueueFilterResult.values()) {
                    results = client.queue(null, filter);
                    assertNotNull(results);
                    results = client.queue(sort, filter);
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
            results = client.queue(null, null);
            assertNotNull(results);
            results = client.queue(QueueSortResult.NONE, QueueFilterResult.ALL);
            assertNotNull(results);
            System.out.println("(+) QueuedJob:");
            for (QueuedJob result : results) {
                job = result.getJobIdentity();
                assertNotNull(job.getJobID());
                assertNotNull(job.getResult());
                assertNotNull(result.getState());
                System.out.printf("(i) %s\n", result);
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    private void enqueueCheckResult(List<EnqueueResult> results) {
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
        // We need to sleep at least one second to avoid enqueue jobs too fast.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("(!) Spurious wakeup: " + e);
        }
    }

    @Test
    public void testEnqueue_ByteArray() {
        System.out.println("(i) *** BatchelorRestClientTest -> enqueue(byte[])");
        try {
            List<EnqueueResult> results = client.enqueue("test".getBytes());
            enqueueCheckResult(results);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    @Test
    public void testEnqueue_File() {
        System.out.println("(i) *** BatchelorRestClientTest -> enqueue(File)");
        try {
            String string = "Hello world!";

            File file = File.createTempFile("batchelor", null);
            FileOutputStream output = new FileOutputStream(file);
            output.write(string.getBytes());
            output.close();

            List<EnqueueResult> results = client.enqueue(file);
            enqueueCheckResult(results);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        } catch (IOException e) {
            fail("(!) IOException: " + e.getMessage());
        }
    }

    @Test
    public void testEnqueue_InputStream() {
        System.out.println("(i) *** BatchelorRestClientTest -> enqueue(InputStream)");
        try {
            String string = "Hello world!";

            File file = File.createTempFile("batchelor", null);
            FileOutputStream output = new FileOutputStream(file);
            output.write(string.getBytes());
            output.close();

            InputStream input = new FileInputStream(file);
            List<EnqueueResult> results = client.enqueue(input);
            enqueueCheckResult(results);
            input.close();

            System.out.println("(i) Wait for last job to complete: ");
            try {
                stamp = results.get(0).getStamp();
                while (client.watch(stamp).isEmpty()) {
                    Thread.sleep(1000);
                    System.out.print(".");
                }
            } catch (InterruptedException e) {
                fail("(!) Argh, spurious wakeup in current thread!!");
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        } catch (IOException e) {
            fail("(!) IOException: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------------
    //  The following test should be runned in this fixed, pre-defined
    //  order as we use a real enqueued job when testing.
    // ----------------------------------------------------------------------
    /**
     * Test of enqueue method, of class BatchelorRestClient.
     */
    @Test
    public void testEnqueue_String() {
        System.out.println("(i) *** BatchelorRestClientTest -> enqueue(String)");
        try {
            List<EnqueueResult> results = client.enqueue("test");
            enqueueCheckResult(results);
            // Keep job for later use. Note that enqueue might create multiple
            // subjobs from a single submit. Also keep the timestamp for later
            // call to watch().
            job = results.get(0).getJobIdentity();
            stamp = results.get(0).getStamp();
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of stat method, of class BatchelorRestClient.
     */
    @Test
    public void testStat() {
        System.out.println("(i) *** BatchelorRestClientTest -> stat(JobIdenity)");
        try {
            assertEquals(false, client.stat(null));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        try {
            assertEquals(false, client.stat(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Stat previous queued job:
        try {
            QueuedJob result = client.stat(job);
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
     * Test of suspend method, of class BatchelorRestClient.
     */
    @Test
    public void testSuspend() {
        System.out.println("(i) *** BatchelorRestClientTest -> suspend(JobIdentity)");
        try {
            assertEquals(false, client.suspend(null));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        try {
            assertEquals(false, client.suspend(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Suspend previous queued job:
        try {
            boolean result = client.suspend(job);
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
     * Test of resume method, of class BatchelorRestClient.
     */
    @Test
    public void testResume() {
        System.out.println("(i) *** BatchelorRestClientTest -> resume(JobIdentity)");
        try {
            assertEquals(false, client.suspend(null));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }
        try {
            assertEquals(false, client.suspend(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Resume previous suspended queued job:
        try {
            boolean result = client.resume(job);
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
     * Test of watch method, of class BatchelorRestClient.
     */
    @Test
    public void testWatch() {
        System.out.println("(i) *** BatchelorRestClientTest -> watch(int)");
        try {
            List<QueuedJob> results;
            results = client.watch(0);
            assertNotNull(results);
            results = client.watch(-1);
            assertNotNull(results);
            results = client.watch(1234567890);
            assertNotNull(results);
            results = client.watch(1999999999);  // Sometime in the future
            assertNotNull(results);
            assertTrue(results.isEmpty());
            // Watch previous enqueued job:
            System.out.println("(i) Watching " + job);
            // Busy wait for job to complete:
            System.out.print("(i) Waiting for job to complete");
            while (results.isEmpty()) {
                results = client.watch(stamp);
                assertNotNull(results);
                System.out.print(".");
                Thread.sleep(1000);
            }
            System.out.println(" done.");
            assertTrue(results.size() >= 1);
            System.out.println("(+) QueuedJob:");
            for (QueuedJob result : results) {
                JobIdentity ident = result.getJobIdentity();
                assertNotNull(ident.getJobID());
                assertNotNull(ident.getResult());
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
     * Test of opendir method, of class BatchelorRestClient.
     */
    @Test
    public void testOpendir() {
        System.out.println("(i) *** BatchelorRestClientTest -> opendir()");
        try {
            List<JobIdentity> results = client.opendir();
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
     * Test of readdir method, of class BatchelorRestClient.
     */
    @Test
    public void testReaddir() {
        System.out.println("(i) *** BatchelorRestClientTest -> readdir(JobIdentity)");
        try {
            assertEquals(false, client.readdir(null));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, client.readdir(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Test read list of files in job directory of newly started job.
        try {
            List<String> results = client.readdir(job);
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
     * Test of fopen method, of class BatchelorRestClient.
     */
    @Test
    public void testFopen() {
        System.out.println("(i) *** BatchelorRestClientTest -> fopen(JobIdentity, String)");

        // Test that expected exceptions gets thrown:
        try {
            assertEquals(false, client.fopen(null, null));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, client.fopen(null, ""));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, client.fopen(new JobIdentity(), null));
            fail("(-) Expected exception");
        } catch (NullPointerException e) {
            System.out.println("(+) Catched expected null pointer exception.");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        try {
            assertEquals(false, client.fopen(new JobIdentity(), ""));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Test get contents of stdout file in previous enqueued job:
        try {
            byte[] result;
            String str;

            result = client.fopen(job, "queued");
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
     * Test of fopen method, of class BatchelorRestClient.
     */
    @Test
    public void testFopen_FileObject() {
        System.out.println("(i) *** BatchelorRestClientTest -> fopen(JobIdentity, String, int)");
        try {
            se.uu.bmc.it.batchelor.rest.schema.File file = client.fopen(job, "queued", 1000);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        } catch (SocketTimeoutException e) {
            fail("(!) Timeout waiting for server response: " + e.getMessage());
        }
    }

    /**
     * Test of fopen method, of class BatchelorRestClient.
     */
    @Test
    public void testFopen_WriteFile() {
        System.out.println("(i) *** BatchelorRestClientTest -> fopen(JobIdentity, String, File)");
        try {
            File file = File.createTempFile("batchelor", "queued");
            client.fopen(job, "queued", file);
            byte[] result1 = new byte[10];
            byte[] result2 = client.fopen(job, "queued");
            FileInputStream input = new FileInputStream(file);
            input.read(result1);
            assertNotNull(result1);
            assertNotNull(result2);
            assertEquals(new String(result1), new String(result2));
        } catch (IOException e) {
            fail("(!) Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test of dequeue method, of class BatchelorRestClient.
     */
    @Test
    public void testDequeue() {
        System.out.println("(i) *** BatchelorRestClientTest -> dequeue(JobIdentity)");

        try {
            client.dequeue(new JobIdentity());
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected remote exception.");
        }

        // Test dequeue previous enqueued job:
        try {
            boolean result = client.dequeue(job);
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
