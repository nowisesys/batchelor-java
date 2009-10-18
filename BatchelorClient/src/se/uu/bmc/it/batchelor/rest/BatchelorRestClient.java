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
 * BatchelorRestClient.java
 *
 * Created: Apr 2, 2009, 11:25:51 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

import se.uu.bmc.it.batchelor.WebServiceInterface;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.QueueFilterResult;
import se.uu.bmc.it.batchelor.QueueSortResult;

import se.uu.bmc.it.batchelor.rest.schema.Result;
import se.uu.bmc.it.batchelor.rest.schema.Job;
import se.uu.bmc.it.batchelor.rest.schema.Link;
import se.uu.bmc.it.batchelor.rest.convert.JobObjectConverter;
import se.uu.bmc.it.batchelor.rest.convert.LinkObjectConverter;

import se.uu.bmc.it.batchelor.codecs.base64.Base64Decoder;

import java.net.URL;
import java.net.Proxy;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.rmi.RemoteException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>This class implements the WebServiceInterface providing an RESTful client
 * for interaction with the Batchelor REST web service on a remote system. The 
 * REST URI space and its associated HTTP actions (GET, PUT, POST and DELETE) 
 * is completely abstacted away within this class.</p>
 *
 * <p>Things to keep in mind using this class is that content handling must
 * be initialized before its first use and only once. Note that the content
 * handler is global for the whole application and its entire lifetime. The
 * following code demonstrates how to create a REST service client, complete
 * with installing content handling using the ContentHandlerFactory obtained
 * from ReponseDecoderFactory:</p>
 *
 * <p><pre><code>
 * import java.rmi.RemoteException;
 * import java.net.URL;
 * import se.uu.bmc.it.batchelor.*;
 * import se.uu.bmc.it.batchelor.rest.*;
 *
 * class RestServiceClient {
 *
 *     static {
 *         //
 *         // Installs content handlers for the text/xml and text/x-foa MIME types:
 *         //
 *         ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
 *         HttpURLConnection.setContentHandlerFactory(factory);
 *     }
 *
 *     private BatchelorRestClient client;
 *
 *     public RestServiceClient(URL url) {
 *         client = new RestWebService(url, ResponseEncoder.FOA);
 *     }
 *
 *     // ... methods calling client.XXX()
 *
 * }
 * </code></pre></p>
 *
 * <p>See ReponseDecoderFactory for an example of how to handle the case
 * where you already have an content handler installed.</p>
 *
 * @see se.uu.bmc.it.batchelor.rest.ResponseDecoderFactory
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorRestClient implements WebServiceInterface {

    protected BatchelorRestClient() {
        this.url = null;
    }

    /**
     * Creates an Batchelor REST client using the url as service address. The 
     * address should be a complete URL, i.e. https://localhost/batchelor/ws/rest. 
     * @param url The REST service address.
     */
    public BatchelorRestClient(URL url) {
        this.url = url;
    }

    /**
     * Creates an Batchelor REST client using the url as service address. The
     * address should be a complete URL, i.e. https://localhost/batchelor/ws/rest.
     * The connection is made thru the proxy.
     * @param url The REST service address.
     * @param proxy Connect thru this proxy.
     */
    public BatchelorRestClient(URL url, Proxy proxy) {
        this.url = url;
        this.proxy = proxy;
    }

    /**
     * Creates an Batchelor REST client using the url as service address. The
     * encoder argument request that server response messages should be encoded
     * using a specific method.
     * @param url The REST service address.
     * @param encoder Prefered response encoding method.
     */
    public BatchelorRestClient(URL url, ResponseEncoder encoder) {
        this.url = url;
        this.encoder = encoder;
    }

    /**
     * Request that server response messages from the REST service should be
     * encoded using the encoder type (XML or FOA).
     * @param encoder The prefered encoding method.
     */
    public void setResponseEncoder(ResponseEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * @param url The REST service address.
     */
    public void setServiceAddress(URL url) {
        this.url = url;
    }

    /**
     * @return The current used REST service address.
     */
    public final URL getServiceAddress() {
        return url;
    }

    /**
     * Set the proxy to connect thru.
     * @param proxy The web proxy.
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Return a formatted request URL. The URL is built from the service base
     * URL with the request path and encoding method appended.
     * @param append The request path.
     * @return The formatted request URL.
     * @throws java.net.MalformedURLException
     */
    private URL getRequestURL(String append) throws MalformedURLException {
        switch (encoder) {
            case FOA:
                if (append.indexOf('?') != -1) {
                    return new URL(url + "/" + append + "&encode=foa");
                } else {
                    return new URL(url + "/" + append + "?encode=foa");
                }
            case XML:
                if (append.indexOf('?') != -1) {
                    return new URL(url + "/" + append + "?encode=xml");
                } else {
                    return new URL(url + "/" + append + "&encode=xml");
                }
            default:
                return new URL(url + "/" + append);
        }
    }

    /**
     * @return The remote interface version string.
     * @throws java.rmi.RemoteException
     */
    public String version() throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            request.setURL(getRequestURL("version"));
            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            return result.getVersion();
        } catch (IOException e) {
            throw new RemoteException("Failed get remote version", e);
        }
    }

    /**
     * Queues an job for later execution. This version of enqueue reads the
     * job data from the indata string.
     *
     * @param indata The data to use for the enqueued job.
     * @return The enqueue result. The array might contain more than one element
     * if the enqueue operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     */
    public List<EnqueueResult> enqueue(String indata) throws RemoteException {
        ByteArrayInputStream stream = new ByteArrayInputStream(indata.getBytes());
        return enqueue(stream);
    }

    /**
     * Queues an job for later execution. This version of enqueue reads the
     * job data from the indata string.
     *
     * @param indata The input data for the enqueued job.
     * @return The enqueue result. The array might contain more than one element
     * if the enqueue operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     */
    public List<EnqueueResult> enqueue(byte[] indata) throws RemoteException {
        ByteArrayInputStream stream = new ByteArrayInputStream(indata);
        return enqueue(stream);
    }

    /**
     * Queues an job for later execution. This version of enqueue reads the
     * job data from the supplied file.
     *
     * @param file The file with input data for the enqueued job.
     * @return The enqueue result. The array might contain more than one element
     * if the enqueue operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     * @throws java.io.FileNotFoundException
     */
    public List<EnqueueResult> enqueue(File file) throws RemoteException, FileNotFoundException {
        FileInputStream stream = new FileInputStream(file);
        return enqueue(stream);
    }

    /**
     * Queues an job for later execution. This function enqueues the job using
     * the input stream as the job data source (indata). For some applications
     * this is a requirement because the indata might be big (maybe up to several
     * gigabytes), so trying to use an byte array or string would lead to an
     * OutOfMemoryException.
     *
     * @param stream The input stream to read job data and send to the enqeueud job.
     * @return The enqueue result. The array might contain more than one element
     * if the enqueue operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     */
    public List<EnqueueResult> enqueue(InputStream stream) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            byte[] buff = new byte[8192];
            request.setURL(getRequestURL("queue"));

            HttpURLConnection connection = request.getConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            while (stream.read(buff) != -1) {
                out.write(buff);
            }
            out.close();

            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isJobCollection()) {
                throw new RemoteException("Expected job list as response.");
            }

            List<EnqueueResult> list = new ArrayList<EnqueueResult>();
            for (Job job : result.getJob()) {
                EnqueueResult data = JobObjectConverter.createEnqueueResult(job);
                list.add(data);
            }
            return list;
        } catch (IOException e) {
            throw new RemoteException("Failed enqueue job", e);
        }
    }

    /**
     * A helper function for dequeue (remove) one or more jobs.
     * @param url The URL of the jobs to dequeue.
     * @return True if the method completes successful.
     * @throws java.rmi.RemoteException
     */
    protected boolean dequeue(URL url) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            request.setURL(url);
            HttpURLConnection connection = request.getConnection();
            connection.setRequestMethod("DELETE");
            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            return true;
        } catch (IOException e) {
            throw new RemoteException("Failed dequeue job", e);
        }
    }

    /**
     * Dequeues an already existing job. Calling this method should result in
     * immidiate termination of the job (if its running) and removal of its
     * associated job directory (on the server). The job is then removed from
     * the list of queued jobs.
     *
     * @param job The identity of the job.
     * @return True if job was dequeued successful.
     * @throws java.rmi.RemoteException
     */
    public boolean dequeue(JobIdentity job) throws RemoteException {
        String path = String.format("queue/%d/%s", job.getResult(), job.getJobID());
        try {
            return dequeue(getRequestURL(path));
        } catch (MalformedURLException e) {
            throw new RemoteException("Failed dequeue job", e);
        }
    }

    /**
     * Dequeue all jobs.
     * @return True if successful.
     * @throws java.rmi.RemoteException
     */
    public boolean dequeue() throws RemoteException {
        try {
            return dequeue(getRequestURL("qeueue/all"));
        } catch (MalformedURLException e) {
            throw new RemoteException("Failed dequeue all jobs", e);
        }
    }

    /**
     * Dequeue all jobs matching the filter (i.e. SUCCESS, ERROR or CRASHED). 
     * @param filter Filter out the jobs to dequeue.
     * @return True is successful.
     */
    public boolean dequeue(QueueFilterResult filter) throws RemoteException {
        String path = String.format("queue/filter/%s/list", filter.getValue());
        try {
            return dequeue(getRequestURL(path));
        } catch (MalformedURLException e) {
            throw new RemoteException("Failed dequeue job", e);
        }
    }

    /**
     * This method returns the list of queued jobs. Use the filter parameter
     * to restrict the number of elements returned to a subset of all enqueued
     * jobs.
     *
     * @param sort Allows caller to sort elements in the returned list. Use
     * null to disable sorting.
     * @param filter Allows caller to filter elements in the returned list.
     * Use null to disable filtering.
     * @return The list of queued jobs (possibly a subset).
     * @throws java.rmi.RemoteException
     * @see QueueFilterResult
     * @see QueueSortResult
     */
    public List<QueuedJob> queue(QueueSortResult sort, QueueFilterResult filter) throws RemoteException {
        String path;

        if (sort != null && filter != null) {
            path = String.format("queue/sort/%s/data?filter=%s", sort.getValue(), filter.getValue());
        } else if (sort != null) {
            path = String.format("queue/sort/%s/data", sort.getValue());
        } else if (filter != null) {
            path = String.format("queue/filter/%s/data", filter.getValue());
        } else {
            path = "queue/all/data";
        }

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            request.setURL(getRequestURL(path));
            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }

            List<QueuedJob> list = new ArrayList<QueuedJob>();
            for (Job job : result.getJob()) {
                JobIdentity ident = JobObjectConverter.createJobIdentity(job);
                QueuedJob data = new QueuedJob(ident, job.getState());
                list.add(data);
            }
            return list;
        } catch (IOException e) {
            throw new RemoteException("Failed list queue", e);
        }
    }

    /**
     * This method gives the caller an opportunity to get an list of queued
     * jobs that has been enqueued after the given timestamp. The timestamp
     * is the number of seconds since the start of the UNIX epoch (00:00:00
     * UTC on January 1, 1970).
     *
     * @param stamp The UNIX timestamp.
     * @return The list of jobs enqueued after the UNIX stamp.
     * @throws java.rmi.RemoteException
     */
    public List<QueuedJob> watch(int stamp) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            request.setURL(getRequestURL("watch"));

            HttpURLConnection connection = request.getConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Write request parameters:
            String params = String.format("format=data&stamp=%d", stamp);
            OutputStream output = connection.getOutputStream();
            output.write(params.getBytes());

            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            // if (!response.isJobCollection()) {
            //     throw new RemoteException("Expected job collection as response.");
            // }

            List<QueuedJob> list = new ArrayList<QueuedJob>();
            for (Job job : result.getJob()) {
                list.add(JobObjectConverter.createQueuedJob(job));
            }
            return list;
        } catch (IOException e) {
            throw new RemoteException("Failed watch job(s)", e);
        }
    }

    /**
     * Suspend (pause) an already running job. If the job is not in the running
     * state, then this is a noop method. Using suspend must be supported on
     * the server side by enabling job control.
     *
     * @param job The unique job identity.
     * @return True if job where suspended.
     * @throws java.rmi.RemoteException
     */
    public boolean suspend(JobIdentity job) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            String path = String.format("suspend/%d/%s", job.getResult(), job.getJobID());
            request.setURL(getRequestURL(path));

            HttpURLConnection connection = request.getConnection();
            connection.setRequestMethod("POST");

            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isStatusObject()) {
                throw new RemoteException("Expected status as response.");
            }
            return true;
        } catch (IOException e) {
            throw new RemoteException("Failed suspend job", e);
        }
    }

    /**
     * Resume an already suspended (paused) job. If the job is not paused, then
     * this is a noop method. Using resume must be supported on the server side
     * by enabling job control.
     *
     * @param job The unique job identity.
     * @return True if job where resumed.
     * @throws java.rmi.RemoteException
     */
    public boolean resume(JobIdentity job) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            String path = String.format("resume/%d/%s", job.getResult(), job.getJobID());
            request.setURL(getRequestURL(path));

            HttpURLConnection connection = request.getConnection();
            connection.setRequestMethod("POST");

            Result result = request.getServerResponse();

            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isStatusObject()) {
                throw new RemoteException("Expected status as response.");
            }
            return true;
        } catch (IOException e) {
            throw new RemoteException("Failed resume job", e);
        }
    }

    /**
     * This method gives the caller an list of all queued jobs. The list can
     * be used at client side to present the remote queue in a tree structure
     * for browsing.
     *
     * @return The list of queued jobs.
     * @throws java.rmi.RemoteException
     */
    public List<JobIdentity> opendir() throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            request.setURL(getRequestURL("result"));
            Result result = request.getServerResponse();
            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isLinkCollection()) {
                throw new RemoteException("Expected link collection as response");
            }
            List<JobIdentity> list = new ArrayList<JobIdentity>();
            for (Link link : result.getLink()) {
                JobIdentity ident = LinkObjectConverter.createJobIdentity(link);
                list.add(ident);
            }
            return list;
        } catch (IOException e) {
            throw new RemoteException("Failed get job directory listing", e);
        }
    }

    /**
     * Get a list of all files and directories in the job directory associated
     * with the JobIdentity object.
     *
     * @param job An unique identifier of the queued job.
     * @return The list of files and directories.
     * @throws java.rmi.RemoteException
     */
    public List<String> readdir(JobIdentity job) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            String path = String.format("result/%d/%s", job.getResult(), job.getJobID());
            request.setURL(getRequestURL(path));
            Result result = request.getServerResponse();
            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isLinkCollection()) {
                throw new RemoteException("Expected link collection as response");
            }
            List<String> list = new ArrayList<String>();
            for (Link link : result.getLink()) {
                list.add(LinkObjectConverter.createFilePath(link, job));
            }
            return list;
        } catch (IOException e) {
            throw new RemoteException("Failed get job directory listing", e);
        }
    }

    /**
     * <p>Opens the given file from the job directory associated with the 
     * JobIdentity object. The filename path should use '/' as path separator
     * to descend into subdirectories.</p>
     *
     * <p>This special version of fopen() returns a File object instead of a byte
     * array. Use this method if the fetched file is expected to be large to
     * avoid an OutOfMemoryException.</p>
     *
     * <p>The File object can be used to read the file response in stream mode
     * and exposes the following methods:
     * <ul><li>getEncoding(): Either "binary" or "base64".</li>
     *     <li>getName(): The filename on the server.</li>
     *     <li>getSize(): The number of bytes that can be read.</li>
     *     <li>getInputStreamReader(): Returns the input stream.</li>
     * </ul>
     * If encoding is "binary", then the input stream will return the file
     * content "as-is". Basically, it means the file bytes has been sent without
     * any processing. If encoding is "base64", then the input stream will 
     * return the file content encoded in Base64 encoding. The se.uu.bmc.it.batchelor.codecs.base64.Base64Decoder
     * class can be used to decode the input stream.
     * </p>
     *
     * @param job An unique identifier of the queued job.
     * @param file The remote filename.
     * @param timeout Sets the read timeout to a specified timeout, in milliseconds. If the timeout expires before there is data available for read, a java.net.SocketTimeoutException is raised. A timeout of zero is interpreted as an infinite timeout.
     * @return The response file object.
     * @see se.uu.bmc.it.batchelor.rest.schema.File
     * @see se.uu.bmc.it.batchelor.codecs.base64.Base64Decoder
     * @throws java.rmi.RemoteException
     * @throws java.net.SocketTimeoutException
     */
    public se.uu.bmc.it.batchelor.rest.schema.File fopen(
        JobIdentity job, String file, int timeout)
        throws RemoteException, SocketTimeoutException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            String path = String.format("result/%d/%s/%s",
                job.getResult(), job.getJobID(), file);
            request.setURL(getRequestURL(path));
            request.getConnection().setReadTimeout(timeout);

            Result result = request.getServerResponse();
            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isFileObject()) {
                throw new RemoteException("Expected file object as response");
            }
            return result.getFile();
        } catch (IOException e) {
            throw new RemoteException("Failed get file object response", e);
        }
    }

    /**
     * <p>Opens the given file from the job directory associated with the
     * JobIdentity object. The filename path should use '/' as path separator
     * to descend into subdirectories.</p>
     *
     * <p>This special version of fopen() saves the content of the remote file
     * to a local file (the path argument).</p>
     * 
     * @param job An unique identifier of the queued job.
     * @param file The remote filename.
     * @param path The local filename.
     * @throws java.rmi.RemoteException
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void fopen(JobIdentity job, String file, File path)
        throws RemoteException, FileNotFoundException, IOException {

        se.uu.bmc.it.batchelor.rest.schema.File remote = fopen(job, file, 0);

        InputStream in = (InputStream) remote.getInputStream();
        OutputStream out = new FileOutputStream(path.getAbsolutePath());

        if (remote.getEncoding().compareTo("base64") == 0) {
            Base64Decoder decoder = new Base64Decoder();
            if (remote.getInputStream() != null) {
                if (remote.getSize() > 1024 * 1024) {
                    decoder.setSize(1024 * 1024);
                }
                byte[] buff = decoder.decode(in);
                while (buff != null) {
                    out.write(buff, 0, decoder.getSize());
                    buff = decoder.decode(in);
                }
            } else {
                if (remote.getSize() != 0) {
                    out.write(decoder.decode(remote.getContent()));
                }
            }
        } else {
            if (remote.getInputStream() != null) {
                long size = 0, read = 0, want = 0, curr = 0;
                byte[] buff = new byte[8192];
                while (size > read) {
                    want = size - read > buff.length ? buff.length : size - read;
                    curr = in.read(buff, 0, (int) want);
                    if (curr == -1) {
                        break;
                    }
                    out.write(buff, 0, (int) curr);
                }
            } else {
                byte[] buff = remote.getContent().getBytes();
                out.write(buff, 0, remote.getSize().intValue());
            }
        }
        out.close();
    }

    /**
     * Opens the given file from the job directory associated with the
     * JobIdentity object. The filename path should use '/' as path separator
     * to descend into subdirectories.
     *
     * @param job An unique identifier of the queued job.
     * @param file The remote filename.
     * @return The file contents as an byte array.
     * @throws java.rmi.RemoteException
     */
    public byte[] fopen(JobIdentity job, String file) throws RemoteException {

        try {
            se.uu.bmc.it.batchelor.rest.schema.File remote = fopen(job, file, 0);
            if (remote.getEncoding().compareTo("base64") == 0) {
                Base64Decoder decoder = new Base64Decoder();
                if (remote.getInputStream() != null) {
                    return decoder.decode((InputStream) remote.getInputStream(), remote.getSize().intValue());
                } else {
                    return decoder.decode(remote.getContent());
                }
            } else {
                if (remote.getInputStream() != null) {
                    byte[] buff = new byte[remote.getSize().intValue()];
                    InputStream stream = (InputStream) remote.getInputStream();
                    stream.read(buff, 0, buff.length);
                    return buff;
                } else {
                    return remote.getContent().getBytes();
                }
            }
        } catch (SocketTimeoutException e) {
            throw new RemoteException("Timeout waiting for server", e);
        } catch (IOException e) {
            throw new RemoteException("Failed read response", e);
        }
    }

    /**
     * This method lets the caller check the status of a single job. This could
     * be useful for monitoring an enqueued job for completion (polling).
     *
     * @param job The job identity.
     * @return Details on the queued job.
     * @throws java.rmi.RemoteException
     */
    public QueuedJob stat(JobIdentity job) throws RemoteException {

        ServerConnectionFactory factory = ServerConnectionFactory.getFactory();
        HttpServerRequest request = factory.createHttpServerConnection(url);

        try {
            String path = String.format("queue/%d/%s", job.getResult(), job.getJobID());
            request.setURL(getRequestURL(path));
            Result result = request.getServerResponse();
            HttpServerResponse response = new HttpServerResponse(result);
            if (response.getState() == HttpServerResponse.State.Failed) {
                throw new RemoteException(result.getError().getMessage());
            }
            if (!response.isJobCollection()) {
                throw new RemoteException("Expected job object as response");
            }
            String state = result.getJob().get(0).getState();
            return new QueuedJob(job, state);
        } catch (IOException e) {
            throw new RemoteException("Failed get job directory listing", e);
        }
    }
    private URL url;       // The base URL for REST node path.
    // The prefered response encoding method:
    private ResponseEncoder encoder = ResponseEncoder.DEFAULT;
    private Proxy proxy = Proxy.NO_PROXY;  // Optional proxy to connect thru.
}
