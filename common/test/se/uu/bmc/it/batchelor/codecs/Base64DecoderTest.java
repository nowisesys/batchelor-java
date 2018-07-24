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
package se.uu.bmc.it.batchelor.codecs;

import se.uu.bmc.it.batchelor.codecs.base64.Base64Decoder;
import java.io.ByteArrayInputStream;

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
public class Base64DecoderTest {

    Base64Decoder decoder;
    final static String decoded = "Hello, world!";
    final static String encoded = "SGVsbG8sIHdvcmxkIQ==";

    public Base64DecoderTest() {
        decoder = new Base64Decoder();
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
     * Test constructors.
     */
    @Test
    public void testEncode_Constructors() {
        System.out.println("** Base64DecoderTest -> Contructors");
        Base64Decoder obj = new Base64Decoder();
        assertEquals(Base64Decoder.BUFFER_SIZE, obj.getSize());
        obj = new Base64Decoder(3600);
        assertEquals(3600, obj.getSize());
    }

    /**
     * Test of setSize method, of class Base64Decoder.
     */
    @Test
    public void testSetSize() {
        System.out.println("** Base64DecoderTest -> setSize(int)");
        decoder.setSize(3600);
        assertEquals(3600, decoder.getSize());
    }

    /**
     * Test of getSize method, of class Base64Decoder.
     */
    @Test
    public void testGetSize() {
        System.out.println("** Base64DecoderTest -> getSize()");
        assertEquals(decoder.getSize(), Base64Decoder.BUFFER_SIZE);
    }

    /**
     * Test of decode method, of class Base64Decoder.
     */
    @Test
    public void testDecode_String() {
        System.out.println("** Base64DecoderTest -> decode(String)");
        byte[] bytes = decoder.decode(encoded);
        String result = new String(bytes);
        System.out.println("Result: '" + result + "'");
        assertEquals(result, decoded);
    }

    /**
     * Test of decode method, of class Base64Decoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testDecode_InputStream() throws Exception {
        System.out.println("** Base64DecoderTest -> decode(InputStream)");
        ByteArrayInputStream stream = new ByteArrayInputStream(encoded.getBytes());
        byte[] bytes = decoder.decode(stream);
        String result = new String(bytes);
        System.out.println("Result: '" + result + "'");
        assertEquals(result, decoded);
    }

    /**
     * Test of decode method, of class Base64Decoder.
     * @throws java.lang.Exception
     */
    @Test
    public void testDecode_InputStream_int() throws Exception {
        System.out.println("** Base64DecoderTest -> decode(InputStream, int)");
        ByteArrayInputStream stream = new ByteArrayInputStream(encoded.getBytes());
        byte[] bytes = decoder.decode(stream, 512);
        String result = new String(bytes);
        System.out.println("Result: '" + result + "'");
        assertEquals(result, decoded);
    }

    /**
     * Test of decode method, of class Base64Decoder.
     */
    @Test
    public void testDecode_byteArr() {
        System.out.println("** Base64DecoderTest -> decode(byte[])");
        byte[] bytes = decoder.decode(encoded.getBytes());
        String result = new String(bytes);
        System.out.println("Result: '" + result + "'");
        assertEquals(result, decoded);
    }

    /**
     * Test of decode method, of class Base64Decoder.
     */
    @Test
    public void testDecode_charArr() {
        System.out.println("** Base64DecoderTest -> decode(char[])");
        byte[] bytes = decoder.decode(encoded.toCharArray());
        String result = new String(bytes);
        System.out.println("Result: '" + result + "'");
        assertEquals(result, decoded);
    }
}
