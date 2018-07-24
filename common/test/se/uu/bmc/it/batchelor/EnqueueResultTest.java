/*
 * Java library for Batchelor (batch job queue)
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
package se.uu.bmc.it.batchelor;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class EnqueueResultTest {

    private final static String JOB_ID = "Job XYZ";
    private final static int RESULT = 1324354657;
    private final static String TIME = "09:15:30";
    private final static String DATE = "2009-04-03";
    private final static int STAMP = 1234567890;
    private EnqueueResult obj1;
    private EnqueueResult obj2;

    public EnqueueResultTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        obj1 = new EnqueueResult();
        obj2 = new EnqueueResult(JOB_ID, RESULT, DATE, TIME, STAMP);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJobID method, of class EnqueueResult.
     */
    @Test
    public void testGetJobID() {
        System.out.println("(i) *** EnqueueResultTest -> getJobID()");
        assertEquals(null, obj1.getJobID());
        assertEquals(JOB_ID, obj2.getJobID());
    }

    /**
     * Test of setJobID method, of class EnqueueResult.
     */
    @Test
    public void testSetJobID() {
        System.out.println("(i) *** EnqueueResultTest -> setJobID()");
        String jobID = "Job 999";
        obj1.setJobID(jobID);
        assertEquals(jobID, obj1.getJobID());
        obj1.setJobID(null);
        assertEquals(null, obj1.getJobID());
    }

    /**
     * Test of getJobID method, of class EnqueueResult.
     */
    @Test
    public void testGetResult() {
        System.out.println("(i) *** EnqueueResultTest -> getResult()");
        assertEquals(0, obj1.getResult());
        assertEquals(RESULT, obj2.getResult());
    }

    /**
     * Test of setJobID method, of class EnqueueResult.
     */
    @Test
    public void testSetResult() {
        System.out.println("(i) *** EnqueueResultTest -> setResult()");
        int result = 999666333;
        obj1.setResult(result);
        assertEquals(result, obj1.getResult());
        obj1.setResult(0);
        assertEquals(0, obj1.getResult());
    }

    /**
     * Test of setDate method, of class EnqueueResult.
     */
    @Test
    public void testSetDate() {
        System.out.println("(i) *** EnqueueResultTest -> setDate()");
        String date = "1998-04-28";
        obj1.setDate(date);
        assertEquals(date, obj1.getDate());
        obj1.setDate(null);
        assertEquals(null, obj1.getDate());
    }

    /**
     * Test of getDate method, of class EnqueueResult.
     */
    @Test
    public void testGetDate() {
        System.out.println("(i) *** EnqueueResultTest -> getDate()");
        assertEquals(null, obj1.getDate());
        assertEquals(DATE, obj2.getDate());
    }

    /**
     * Test of setTime method, of class EnqueueResult.
     */
    @Test
    public void testSetTime() {
        System.out.println("(i) *** EnqueueResultTest -> setTime()");
        String time = "12:34:56";
        obj1.setTime(time);
        assertEquals(time, obj1.getTime());
        obj1.setTime(null);
        assertEquals(null, obj1.getTime());
    }

    /**
     * Test of getTime method, of class EnqueueResult.
     */
    @Test
    public void testGetTime() {
        System.out.println("(i) *** EnqueueResultTest -> getTime()");
        assertEquals(null, obj1.getTime());
        assertEquals(TIME, obj2.getTime());
    }

    /**
     * Test of setStamp method, of class EnqueueResult.
     */
    @Test
    public void testSetStamp() {
        System.out.println("(i) *** EnqueueResultTest -> setStamp()");
        int stamp = 01010101;
        obj1.setStamp(stamp);
        assertEquals(stamp, obj1.getStamp());
        obj1.setStamp(0);
        assertEquals(0, obj1.getStamp());
    }

    /**
     * Test of getStamp method, of class EnqueueResult.
     */
    @Test
    public void testGetStamp() {
        System.out.println("(i) *** EnqueueResultTest -> getStamp()");
        assertEquals(0, obj1.getStamp());
        assertEquals(STAMP, obj2.getStamp());
    }

    @Test
    public void testGetJobIdentity() {
        System.out.println("(i) *** EnqueueResultTest -> getJobIdentity()");
        JobIdentity ident1 = new JobIdentity(JOB_ID, RESULT);
        JobIdentity ident2 = obj2.getJobIdentity();
        assertEquals(ident1.getJobID(), ident2.getJobID());
        assertEquals(ident1.getResult(), ident2.getResult());
    }
}
