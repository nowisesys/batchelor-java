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
 * Unit test of the QueuedJob class.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class QueuedJobTest {

	private final static JobIdentity IDENT = new JobIdentity();
	private final static String STATE = "running";

    private QueuedJob obj1;     // Uses default contructor.
    private QueuedJob obj2;     // Uses contructor taking an JobIdentity.
    private QueuedJob obj3;     // Uses contructor taking all arguments.


    public QueuedJobTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        obj1 = new QueuedJob();
        obj2 = new QueuedJob(IDENT);
        obj3 = new QueuedJob(IDENT, STATE);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJobIdentty method, of class QueuedJob.
     */
    @Test
    public void testGetJobIdentity() {
        System.out.println("(i) *** QueuedJobTest -> getJobIdentity()");
        assertNotNull(obj1.getJobIdentity());
        assertEquals(IDENT, obj2.getJobIdentity());
        assertEquals(IDENT, obj3.getJobIdentity());
    }

    /**
     * Test of setState method, of class QueuedJob.
     */
    @Test
    public void testSetState() {
        System.out.println("(i) *** QueuedJobTest -> setState()");
        String state = "queued";
        obj1.setState(state);
        assertEquals(state, obj1.getState());
        obj1.setState(null);
        assertEquals(null, obj1.getState());
    }

    /**
     * Test of getState method, of class QueuedJob.
     */
    @Test
    public void testGetState() {
        System.out.println("(i) *** QueuedJobTest -> getState()");
        assertEquals(null, obj1.getState());
        assertEquals(null, obj2.getState());
        assertEquals(STATE, obj3.getState());
    }

}
