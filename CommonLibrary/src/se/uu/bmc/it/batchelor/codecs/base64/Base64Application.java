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
 * Base64Application.java
 *
 * Created: May 3, 2009, 11:07:14 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.codecs.base64;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Sample main class that utilize the Base64Encode/Base64Decode classes to
 * provide an Base 64 encoding/decoding application.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class Base64Application {

    enum OptionArg {

        NONE, INPUT, OUTPUT, STRING
    }

    class OptionException extends Exception {

        OptionException(String msg) {
            super(msg);
        }
    }

    class Options {

        private String input;
        private String output;
        private String string;
        private OptionArg optarg = OptionArg.NONE;
        private boolean decode;
        private boolean encode;
        private String[] args;

        Options(String[] args) {
            this.args = args;
        }

        public boolean decode() {
            return decode;
        }

        public InputStream getInputStream() throws FileNotFoundException {
            if (input != null) {
                return new FileInputStream(input);
            } else {
                return new ByteArrayInputStream(string.getBytes());
            }
        }

        public OutputStream getOutputStream() throws FileNotFoundException {
            if (output != null) {
                return new FileOutputStream(output);
            } else {
                return System.out;
            }
        }

        public boolean parseOptions() throws OptionException {
            for (String arg : args) {
                if (arg.compareTo("-h") == 0 ||
                    arg.compareTo("--help") == 0) {
                    return false;
                } else if (arg.compareTo("-d") == 0 ||
                    arg.compareTo("--decode") == 0) {
                    decode = true;
                } else if (arg.compareTo("-e") == 0 ||
                    arg.compareTo("--encode") == 0) {
                    encode = true;
                } else if (arg.compareTo("-i") == 0) {
                    optarg = OptionArg.INPUT;
                } else if (arg.compareTo("-o") == 0) {
                    optarg = OptionArg.OUTPUT;
                } else if (arg.compareTo("-s") == 0) {
                    optarg = OptionArg.STRING;
                } else if (optarg != OptionArg.NONE) {
                    switch (optarg) {
                        case INPUT:
                            input = arg;
                            break;
                        case OUTPUT:
                            output = arg;
                            break;
                        case STRING:
                            string = arg;
                            break;
                    }
                    optarg = OptionArg.NONE;
                } else {
                    throw new OptionException("Unknown option '" + arg);
                }
            }
            return true;
        }

        public void checkOptions() throws OptionException {
            if (!decode && !encode) {
                throw new OptionException("Neither decode (-d) nor encode (-e) mode selected");
            }
            if (decode && encode) {
                throw new OptionException("Both encode (-e) and decode (-d) mode can't be active at same time");
            }
            if (input != null && string != null) {
                throw new OptionException("Both input file (-i) and string (-s) can't be used at same time");
            }
            if (input == null && string == null) {
                throw new OptionException("Neither input file (-i) or string (-s) specified");
            }
        }

        public void showUsage() {
            System.out.println("Base64Application - Encode/decode Base64 strings");
            System.out.println("Usage: Base64Application [-d|-e] [-i file|-s string] [-o file]");
            System.out.println("Options:");
            System.out.println("  -d,--decode: Decode input");
            System.out.println("  -e,--encode: Encode input");
            System.out.println("  -i file:     Read input from file");
            System.out.println("  -o file:     Write output to file");
            System.out.println("  -s string:   Encode/decode string");
            System.out.println("  -h,--help:   Show this help");
            System.out.println("Copyright (C) 2009 Anders Lövgren");
        }
    }

    Base64Application(String[] args) {
        Options options = new Options(args);
        try {
            if (!options.parseOptions()) {
                options.showUsage();
                System.exit(0);
            }
            options.checkOptions();
        } catch (OptionException e) {
            System.err.print(e);
        }

        try {
            InputStream input = options.getInputStream();
            OutputStream output = options.getOutputStream();

            if (options.decode()) {
                Base64Decoder decoder = new Base64Decoder();
                try {
                    byte[] result = decoder.decode(input);
                    while (result != null) {
                        output.write(result);
                        result = decoder.decode(input);
                    }
                } catch (IOException e) {
                    System.err.print(e);
                }
            } else {
                Base64Encoder encoder = new Base64Encoder();
                try {
                    byte[] result = encoder.encode(input);
                    while (result != null) {
                        output.write(result);
                        result = encoder.encode(input);
                    }
                } catch (IOException e) {
                    System.err.print(e);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.print(e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Base64Application app = new Base64Application(args);
    }
}
