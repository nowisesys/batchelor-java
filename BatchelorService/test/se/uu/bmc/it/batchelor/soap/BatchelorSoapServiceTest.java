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

import java.rmi.RemoteException;

import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.QueueFilterResult;
import se.uu.bmc.it.batchelor.QueueSortResult;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapServiceTest {

    private BatchelorSoapService service;

    public BatchelorSoapServiceTest() {
        service = new BatchelorSoapService();
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
     * Test of version method, of class BatchelorSoapService.
     */
    @Test
    public void testVersion() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> version()");
        assertNotNull(service.version());
        assertEquals("1.0", service.version());
    }

    /**
     * Test of enqueue method, of class BatchelorSoapService.
     */
    @Test
    public void testEnqueue() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> enqueue(String)");
        try {
            assertNull(service.enqueue(null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
        assertNotNull(service.enqueue(""));
    }

    /**
     * Test of dequeue method, of class BatchelorSoapService.
     */
    @Test
    public void testDequeue() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> dequeue(JobIdentity)");
        try {
            assertEquals(false, service.dequeue(null));
        } catch (RemoteException e) {
        }
        try {
            assertEquals(false, service.dequeue(new JobIdentity()));
        } catch (RemoteException e) {
        }
    }

    /**
     * Test of queue method, of class BatchelorSoapService.
     */
    @Test
    public void testQueue() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> queue(QueueSortResult, QueueFilterResult)");
        assertNotNull(service.queue(QueueSortResult.NONE, QueueFilterResult.ALL));
        assertNotNull(service.queue(QueueSortResult.NONE, null));
        assertNotNull(service.queue(null, QueueFilterResult.ALL));
        assertNotNull(service.queue(null, null));
    }

    /**
     * Test of watch method, of class BatchelorSoapService.
     */
    @Test
    public void testWatch() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> watch(int)");
        assertNotNull(service.watch(0));
        assertNotNull(service.watch(1234567890));
        assertNotNull(service.watch(-1));
    }

    /**
     * Test of suspend method, of class BatchelorSoapService.
     */
    @Test
    public void testSuspend() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> suspend(JobIdentity)");
        assertEquals(false, service.suspend(new JobIdentity()));
        try {
            assertEquals(false, service.suspend(null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
    }

    /**
     * Test of resume method, of class BatchelorSoapService.
     */
    @Test
    public void testResume() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> resume(JobIdentity)");
        assertEquals(false, service.resume(new JobIdentity()));
        try {
            assertEquals(false, service.resume(null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
    }

    /**
     * Test of stat method, of class BatchelorSoapService.
     */
    @Test
    public void testStat() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> stat(JobIdentity)");
        assertNull(service.stat(new JobIdentity()));
        try {
            assertNull(service.stat(null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
    }

    /**
     * Test of opendir method, of class BatchelorSoapService.
     */
    @Test
    public void testOpendir() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> opendir()");
        assertNotNull(service.opendir());
    }

    /**
     * Test of readdir method, of class BatchelorSoapService.
     */
    @Test
    public void testReaddir() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> readdir(JobIdentity)");
        assertNotNull(service.readdir(new JobIdentity()));
        try {
            assertNull(service.readdir(null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
    }

    /**
     * Test of fopen method, of class BatchelorSoapService.
     */
    @Test
    public void testFopen() throws Exception {
        System.out.println("(i) *** BatchelorSoapServiceTest -> fopen(JobIdentity, String)");
        assertNull(service.fopen(new JobIdentity(), ""));
        try {
            assertNull(service.fopen(new JobIdentity(), null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
        try {
            assertNull(service.fopen(null, ""));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
        try {
            assertNull(service.fopen(null, null));
            fail("(-) Expected remote exception");
        } catch (RemoteException e) {
        }
    }
}
