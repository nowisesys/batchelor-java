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
public class JobIdentityTest {

    private final static String JOB_ID = "Job XYZ";
    private final static String RESULT = "/var/cache/jobs/xyz/123";
    private JobIdentity obj1;   // Uses default contructor.
    private JobIdentity obj2;   // Uses constructor taking arguments.

    public JobIdentityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        obj1 = new JobIdentity();
        obj2 = new JobIdentity(JOB_ID, RESULT);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJobId method, of class JobIdentity.
     */
    @Test
    public void testGetJobID() {
        System.out.println("(i) JobIdentityTest -> getJobId()");
        assertEquals(null, obj1.getJobID());
        assertEquals(JOB_ID, obj2.getJobID());
    }

    /**
     * Test of getResult method, of class JobIdentity.
     */
    @Test
    public void testGetResult() {
        System.out.println("(i) JobIdentityTest -> getResult()");
        assertEquals(null, obj1.getResult());
        assertEquals(RESULT, obj2.getResult());
    }

    @Test
    public void testSetJobID() {
        System.out.println("(i) JobIdentityTest -> setJobID(String)");
        obj1.setJobID(JOB_ID);
        assertEquals(JOB_ID, obj1.getJobID());
    }

    @Test
    public void testSetResult() {
        System.out.println("(i) JobIdentityTest -> setResult(String)");
        obj1.setResult(RESULT);
        assertEquals(RESULT, obj1.getResult());
    }
}
