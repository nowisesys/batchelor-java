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

 /*
 * Base64Decoder.java
 *
 * Created: Apr 27, 2009, 11:52:27 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.codecs.base64;

import java.io.InputStream;
import java.io.IOException;

/**
 * <p>
 * This class provides Base64 decoding of data. Basically, it has two modes of operations:
 * </p>
 *
 * <ol><li>Decoding of input streams.</li>
 * <li>Decoding of strings and byte/char arrays.</li>
 * </ol>
 *
 * <p>
 * Decoding an input stream is an iterative process where decode() gets called in a loop until null
 * is returned (EOF). Strings and byte/char arrays are normally decoded by a single step by calling
 * decode(), but chunked mode can be emulated for i.e. byte arrays by creating a
 * ByteArrayInputStream object and using it as stream argument to decode().
 * </p>
 *
 * <p>
 * The default number of bytes to decode is BUFFER_SIZE, but can be changed dynamic before or in
 * between calls to decode().
 * </p>
 *
 * <p>
 * This implementation is based on information from
 * <a href="http://en.wikipedia.org/wiki/Base64">http://en.wikipedia.org/wiki/Base64</a>
 * and verified against other Base64 implementations like the base64(1) command from the GNU
 * coreutils package.
 * </p>
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class Base64Decoder {

    private byte[] result;   // Output buffer.
    private byte[] buffer;   // Input buffer.
    private final static int[] valid = {0, 1, 1, 2, 4};  // valid bytes map.
    private final static byte[] lookup;                  // lookup table.
    private final static int[] mask = {0xff0000, 0x00ff00, 0x0000ff};
    /**
     * Default buffer size.
     */
    public final static int BUFFER_SIZE = 512;
    private int bufferSize;

    static {
        //
        // Initilize the ASCII to Base64 index lookup table:
        //
        lookup = new byte[255];
        for (int i = 0; i < 26; ++i) {
            lookup[i + 65] = (byte) (i);        // 'A' - 'Z'
        }
        for (int i = 0; i < 26; ++i) {
            lookup[i + 97] = (byte) (i + 26);   // 'a' - 'z'
        }
        for (int i = 0; i < 10; ++i) {
            lookup[i + 48] = (byte) (i + 52);   // '0' - '9'
        }
        lookup[43] = (byte) 62;                 // '+'
        lookup[47] = (byte) 63;                 // '/'
    }

    /**
     * Creates a decoder object suitable for decoding strings and byte or char arrays.
     */
    public Base64Decoder() {
        bufferSize = BUFFER_SIZE;
    }

    /**
     * Creates a decoder object suitable for decoding InputStreams.
     *
     * @param size The number of bytes to decode per decode call.
     */
    public Base64Decoder(int size) {
        bufferSize = size;
    }

    /**
     * Set the number of bytes to decode. Calling this function has no effect unless the input
     * source is a stream, for strings or char/byte arrays the size is instead determined from the
     * input.
     *
     * @param size The number of bytes to encode.
     */
    public void setSize(int size) {
        bufferSize = size;
    }

    /**
     * Get the number of bytes that will be decoded by next call of decode(). This function will
     * also return the number of bytes decoded from last call of decode().
     *
     * @return Number of bytes to decode.
     */
    public int getSize() {
        return bufferSize;
    }

    /**
     * Get the number of bytes required to hold the result of decoding size number of bytes from the
     * input source.
     *
     * @param size The number of bytes to decode.
     * @return The number of bytes required for the result buffer.
     */
    private int getResultSize(int size) {
        int aligned = (3 * (size / 4));
        if (buffer[size - 2] == '=') {
            --aligned;
        }
        if (buffer[size - 1] == '=') {
            --aligned;
        }
        return aligned;
    }

    /**
     * Creates the result buffer.
     *
     * @param size The number of bytes to decode.
     */
    private void prepare(int size) {
        bufferSize = size;
        int aligned = getResultSize(size);
        if (result == null || aligned != result.length) {
            result = new byte[aligned];
        }
    }

    /**
     * Decodes the whole string.
     *
     * @param string The string to encode.
     * @return The encoded result.
     */
    public byte[] decode(String string) {
        buffer = string.getBytes();
        prepare(buffer.length);
        return decode(true);
    }

    /**
     * Decode the input stream using current input buffer. Call setSize() prior to calling this
     * function to resize the input buffer. The number of bytes to decode is the same as returned by
     * getSize(), that is BUFFER_SIZE by default.
     *
     * @param stream The input stream.
     * @return The encoded result or null on EOF (end of file).
     * @throws java.io.IOException
     */
    public byte[] decode(InputStream stream) throws IOException {
        return decode(stream, bufferSize);
    }

    /**
     * Decode the input stream in block of size number of bytes. The actual number of bytes decoded
     * might be smaller if EOF is detected or larger if size is not an multiple of 4 (adjusted
     * internal).
     *
     * @param stream The input stream.
     * @param size The number of bytes to decode.
     * @return The decoded result or null on EOF (end of file).
     * @throws java.io.IOException
     */
    public byte[] decode(InputStream stream, int size) throws IOException {
        //
        // Make sure size is an multiple of 4.
        //
        while (size % 4 != 0) {
            ++size;
        }
        if (buffer == null) {
            buffer = new byte[size];
        } else if (buffer.length != size) {
            buffer = new byte[size];
        }

        while (size > 0) {
            int read = stream.read(buffer, buffer.length - size, size);
            if (read == -1) {
                break;
            }
            size -= read;
        }
        if (size == buffer.length) {
            return null;  // Nothing read.
        }
        if (size > 0) {
            bufferSize = buffer.length - size;
            for (int i = buffer.length - size; i < buffer.length; ++i) {
                buffer[i] = -1;
            }
        }
        prepare(buffer.length - size);
        return decode(false);
    }

    /**
     * Decode the byte array.
     *
     * @param input The byte array to decode.
     * @return The decoded result.
     */
    public byte[] decode(byte[] input) {
        buffer = input;
        prepare(buffer.length);
        return decode(true);
    }

    /**
     * Decode the char array.
     *
     * @param input The char array to decode.
     * @return The decoded result.
     */
    public byte[] decode(char[] input) {
        buffer = (new String(input)).getBytes();
        prepare(buffer.length);
        return decode(true);
    }

    /**
     * Decodes the current buffer content.
     *
     * @param single Denotes whether the buffer is internal allocated or not (uses external buffer
     * in single call mode).
     * @return The decoded result.
     */
    private byte[] decode(boolean single) {
        for (int i = 0; i < buffer.length / 4; ++i) {
            int ch = 0, len = 0;
            for (int j = 0; j < 4; ++j) {
                int index = 4 * i + j;
                int shift = 18 - j * 6;
                if (buffer[index] != -1 && buffer[index] != '=') {
                    ch |= lookup[buffer[index]] << shift;
                } else {
                    break;
                }
                ++len;
            }
            int split = valid[len];
            for (int j = 0; j < 3; ++j) {
                int index = 3 * i + j;
                int shift = 16 - j * 8;
                if (index >= result.length) {
                    break;
                }
                if (j < split) {
                    result[index] = (byte) ((ch & mask[j]) >> shift);
                }
            }
        }
        if (single) {
            buffer = null;
        }
        return result;
    }
}
