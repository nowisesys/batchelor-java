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

/*
 * Base64Encoder.java
 *
 * Created: Apr 27, 2009, 11:51:12 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.codecs.base64;

import java.io.InputStream;
import java.io.IOException;

/**
 * <p>This class provides Base64 encoding of data. Basically, it has two modes
 * of operations:</p>
 *
 * <ol><li>Encoding of input streams.</li>
 *     <li>Encoding of strings and byte/char arrays.</li>
 * </ol>
 *
 * <p>Encoding an input stream is an iterative process where encode() gets called in
 * a loop until null is returned (EOF). Strings and byte/char arrays are normally
 * encoded by a single call to encode(), but chunked mode can be emulated 
 * i.e. for byte arrays by creating an ByteArrayInputStream object and calling
 * encode(InputStream), using the byte array input stream.</p>
 *
 * <p>The default number of bytes to encode is BUFFER_SIZE, but can be changed
 * dynamic before or in between calls to encode().</p>
 * 
 * <p>This implementation is based on information from <a href="http://en.wikipedia.org/wiki/Base64">http://en.wikipedia.org/wiki/Base64</a>
 * and verified against other Base64 implementations like the base64(1) command
 * from the GNU coreutils package.</p>
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public final class Base64Encoder {

    private byte[] result;   // Output buffer.
    private byte[] buffer;   // Input buffer.
    private final static int[] valid = {0, 2, 3, 4, 4};  // valid bytes map.
    private final static byte[] lookup = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();
    private final static int[] mask = {0xfc0000, 0x03f000, 0x000fc0, 0x00003f};
    /**
     * Default buffer size.
     */
    public final static int BUFFER_SIZE = 512;
    private int bufferSize;

    /**
     * Creates a encoder object suitable for encoding strings and char or byte
     * arrays.
     */
    public Base64Encoder() {
        bufferSize = BUFFER_SIZE;
    }

    /**
     * Creates a encoder object suitable for encoding InputStreams. The size
     * argument is the number of bytes to encode from the input stream in each
     * call to encode.
     * @param size The number of bytes to encode.
     */
    public Base64Encoder(int size) {
        bufferSize = size;
    }

    /**
     * Set the number of bytes to encode. Calling this function has no effect
     * unless the input source is a stream, for strings or char/byte arrays
     * the size is instead determined from the input.
     * @param size The number of bytes to encode.
     */
    public void setSize(int size) {
        bufferSize = size;
    }

    /**
     * Get the number of bytes that will be encoded by next call of encode().
     * This function will also return the number of bytes encoded from last
     * call of encode().
     * @return Number of bytes to encode.
     */
    public int getSize() {
        return bufferSize;
    }

    /**
     * Get the number of bytes required to hold the result of encoding size
     * number of bytes from the input source.
     * @param size The number of bytes to encode.
     * @return The aligned number of bytes.
     */
    private int getResultSize(int size) {
        int aligned = (4 * (size / 3));
        if (size % 3 != 0) {
            aligned += 4;
        }
        return aligned;
    }

    /**
     * Creates an suitable sized result buffer for encoding the size number
     * of bytes.
     * @param size The number of input bytes.
     */
    private void prepare(int size) {
        bufferSize = size;
        int aligned = getResultSize(size);
        if (result == null || aligned != result.length) {
            result = new byte[aligned];
        }
    }

    /**
     * Encodes the whole string.
     * @param string The string to encode.
     * @return The encoded result.
     */
    public byte[] encode(String string) {
        buffer = string.getBytes();
        prepare(buffer.length);
        return encode(true);
    }

    /**
     * Encode the input stream using current input buffer. Call setSize() prior
     * to calling this function to resize the input buffer. The number of bytes
     * to decode is the same as returned by getSize(), that is BUFFER_SIZE by
     * default.
     * @param stream The input stream.
     * @return The encoded result or null on EOF (end of file).
     * @throws java.io.IOException
     */
    public byte[] encode(InputStream stream) throws IOException {
        return encode(stream, bufferSize);
    }

    /**
     * Encode the input stream in block of size number of bytes. The actual
     * number of bytes encoded might be smaller if EOF is detected or larger
     * if size is not an multiple of 3 (adjusted internal).
     *
     * @param stream The input stream.
     * @param size The number of bytes to encode.
     * @return The encoded result or null on EOF (end of file).
     * @throws java.io.IOException
     */
    public byte[] encode(InputStream stream, int size) throws IOException {
        //
        // Make sure size is an multiple of 3 to prevent trailing '=' unless
        // EOF is detected on input stream. 
        //
        while(size % 3 != 0) {
            ++size;
        }
        if (buffer == null) {
            buffer = new byte[size];
        } else if (buffer.length != size) {
            buffer = new byte[size];
        }

        int read = 0;
        while (size > 0) {
            read = stream.read(buffer, buffer.length - size, size);
            if (read == -1) {
                break;
            }
            size -= read;
        }
        if (size == buffer.length) {
            return null;  // Nothing read.
        }
        if (size > 0) {
            for (int i = buffer.length - size; i < buffer.length; ++i) {
                buffer[i] = -1;
            }
        }
        prepare(buffer.length - size);
        return encode(false);
    }

    /**
     * Encode the byte array.
     * @param input The byte array to encode.
     * @return The encoded result.
     */
    public byte[] encode(byte[] input) {
        buffer = input;
        prepare(buffer.length);
        return encode(true);
    }

    /**
     * Encode the char array.
     * @param input The char array to encode.
     * @return The encoded result.
     */
    public byte[] encode(char[] input) {
        buffer = (new String(input)).getBytes();
        prepare(buffer.length);
        return encode(true);
    }

    /**
     * Encodes the current buffer content.
     * @param single Denotes whether the buffer is internal allocated or not (uses external buffer in single call mode).
     * @return The encoded result.
     */
    private byte[] encode(boolean single) {
        for (int i = 0; i < result.length / 4; ++i) {
            int ch = 0, len = 0;
            for (int j = 0; j < 3; ++j) {
                int index = i * 3 + j;
                int shift = 16 - 8 * j;
                if (index < buffer.length && buffer[index] != -1) {
                    ch |= buffer[i * 3 + j] << shift;
                } else {
                    break;
                }
                ++len;
            }
            int split = valid[len];
            for (int j = 0; j < 4; ++j) {
                int index = i * 4 + j;
                int shift = 18 - j * 6;
                if (j < split) {
                    result[index] = lookup[(ch & mask[j]) >> shift];
                } else {
                    result[index] = '=';
                }
            }
        }
        if (single) {
            buffer = null;
        }
        return result;
    }
}
