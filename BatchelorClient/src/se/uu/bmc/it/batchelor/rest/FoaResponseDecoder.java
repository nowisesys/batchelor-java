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
 * FoaResponseDecoder.java
 *
 * Created: Apr 3, 2009, 10:37:16 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import java.net.ContentHandler;
import java.net.URLConnection;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import se.uu.bmc.it.foa.StreamDecoder;
import se.uu.bmc.it.foa.Entity;
import se.uu.bmc.it.foa.DecoderException;

import se.uu.bmc.it.batchelor.rest.schema.Error;
import se.uu.bmc.it.batchelor.rest.schema.File;
import se.uu.bmc.it.batchelor.rest.schema.Job;
import se.uu.bmc.it.batchelor.rest.schema.Link;
import se.uu.bmc.it.batchelor.rest.schema.ObjectFactory;
import se.uu.bmc.it.batchelor.rest.schema.Result;

/**
 * This class extends the ContentHandler class to decoding FOA encoded response
 * messages from the Batchelor REST service.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 * @see ResponseDecoder
 * @see <a href="http://it.bmc.uu.se/andlov/proj/libfoa">FOA</a>
 */
public class FoaResponseDecoder extends ContentHandler {

    /**
     * This function checks that the entity meets the expected format or an
     * DecoderException is thrown.
     * @param entity The entity to check.
     * @param name The expected name.
     * @param type The expected type.
     * @throws se.uu.bmc.it.foa.DecoderException
     */
    private static void expect(Entity entity, String name, Entity.Type type) throws DecoderException {
        if (!entity.hasName() || entity.getName().compareTo(name) != 0) {
            throw new DecoderException("Expected name '" + name + "' (got '" + entity.getName() + "') at line " + entity.getLine());
        }
        if (entity.getType() != type) {
            throw new DecoderException("Expected type '" + type + "' (got '" + entity.getType() + "') at line " + entity.getLine());
        }
    }

    /**
     * This function checks that the entity meets the expected format or an
     * DecoderException is thrown.
     * @param entity The entity to check.
     * @param type The expected type.
     * @throws se.uu.bmc.it.foa.DecoderException
     */
    private static void expect(Entity entity, Entity.Type type) throws DecoderException {
        if (entity.getType() != type) {
            throw new DecoderException("Expected type '" + type + "' (got '" + entity.getType() + "') at line " + entity.getLine());
        }
    }

