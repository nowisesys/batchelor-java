/*
 * Web service library for the Batchelor batch queued queue.
 * Copyright (C) 2009-2018 Anders Lövgren (Nowise Systems/Uppsala University (BMC-IT)
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ContentHandlerFactory;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import se.uu.bmc.it.batchelor.BatchelorWebServiceClientTest;
import se.uu.bmc.it.batchelor.EnqueueResult;

/**
 * Unit test of the REST interface for the Batchelor SERVICE running on localhost. Changed the
 SERVICE member to test against another system.

 Note:

 It's normal for the suspend/resume tests to fail if the enqueued queued gets executed immediate.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorRestClientTest extends BatchelorWebServiceClientTest {

    private static final String SERVICE = "http://localhost/batchelor1/ws/rest";

    public BatchelorRestClientTest() throws MalformedURLException {
        super(new BatchelorRestClient(new URL(SERVICE)));
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
        HttpURLConnection.setContentHandlerFactory(factory);
        System.out.println("(i) *** BatchelorRestClientTest: using " + SERVICE + " as server");
    }

    @Test
    public void testContructors() {
        try {
            URL url = new URL("http://127.0.0.1");
            BatchelorRestClient obj;
            obj = new BatchelorRestClient();
            assertEquals(null, obj.getServiceAddress());
            obj = new BatchelorRestClient(url);
            assertEquals(url, obj.getServiceAddress());
        } catch (MalformedURLException e) {
            fail("(-) Catched unexpected exception");
        }
    }

    /**
     * Test of setResponseEncoder method, of class BatchelorRestClient.
     */
    @Test
    public void testSetResponseEncoder() {
        System.out.println("(i) *** BatchelorRestClientTest -> setResponseEncoder(ResponseEncoder)");
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
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
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            client.setServiceAddress(new URL(SERVICE));
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
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        assertEquals(SERVICE, client.getServiceAddress().toString());
    }

    /**
     * Test of setProxy method, of class BatchelorRestClient.
     */
    @Test
    public void testSetProxy() {
        System.out.println("(i) *** BatchelorRestClientTest -> setProxy(Proxy)");
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            client.setProxy(null);
            client.setProxy(Proxy.NO_PROXY);
        } catch (Exception e) {
            fail("(-) Failed set proxy: " + e);
        }
    }

