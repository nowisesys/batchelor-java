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
 * BatchelorSoapClient.java
 *
 * Created: Apr 8, 2009, 11:41:34 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.soap;

import se.uu.bmc.it.batchelor.WebServiceInterface;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.QueueSortResult;
import se.uu.bmc.it.batchelor.QueueFilterResult;

import se.uu.bmc.it.batchelor.soap.schema.BatchelorSoapServiceService;
import se.uu.bmc.it.batchelor.soap.schema.BatchelorSoapService;
import se.uu.bmc.it.batchelor.soap.convert.*;

import java.util.List;
import java.util.ArrayList;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;

/**
 * <p>This class implements the WebServiceInterface providing an SOAP client
 * that is used for connections against Batchelors SOAP web service interface.
 * All remote methods might throw an RemoteException.</p>
 *
 * <p>The URL to use deserves an bit of explanation. It is the local or remote 
 * location of an WSDL file that describes the Batchelor SOAP service, not the
 * location of the Batchelor SOAP service or its base URL. An typical example
 * is "http://host.example.com/batchelor/ws/schema/wsdl/?wsdl". Note that the
 * WSDL and the SOAP service itself might be at different locations, it might
 * not even be located at the same host!</p>
 *
 * <p>This example demonstrate the essential parts of using this class:</p>
 * <p><pre><code>
 * import java.rmi.RemoteException;
 * import java.net.URL;
 * import se.uu.bmc.it.batchelor.soap;
 *
 * class SoapServiceClient {
 *     private BatchelorSoapClient service;
 *
 *     public SoapServiceClient(URL url) {
 *         service = new BatchelorSoapClient(url);
 *     }
 *
 *     // Add some functions that calls remote methods using the service member...
 *
 * }
 * </code></pre></p>
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapClient implements WebServiceInterface {

    private URL url;      // Non-default WSDL location.
    private String name;  // The service name.

    /**
     * This contructor overrides the WSDL location that JAX-WS has generated
     * the service code from. Use this constructor for binding to an volatile
     * Batchelor service (on some host).
     * 
     * @param url The WSDL location.
     */
    public BatchelorSoapClient(URL url) {
        this.url = url;
        this.name = null;
    }

    /**
     * This contructor overrides the WSDL location that JAX-WS has generated
     * the service code from. Use this constructor for binding to an volatile
     * Batchelor service (on some host). The name argument is the service name.
     *
     * @param url The WSDL location.
     * @param name The service name.
     */
    public BatchelorSoapClient(URL url, String name) {
        this.url = url;
        this.name = name;
    }

    /**
     * Set the location of the WSDL for the service to consume. This adress is
     * used by the SOAP binding provider.
     * @param url The WSDL location.
     */
    public void setServiceAddress(URL url) {
        this.url = url;
    }

    /**
     * Set the name of the service. This method allows the service name to
     * be overridden. If name is null, then the previous used service name
     * is reset causing the SOAP binding provider to use the service name from
     * the WSDL instead.
     * 
     * @param name The service name.
     */
    public void setServiceName(String name) {
        this.name = name;
    }

    /**
     * @return The service port.
     */
    private BatchelorSoapService getServicePort() {
        BatchelorSoapServiceService service;
        BatchelorSoapService port;
        if (url != null) {
            if (name != null) {
                // Use custom WSDL and service name:
                service = new BatchelorSoapServiceService(
                    url,
                    new QName("http://soap.batchelor.it.bmc.uu.se/", name));
                port = service.getBatchelorSoapServicePort();
            } else {
                // Use custom WSDL, but rely on that endpoint is using our
                // default service name:
                service = new BatchelorSoapServiceService(
                    url,
                    new QName("http://soap.batchelor.it.bmc.uu.se/",
                    "BatchelorSoapServiceService"));
                port = service.getBatchelorSoapServicePort();
            }
        } else {
            // This port uses default WSDL and service name that was defined
            // and generated by JAX-WS:
            service = new BatchelorSoapServiceService();
            port = service.getBatchelorSoapServicePort();
        }
        return port;
    }

    /**
     * @return The remote interface version string.
     * @throws java.rmi.RemoteException
     */
    public String version() throws RemoteException {

        try { // Call Web Service Operation
            String result = getServicePort().version();
            return result;
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
        }
    }

    /**
     * Queues an job for later execution.
     *
     * @param indata The data to use for the enqueued job.
     * @return The enqueue result. The array might contain more than one elemnt
     * if the enqueue operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     */
    public List<EnqueueResult> enqueue(String indata) throws RemoteException {

        try { // Call Web Service Operation
            List<EnqueueResult> list = new ArrayList<EnqueueResult>();
            for (se.uu.bmc.it.batchelor.soap.schema.EnqueueResult in : getServicePort().enqueue(indata)) {
                list.add(EnqueueResultConverter.createEnqueueResult(in));
            }
            return list;
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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

        try { // Call Web Service Operation
            return getServicePort().dequeue(JobIdentityConverter.createJobIdentity(job));
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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
     * @return The list of queued jobs (possbly a subset).
     * @throws java.rmi.RemoteException
     * @see QueueFilterResult
     * @see QueueSortResult
     */
    public List<QueuedJob> queue(QueueSortResult sort, QueueFilterResult filter) throws RemoteException {

        try { // Call Web Service Operation
            List<QueuedJob> list = new ArrayList<QueuedJob>();
            for (se.uu.bmc.it.batchelor.soap.schema.QueuedJob in : getServicePort().queue(
                QueueSortResultConverter.getQueueSortResult(sort),
                QueueFilterResultConverter.getQueueFilterResult(filter))) {
                list.add(QueuedJobConverter.createQueuedJob(in));
            }
            return list;
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
        }
    }

    /**
     * This method gives the caller an opportunity to get an list of queued
     * jobs that has been enqueued after the given timestamp. The timestamp
     * is the number of seconds since the start of the UNIX epoch (00:00:00
     * UTC on January 1, 1970). The stamp to use is typical obtained by calling
     * getStamp() using the EnqueueResult object returned by enqueue().
     *
     * @param stamp The UNIX timestamp.
     * @return The list of jobs enqueued after the UNIX stamp.
     * @throws java.rmi.RemoteException
     */
    public List<QueuedJob> watch(int stamp) throws RemoteException {

        try { // Call Web Service Operation
            List<QueuedJob> list = new ArrayList<QueuedJob>();
            for (se.uu.bmc.it.batchelor.soap.schema.QueuedJob in : getServicePort().watch(stamp)) {
                list.add(QueuedJobConverter.createQueuedJob(in));
            }
            return list;
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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

        try { // Call Web Service Operation
            return getServicePort().suspend(JobIdentityConverter.createJobIdentity(job));
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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

        try { // Call Web Service Operation
            return getServicePort().resume(JobIdentityConverter.createJobIdentity(job));
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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

        try { // Call Web Service Operation
            List<JobIdentity> list = new ArrayList<JobIdentity>();
            for (se.uu.bmc.it.batchelor.soap.schema.JobIdentity in : getServicePort().opendir()) {
                list.add(JobIdentityConverter.createJobIdentity(in));
            }
            return list;
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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

        try { // Call Web Service Operation
            return getServicePort().readdir(JobIdentityConverter.createJobIdentity(job));
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
        }
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
     */
    public byte[] fopen(JobIdentity job, String file) throws RemoteException {

        try { // Call Web Service Operation
            return getServicePort().fopen(JobIdentityConverter.createJobIdentity(job), file);
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
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

        try { // Call Web Service Operation
            return QueuedJobConverter.createQueuedJob(getServicePort().stat(JobIdentityConverter.createJobIdentity(job)));
        } catch (Exception ex) {
            throw new RemoteException("Calling remote method failed", ex);
        }
    }
}