    /**
     * Decode the FOA message header and validate the input.
     * @param result The result object.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private void decodeHeader(Result result) throws DecoderException, IOException {
        entity = decoder.read();
        expect(entity, "result", Entity.Type.StartObject);
        entity = decoder.read();
        expect(entity, "state", Entity.Type.DataName);
        result.setState(entity.getData());
        entity = decoder.read();
        expect(entity, "type", Entity.Type.DataName);
        result.setType(entity.getData());
    }

    /**
     * Decode the error message from the input stream.
     * @return The decoded error object.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private Error decodeError() throws DecoderException, IOException {
        Error error = factory.createError();

        entity = decoder.read();
        expect(entity, "error", Entity.Type.StartObject);

        entity = decoder.read();
        expect(entity, "code", Entity.Type.DataName);
        error.setCode(Integer.parseInt(entity.getData()));
        entity = decoder.read();
        expect(entity, "message", Entity.Type.DataName);
        error.setMessage(entity.getData());
        error.setOrigin("remote");

        entity = decoder.read();
        expect(entity, Entity.Type.EndObject);

        return error;
    }

    /**
     * Decode the version message from the input stream.
     * @return The remote version string.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private String decodeVersion() throws DecoderException, IOException {
        entity = decoder.read();
        expect(entity, "version", Entity.Type.DataName);
        return entity.getData();
    }

    /**
     * Decode the status message from the input stream.
     * @return The remote version string.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private String decodeStatus() throws DecoderException, IOException {
        entity = decoder.read();
        expect(entity, "status", Entity.Type.DataName);
        return entity.getData();
    }

    /**
     * Decode a single link object from the input stream.
     * @return The decoded link.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private Link decodeLink() throws DecoderException, IOException {
        Link link = factory.createLink();

        entity = decoder.read();
        if (entity.getType() == Entity.Type.EndArray) {
            return null;    // End of array of links
        }
        expect(entity, "link", Entity.Type.StartObject);
        entity = decoder.read();
        expect(entity, "href", Entity.Type.DataName);
        link.setHref(entity.getData());

        while ((entity = decoder.read()) != null) {
            if (entity.getType() == Entity.Type.EndObject) {
                break;
            }
            expect(entity, Entity.Type.DataName);

            if (entity.getName().compareTo("get") == 0) {
                link.setGet(entity.getData());
            } else if (entity.getName().compareTo("post") == 0) {
                link.setPost(entity.getData());
            } else if (entity.getName().compareTo("put") == 0) {
                link.setPut(entity.getData());
            } else if (entity.getName().compareTo("delete") == 0) {
                link.setDelete(entity.getData());
            }
        }

        return link;
    }

    /**
     * Decodes an link message containing an array of link objects.
     * @param result The result object.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private void decodeLinkArray(Result result) throws DecoderException, IOException {
        entity = decoder.read();
        expect(entity, "data", Entity.Type.StartArray);

        for (Link link = decodeLink(); link != null; link = decodeLink()) {
            result.getLink().add(link);
        }
        expect(entity, Entity.Type.EndArray);
    }

    /**
     * Decodes a single job object from the input stream.
     * @return The decoded job.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private Job decodeJob() throws DecoderException, IOException {
        Job job = factory.createJob();

        entity = decoder.read();
        if (entity.getType() == Entity.Type.EndArray) {
            return null;    // End of array of jobs
        }
        expect(entity, "job", Entity.Type.StartObject);

        entity = decoder.read();
        expect(entity, "timezone", Entity.Type.DataName);
        job.setTimezone(entity.getData());
        entity = decoder.read();
        expect(entity, "data", Entity.Type.StartObject);

        while ((entity = decoder.read()) != null) {
            if (entity.getType() == Entity.Type.EndObject) {
                break;
            }
            expect(entity, Entity.Type.DataName);

            if (entity.getName().compareTo("jobid") == 0) {
                job.setJobid(entity.getData());
            } else if (entity.getName().compareTo("state") == 0) {
                job.setState(entity.getData());
            } else if (entity.getName().compareTo("stdout") == 0) {
                job.setStdout(Integer.parseInt(entity.getData()));
            } else if (entity.getName().compareTo("started") == 0) {
                job.setStarted(Integer.parseInt(entity.getData()));
            } else if (entity.getName().compareTo("queued") == 0) {
                job.setQueued(Integer.parseInt(entity.getData()));
            } else if (entity.getName().compareTo("result") == 0) {
                job.setResult(Integer.parseInt(entity.getData()));
            }
        }
        entity = decoder.read();
        expect(entity, Entity.Type.EndObject);

        return job;
    }

    /**
     * Decodes an job message containing an array of job objects.
     * @param result The result object.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private void decodeJobArray(Result result) throws DecoderException, IOException {
        entity = decoder.read();
        expect(entity, "data", Entity.Type.StartArray);

        for (Job job = decodeJob(); job != null; job = decodeJob()) {
            result.getJob().add(job);
        }
        expect(entity, Entity.Type.EndArray);
    }

    /**
     * Decode an file object. This function reads the file header, leaving the
     * input stream at the position where the caller can start read the actual
     * file content. The number of bytes to read can be obtained from
     * File.getSize()
     * @param result The result object.
     * @throws se.uu.bmc.it.foa.DecoderException
     * @throws java.io.IOException
     */
    private void decodeFile(Result result) throws DecoderException, IOException {
        File file = factory.createFile();

        entity = decoder.read();
        expect(entity, "@file", Entity.Type.StartObject);
        entity = decoder.read();
        expect(entity, "name", Entity.Type.DataName);
        file.setName(entity.getData());
        entity = decoder.read();
        expect(entity, "size", Entity.Type.DataName);
        file.setSize(Long.parseLong(entity.getData()));
        entity = decoder.read();
        expect(entity, "encoding", Entity.Type.DataName);
        file.setEncoding(entity.getData());

        // Set stream for reading file content:
        file.setInputStream(decoder.getStream());

        result.setFile(file);
    }

    /**
     * Get next FOA encoded object from input stream (contained in the URL
     * connection).
     * @param connection The URL connection to read from.
     * @return The next object from decoded FOA encoded response message.
     */
    public Object getContent(URLConnection connection) {
        try {
            Reader reader = new InputStreamReader(connection.getInputStream());
            decoder = new StreamDecoder(reader);
            factory = new ObjectFactory();
            Result result = factory.createResult();

            try {
                // Read message header. First line should always be start of
                // an result object, followed by the state and type. Once we
                // got these properties, we can decode the contained object.
                decodeHeader(result);
                if (result.getState().compareTo("failed") == 0) {
                    result.setError(decodeError());
                    return result;
                }
                if (result.getType().compareTo("version") == 0) {
                    result.setVersion(decodeVersion());
                } else if (result.getType().compareTo("status") == 0) {
                    result.setStatus(decodeStatus());
                } else if (result.getType().compareTo("link") == 0) {
                    decodeLinkArray(result);
                } else if (result.getType().compareTo("job") == 0) {
                    decodeJobArray(result);
                } else if (result.getType().compareTo("file") == 0) {
                    decodeFile(result);
                }
                expect(decoder.read(), Entity.Type.EndObject);
                return result;

            } catch (DecoderException e) {
                return getError("Decode error: " + e.getMessage());
            } finally {
                if (result.getFile() == null) {
                    reader.close();
                }
            }

        } catch (IOException e) {
            return getError("I/O error: " + e.getMessage());
        }
    }

    /**
     * Return a result object wrapping an error object. This function extends
     * the result error type with local errors.
     * @param message The error message.
     * @return The result object representing the error.
     */
    private Result getError(String message) {
        Result result = factory.createResult();
        Error error = factory.createError();

        error.setCode(0);
        error.setMessage(message);
        error.setOrigin("local");

        result.setError(error);
        return result;
    }
    private StreamDecoder decoder;
    private ObjectFactory factory;
    private Entity entity;
}
