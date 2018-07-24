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
public class QueueSortResultTest {

    public QueueSortResultTest() {
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
     * Test of values method, of class QueueSortResult.
     */
    @Test
    public void testValues() {
        System.out.println("(i) *** QueueSortResultTest -> values()");
        QueueSortResult[] result = QueueSortResult.values();
        assertNotNull(result);
    }

    /**
     * Test of getValue method, of class QueueFilterResult.
     */
    @Test
    public void testGetValue() {
        System.out.println("(i) *** QueueSortResultTest -> getValue()");
        assertEquals("none", QueueSortResult.NONE.getValue());
        assertEquals("started", QueueSortResult.STARTED.getValue());
        assertEquals("jobid", QueueSortResult.JOB_ID.getValue());
        assertEquals("state", QueueSortResult.STATUS.getValue());
        assertEquals("state", QueueSortResult.STATE.getValue());
        assertEquals("name", QueueSortResult.NAME.getValue());
    }

    /**
     * Test of valueOf method, of class QueueSortResult.
     */
    @Test
    public void testValueOf() {
        System.out.println("(i) *** QueueSortResultTest -> valueOf(String)");
        assertEquals(QueueSortResult.JOB_ID, QueueSortResult.valueOf("JOB_ID"));
        assertEquals(QueueSortResult.NAME, QueueSortResult.valueOf("NAME"));
        assertEquals(QueueSortResult.NONE, QueueSortResult.valueOf("NONE"));
        assertEquals(QueueSortResult.STARTED, QueueSortResult.valueOf("STARTED"));
        assertEquals(QueueSortResult.STATE, QueueSortResult.valueOf("STATE"));
        assertEquals(QueueSortResult.STATUS, QueueSortResult.valueOf("STATUS"));
    }

    /**
     * Test aliases in class QueueFilterResult.
     */
    @Test
    public void testAlias() {
        System.out.println("(i) *** QueueSortResultTest -> testing aliases:");
        assertEquals(QueueSortResult.STATE.getValue(), QueueSortResult.STATUS.getValue());
    }

}
