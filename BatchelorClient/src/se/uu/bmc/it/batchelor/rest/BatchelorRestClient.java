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

import java.net.URL;
import java.net.Proxy;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
// import java.net.HttpURLConnection;

/**
 * This class implements a RESTful based client side for communication with
 * the Batchelor batch job queues web service interface.
 *
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
     * @param decoder Prefered response encoding method.
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
     * @return The remote interface version string.
     * @throws java.rmi.RemoteException
     */
    public String version() throws RemoteException {
        return null;
    }

    /**
     * Queues an job for later execution.
     *
     * @param indata The data to use for the enqueued job.
     * @return The enqueue result. The array might contain more than one elemnt
     * if the enqueue operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     */
    public EnqueueResult[] enqueue(String indata) throws RemoteException {
        return null;
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
    public boolean dequeue(JobIdentity job) {
        return false;
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
     * @return The list of queued jobs (possbly a subset).
     * @throws java.rmi.RemoteException
     * @see QueueFilterResult
     * @see QueueSortResult
     */
    public QueuedJob[] queue(String sort, String filter) throws RemoteException {
        return null;
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
    public QueuedJob[] watch(int stamp) throws RemoteException {
        return null;
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
        return false;
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
        return false;
    }

    /**
     * This method gives the caller an list of all queued jobs. The list can
     * be used at client side to present the remote queue in a tree structure
     * for browsing.
     *
     * @return The list of queued jobs.
     * @throws java.rmi.RemoteException
     */
    public JobIdentity[] opendir() throws RemoteException {
        return null;
    }

    /**
     * Get a list of all files and directories in the job directory associated
     * with the JobIdentity object.
     *
     * @param job An unique identifier of the queued job.
     * @return The list of files and directories.
     * @throws java.rmi.RemoteException
     */
    public String[] readdir(JobIdentity job) throws RemoteException {
        return null;
    }

    /**
     * Opens the given file from the job directory associated with the
     * JobIdentity object. The filename path should use '/' as path separator
     * to descend into subdirectories.
     *
     * @param job An unique identifier of the queued job.
     * @param file The filename.
     * @return The file contents as an byte array.
     * @throws java.rmi.RemoteException
     *
     * TODO: add variant of fopen that returns a stream instead of a byte array.
     */
    public byte[] fopen(JobIdentity job, String file) throws RemoteException {
        return null;
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
        return null;
    }
    private URL url;       // The server base URL
    // The prefered response encoding method:
    private ResponseEncoder encoder = ResponseEncoder.DEFAULT;
    private Proxy proxy = Proxy.NO_PROXY;  // Optional proxy to connect thru.
}
