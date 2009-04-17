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
public class QueueFilterResultTest {

    public QueueFilterResultTest() {
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
     * Test of values method, of class QueueFilterResult.
     */
    @Test
    public void testValues() {
        System.out.println("(i) *** QueueFilterResultTest -> values()");
        QueueFilterResult[] result = QueueFilterResult.values();
        assertNotNull(result);
    }

    /**
     * Test of getValue method, of class QueueFilterResult.
     */
    @Test
    public void testGetValue() {
        System.out.println("(i) *** QueueFilterResultTest -> getValue()");
        assertEquals("all", QueueFilterResult.ALL.getValue());
        assertEquals("all", QueueFilterResult.NONE.getValue());
        assertEquals("pending", QueueFilterResult.PENDING.getValue());
        assertEquals("running", QueueFilterResult.RUNNING.getValue());
        assertEquals("waiting", QueueFilterResult.UNFINISHED.getValue());
        assertEquals("waiting", QueueFilterResult.WAITING.getValue());
        assertEquals("finished", QueueFilterResult.SUCCESS.getValue());
        assertEquals("finished", QueueFilterResult.FINISHED.getValue());
        assertEquals("warning", QueueFilterResult.WARNING.getValue());
        assertEquals("error", QueueFilterResult.ERROR.getValue());
        assertEquals("crashed", QueueFilterResult.CRASHED.getValue());
    }

    /**
     * Test of valueOf method, of class QueueFilterResult.
     */
    @Test
    public void testValueOf() {
        System.out.println("(i) *** QueueFilterResultTest -> valueOf(String)");
        assertEquals(QueueFilterResult.ALL, QueueFilterResult.valueOf("ALL"));
        assertEquals(QueueFilterResult.NONE, QueueFilterResult.valueOf("NONE"));
        assertEquals(QueueFilterResult.PENDING, QueueFilterResult.valueOf("PENDING"));
        assertEquals(QueueFilterResult.RUNNING, QueueFilterResult.valueOf("RUNNING"));
        assertEquals(QueueFilterResult.UNFINISHED, QueueFilterResult.valueOf("UNFINISHED"));
        assertEquals(QueueFilterResult.SUCCESS, QueueFilterResult.valueOf("SUCCESS"));
        assertEquals(QueueFilterResult.FINISHED, QueueFilterResult.valueOf("FINISHED"));
        assertEquals(QueueFilterResult.WARNING, QueueFilterResult.valueOf("WARNING"));
        assertEquals(QueueFilterResult.ERROR, QueueFilterResult.valueOf("ERROR"));
        assertEquals(QueueFilterResult.CRASHED, QueueFilterResult.valueOf("CRASHED"));
    }

    /**
     * Test aliases in class QueueFilterResult.
     */
    @Test
    public void testAlias() {
        System.out.println("(i) *** QueueFilterResultTest -> testing aliases:");
        assertEquals(QueueFilterResult.ALL.getValue(), QueueFilterResult.NONE.getValue());
        assertEquals(QueueFilterResult.WAITING.getValue(), QueueFilterResult.UNFINISHED.getValue());
        assertEquals(QueueFilterResult.SUCCESS.getValue(), QueueFilterResult.FINISHED.getValue());
    }
}