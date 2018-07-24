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
/**
 * BatchelorWebServiceClientTest.java
 *
 * Created: 2018-jul-23, 12:33:03
 * Author:  Anders Lövgren (Nowise Systems)
 */
package se.uu.bmc.it.batchelor;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Common web service client test.
 *
 * @author Anders Lövgren (Nowise Systems)
 */
@Ignore("Common base class for service client tests")
public abstract class BatchelorWebServiceClientTest {

    private final WebServiceInterface client;
    protected QueuedJob queued;

    public BatchelorWebServiceClientTest(WebServiceInterface client) {
        this.client = client;
    }

    public WebServiceInterface getServiceClient() {
        return client;
    }

    public EnqueueResult addQueuedJob() {
        try {
            return client.enqueue("test").get(0);
        } catch (RemoteException ex) {
            Logger.getLogger(BatchelorWebServiceClientTest.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Before
    public void setUp() {
        try {
            queued = client.queue(QueueSortResult.NONE, QueueFilterResult.ALL).get(0);
        } catch (RemoteException ex) {
            Logger.getLogger(BatchelorWebServiceClientTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testVersion() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> version()");

        try {
            String result = client.version();
            assertNotNull(result);
            System.out.println("(+) Version: " + result);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    @Test
    public void testEnqueue() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> enqueue(String)");

        try {
            client.enqueue(null);
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }

        try {
            List<EnqueueResult> results = client.enqueue("test");
            assertNotNull(results);
            System.out.println("(+) EnqueueResult:");
            results.stream().map((result) -> {
                System.out.println("(i)   " + result.getJobID() + ":");
                return result;
            }).map((result) -> {
                System.out.println("(i)\tDate:\t" + result.getDate());
                return result;
            }).map((result) -> {
                System.out.println("(i)\tTime:\t" + result.getTime());
                return result;
            }).forEachOrdered((result) -> {
                System.out.println("(i)\tStamp:\t" + result.getStamp());
            });
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    @Test
    public void testDequeue() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> dequeue(JobIdentity)");

        try {
            assertEquals(false, client.dequeue(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }

        try {
            assertEquals(false, client.dequeue(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }

        try {
            EnqueueResult newjob = addQueuedJob();
            boolean result = client.dequeue(newjob.getJobIdentity());
            assumeTrue(result);
            if (result) {
                System.out.println("(i) Successful dequeued " + newjob.getJobIdentity());
            } else {
                System.out.println("(-) Failed dequeue " + newjob.getJobIdentity());
            }
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    @Test
    public void testQueue() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> queue(QueueSortResult, QueueFilterResult)");

        try {
            List<QueuedJob> results;
            results = client.queue(QueueSortResult.NONE, QueueFilterResult.ALL);
            assertNotNull(results);

            // 
            // Test sort enum values:
            // 
            for (QueueSortResult sort : QueueSortResult.values()) {
                QueueFilterResult filter = QueueFilterResult.ALL;

                results = client.queue(sort, null);
                assertNotNull(results);
                results = client.queue(null, filter);
                assertNotNull(results);
                results = client.queue(sort, filter);
                assertNotNull(results);
                results = client.queue(null, null);
                assertNotNull(results);
            }

            // 
            // Test filter enum values:
            // 
            for (QueueFilterResult filter : QueueFilterResult.values()) {
                QueueSortResult sort = QueueSortResult.NONE;

                results = client.queue(sort, null);
                assertNotNull(results);
                results = client.queue(null, filter);
                assertNotNull(results);
                results = client.queue(sort, filter);
                assertNotNull(results);

                System.out.println("(i) Checking { filter=" + filter + ", sort=" + sort.getValue() + "}");

                // 
                // Skip pseudo filters that maps to non-unique state (i.e. waiting -> both
                // running and pending):
                // 
                if (filter != QueueFilterResult.ALL && filter != QueueFilterResult.NONE && filter != QueueFilterResult.WAITING) {
                    for (QueuedJob result : results) {
                        System.out.println("(i) Result: " + result);
                        assertEquals(filter.getValue(), result.getState());
                    }
                }
            }

            // 
            // Test list all jobs:
            // 
            results = client.queue(null, null);
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

    @Test
    public void testWatch() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> watch(int)");

        try {
            List<QueuedJob> results;
            results = client.watch(0);
            assertNotNull(results);
            results = client.watch(-1);
            assertNotNull(results);
            results = client.watch(1234567890);
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

    @Test
    public void testSuspend() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> suspend(JobIdentity)");

        try {
            client.suspend(null);
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }
        try {
            client.suspend(new JobIdentity());
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }
        try {
            client.suspend(new JobIdentity(null, 0));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }
        try {
            client.suspend(new JobIdentity("", 1));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }

        try {
            EnqueueResult newjob = addQueuedJob();
            client.suspend(newjob.getJobIdentity());
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of resume method, of class SoapWebService.
     */
    @Test
    public void testResume() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> resume(JobIdentity)");

        try {
            client.resume(null);
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }
        try {
            client.resume(new JobIdentity());
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }
        try {
            client.resume(new JobIdentity(null, 0));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }
        try {
            client.resume(new JobIdentity("", 1));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }

        try {
            EnqueueResult newjob = addQueuedJob();
            System.out.println(newjob);
            client.resume(newjob.getJobIdentity());
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of opendir method, of class SoapWebService.
     */
    @Test
    public void testOpendir() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> opendir()");

        try {
            List<JobIdentity> results = client.opendir();
            assertNotNull(results);
            System.out.println("(+) JobIdentity:");
            results.stream().map((result) -> {
                System.out.println("(i)   " + result.getJobID());
                return result;
            }).forEachOrdered((result) -> {
                System.out.println("(i)\tResult:\t" + result.getResult());
            });
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of readdir method, of class SoapWebService.
     */
    @Test
    public void testReaddir() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> readdir(JobIdentity)");

        try {
            assertEquals(false, client.readdir(null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }

        try {
            assertEquals(false, client.readdir(new JobIdentity()));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }

        // 
        // Test read list of files in from queued job:
        // 
        try {
            List<String> results = client.readdir(queued.getJobIdentity());
            assertNotNull(results);
            System.out.println("(+) Files:");

            results.stream().map((result) -> {
                assertNotNull(result);
                return result;
            }).forEachOrdered((result) -> {
                System.out.println("(i) " + result);
            });
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

    /**
     * Test of fopen method, of class SoapWebService.
     */
    @Test
    public void testFopen() {
        System.out.println("(i) *** BatchelorWebServiceClientTest -> fopen(JobIdentity, String)");

        // 
        // Test that expected exceptions are throwed:
        // 
        try {
            assertEquals(false, client.fopen(null, null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }

        try {
            assertEquals(false, client.fopen(null, ""));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }

        try {
            assertEquals(false, client.fopen(new JobIdentity(), null));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("(+) Catched excepted exception: " + e.getMessage());
        }

        try {
            assertEquals(false, client.fopen(new JobIdentity(), ""));
            fail("(-) Remote exception expected");
        } catch (RemoteException e) {
            System.out.println("(+) Catched expected exception: " + e.getMessage());
        }

        // 
        // Test get file content from enqueued job:
        // 
        try {
            byte[] result;
            String str;

            result = client.fopen(queued.getJobIdentity(), "queued");
            assertNotNull(result);
            str = new String(result);
            assertNotNull(str);
            assertTrue(str.length() > 0);
            System.out.println("(+) Result: " + str);
        } catch (RemoteException e) {
            fail("(!) Remote exception: " + e.getMessage());
        }
    }

}