//    /**
//     * Test of version method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testVersion() {
//        System.out.println("(i) *** BatchelorRestClientTest -> version()");
//        try {
//            String result = client.version();
//            assertNotNull(result);
//            assertEquals("1.0", result);
//            System.out.println("(+) Received version " + result);
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of queue method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testQueue() {
//        System.out.println("(i) *** BatchelorRestClientTest -> queue(QueueSortResult, QueueFilterResult)");
//        try {
//            List<QueuedJob> results;
//            for (QueueSortResult sort : QueueSortResult.values()) {
//                results = client.queue(sort, null);
//                assertNotNull(results);
//                for (QueueFilterResult filter : QueueFilterResult.values()) {
//                    results = client.queue(null, filter);
//                    assertNotNull(results);
//                    results = client.queue(sort, filter);
//                    assertNotNull(results);
//                    // Check that filtering is working:
//                    if (filter != QueueFilterResult.ALL && filter != QueueFilterResult.NONE) {
//                        System.out.println("(i) Filter: " + filter.getValue());
//                        for (QueuedJob result : results) {
//                            System.out.println("(i) Result: " + result);
//                            assertEquals(filter.getValue(), result.getState());
//                        }
//                    }
//                }
//            }
//            results = client.queue(null, null);
//            assertNotNull(results);
//            results = client.queue(QueueSortResult.NONE, QueueFilterResult.ALL);
//            assertNotNull(results);
//            System.out.println("(+) QueuedJob:");
//            for (QueuedJob result : results) {
//                JobIdentity jj = result.getJobIdentity();
//                assertNotNull(jj.getJobID());
//                assertNotNull(jj.getResult());
//                assertNotNull(result.getState());
//                System.out.printf("(i) %s\n", result);
//            }
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }

    private void enqueueCheckResult(List<EnqueueResult> results) {
        assertNotNull(results);
        assertTrue(!results.isEmpty());

        System.out.println("(+) EnqueueResult:");
        results.stream().map((result) -> {
            assertNotNull(result.getJobID());
            return result;
        }).map((result) -> {
            assertNotNull(result.getDate());
            return result;
        }).map((result) -> {
            assertNotNull(result.getStamp());
            return result;
        }).map((result) -> {
            assertNotNull(result.getTime());
            return result;
        }).forEachOrdered((result) -> {
            System.out.printf("(i) %s\n", result);
        });
    }

    @Test
    public void testEnqueue_ByteArray() {
        System.out.println("(i) *** BatchelorRestClientTest -> enqueue(byte[])");
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
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
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            String string = "Hello world!";

            File file = File.createTempFile("batchelor", null);
            try (FileOutputStream output = new FileOutputStream(file)) {
                output.write(string.getBytes());
            }

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
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            String string = "Hello world!";

            File file = File.createTempFile("batchelor", null);
            try (FileOutputStream output = new FileOutputStream(file)) {
                output.write(string.getBytes());
            }

            List<EnqueueResult> results;
            try (InputStream input = new FileInputStream(file)) {
                results = client.enqueue(input);
                enqueueCheckResult(results);
            }

            System.out.println("(i) Wait for last job to complete: ");
            try {
                int ss = results.get(0).getStamp();
                while (client.watch(ss).isEmpty()) {
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
    //  order as we use a real enqueued queued when testing.
    // ----------------------------------------------------------------------
    /**
     * Test of enqueue method, of class BatchelorRestClient.
     */
    @Test
    public void testEnqueue_String() {
        System.out.println("(i) *** BatchelorRestClientTest -> enqueue(String)");
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            List<EnqueueResult> results = client.enqueue("test");
            enqueueCheckResult(results);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

//    /**
//     * Test of stat method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testStat() {
//        System.out.println("(i) *** BatchelorRestClientTest -> stat(JobIdenity)");
//        try {
//            assertEquals(false, client.stat(null));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//        try {
//            assertEquals(false, client.stat(new JobIdentity()));
//            fail("(-) Remote exception expected");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        // 
//        // Stat queued job from test setup:
//        // 
//        try {
//            QueuedJob result = client.stat(queued.getJobIdentity());
//            assumeNotNull(result);
//            if (result != null) {
//                System.out.println("(i) Successful stat on " + queued.getJobIdentity());
//            } else {
//                System.out.println("(-) Failed stat " + queued.getJobIdentity());
//            }
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of suspend method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testSuspend() {
//        System.out.println("(i) *** BatchelorRestClientTest -> suspend(JobIdentity)");
//        try {
//            assertEquals(false, client.suspend(null));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//        try {
//            assertEquals(false, client.suspend(new JobIdentity()));
//            fail("(-) Remote exception expected");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        // 
//        // Suspend queued job from test setup:
//        // 
//        try {
//            boolean result = client.suspend(queued.getJobIdentity());
//            assumeTrue(result);
//            if (result) {
//                System.out.println("(i) Successful suspened " + queued.getJobIdentity());
//            } else {
//                System.out.println("(-) Failed suspend " + queued.getJobIdentity());
//            }
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of resume method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testResume() {
//        System.out.println("(i) *** BatchelorRestClientTest -> resume(JobIdentity)");
//        try {
//            assertEquals(false, client.resume(null));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//        try {
//            assertEquals(false, client.resume(new JobIdentity()));
//            fail("(-) Remote exception expected");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        // 
//        // Resume queued job from test setup:
//        // 
//        try {
//            EnqueueResult newjob = client.enqueue("test").get(0);
//            boolean result = client.resume(newjob.getJobIdentity());
//            assumeTrue(result);
//            if (result) {
//                System.out.println("(i) Successful resumed " + newjob.getJobIdentity());
//            } else {
//                System.out.println("(-) Failed resume " + newjob.getJobIdentity());
//            }
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of watch method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testWatch() {
//        System.out.println("(i) *** BatchelorRestClientTest -> watch(int)");
//        try {
//            List<QueuedJob> results;
//            results = client.watch(0);
//            assertNotNull(results);
//            results = client.watch(-1);
//            assertNotNull(results);
//            results = client.watch(1234567890);
//            assertNotNull(results);
//            results = client.watch(1999999999);  // Sometime in the future
//            assertNotNull(results);
//            assertTrue(results.isEmpty());
//
//            // 
//            // Watch new enqueued job:
//            // 
//            EnqueueResult newjob = client.enqueue("test").get(0);
//            System.out.println("(i) Watching " + newjob);
//
//            System.out.print("(i) Waiting for job to complete");
//            while (results.isEmpty()) {
//                results = client.watch(newjob.getStamp());
//                assertNotNull(results);
//                System.out.print(".");
//                Thread.sleep(1000);
//            }
//            System.out.println(" done.");
//            assertTrue(results.size() >= 1);
//            System.out.println("(+) QueuedJob:");
//            for (QueuedJob result : results) {
//                JobIdentity ident = result.getJobIdentity();
//                assertNotNull(ident.getJobID());
//                assertNotNull(ident.getResult());
//                assertNotNull(result.getState());
//                System.out.printf("(i) %s\n", result);
//            }
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        } catch (InterruptedException e) {
//            fail("(!) Interrupted exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of opendir method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testOpendir() {
//        System.out.println("(i) *** BatchelorRestClientTest -> opendir()");
//        try {
//            List<JobIdentity> results = client.opendir();
//            assertNotNull(results);
//            System.out.println("(+) JobIdentity:");
//            results.stream().map((result) -> {
//                assertNotNull(result.getJobID());
//                return result;
//            }).map((result) -> {
//                assertNotNull(result.getResult());
//                return result;
//            }).forEachOrdered((result) -> {
//                System.out.printf("(i) %s\n", result);
//            });
//
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of readdir method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testReaddir() {
//        System.out.println("(i) *** BatchelorRestClientTest -> readdir(JobIdentity)");
//        try {
//            assertEquals(false, client.readdir(null));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        try {
//            assertEquals(false, client.readdir(new JobIdentity()));
//            fail("(-) Remote exception expected");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        // Test read list of files in queued directory of newly started queued.
//        try {
//            List<String> results = client.readdir(queued.getJobIdentity());
//            assertNotNull(results);
//            System.out.println("(+) Files:");
//            results.stream().map((result) -> {
//                assertNotNull(result);
//                return result;
//            }).forEachOrdered((result) -> {
//                System.out.println("(i) " + result);
//            });
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Test of fopen method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testFopen() {
//        System.out.println("(i) *** BatchelorRestClientTest -> fopen(JobIdentity, String)");
//
//        // Test that expected exceptions gets thrown:
//        try {
//            assertEquals(false, client.fopen(null, null));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        try {
//            assertEquals(false, client.fopen(null, ""));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        try {
//            assertEquals(false, client.fopen(new JobIdentity(), null));
//            fail("(-) Expected exception");
//        } catch (NullPointerException e) {
//            System.out.println("(+) Catched expected null pointer exception.");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        try {
//            assertEquals(false, client.fopen(new JobIdentity(), ""));
//            fail("(-) Remote exception expected");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        // Test get contents of stdout file in previous enqueued queued:
//        try {
//            byte[] result;
//            String str;
//
//            result = client.fopen(queued.getJobIdentity(), "queued");
//            assertNotNull(result);
//            str = new String(result);
//            assertNotNull(str);
//            assertTrue(str.length() > 0);
//            System.out.println("(+) Result: " + str);
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        }
//    }

    /**
     * Test of fopen method, of class BatchelorRestClient.
     */
    @Test
    public void testFopen_FileObject() {
        System.out.println("(i) *** BatchelorRestClientTest -> fopen(JobIdentity, String, int)");
        
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            se.uu.bmc.it.batchelor.rest.schema.File result = client.fopen(queued.getJobIdentity(), "queued", 1000);
            assertNotNull(result);
            String str = result.getContent();
            assertNotNull(str);
            assertTrue(str.length() > 0);
            System.out.println("(+) Result: " + str);
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
        
        BatchelorRestClient client = (BatchelorRestClient)getServiceClient();
        try {
            File file = File.createTempFile("batchelor", "queued");
            client.fopen(queued.getJobIdentity(), "queued", file);
            byte[] result1 = new byte[10];
            byte[] result2 = client.fopen(queued.getJobIdentity(), "queued");
            FileInputStream input = new FileInputStream(file);
            input.read(result1);
            assertNotNull(result1);
            assertNotNull(result2);
            assertEquals(new String(result1), new String(result2));
        } catch (IOException e) {
            fail("(!) Unexpected exception: " + e.getMessage());
        }
    }

//    /**
//     * Test of dequeue method, of class BatchelorRestClient.
//     */
//    @Test
//    public void testDequeue() {
//        System.out.println("(i) *** BatchelorRestClientTest -> dequeue(JobIdentity)");
//
//        try {
//            client.dequeue(new JobIdentity());
//            fail("(-) Remote exception expected");
//        } catch (RemoteException e) {
//            System.out.println("(+) Catched expected exception.");
//        }
//
//        // 
//        // Create new job to dequeue:
//        // 
//        try {
//            EnqueueResult newjob = client.enqueue("test").get(0);
//            boolean result = client.dequeue(newjob.getJobIdentity());
//            assumeTrue(result);
//            if (result) {
//                System.out.println("(i) Successful dequeued " + queued.getJobIdentity());
//            } else {
//                System.out.println("(-) Failed dequeue " + queued.getJobIdentity());
//            }
//
//        } catch (RemoteException e) {
//            fail("(!) Remote exception: " + e.getMessage());
//        } finally {
//            queued = null;
//        }
//    }
}
