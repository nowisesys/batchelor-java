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
 * WebServiceInterface.java
 *
 * Created: Apr 2, 2009, 11:22:25 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor;

import java.util.List;

/**
 * The interface that is implemented by concrete subclasses to provide client side web service
 * communication with Batchelors SOAP and RESTful web service interfaces on the server side.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public interface WebServiceInterface {

    /**
     * @return The remote interface version string.
     * @throws java.rmi.RemoteException
     */
    public String version() throws java.rmi.RemoteException;

    /**
     * Queues an job for later execution.
     *
     * @param indata The data to use for the enqueued job.
     * @return The enqueue result. The array might contain more than one elemnt if the enqueue
     * operation results in multiple subjobs.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public List<EnqueueResult> enqueue(String indata) throws java.rmi.RemoteException;

    /**
     * Dequeues an already existing job. Calling this method should result in immidiate termination
     * of the job (if its running) and removal of its associated job directory (on the server). The
     * job is then removed from the list of queued jobs.
     *
     * @param job The identity of the job.
     * @return True if job was dequeued successful.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public boolean dequeue(JobIdentity job) throws java.rmi.RemoteException;

    /**
     * This method returns the list of queued jobs. Use the filter parameter to restrict the number
     * of elements returned to a subset of all enqueued jobs.
     *
     * @param sort Allows caller to sort elements in the returned list. Use null to disable sorting.
     * @param filter Allows caller to filter elements in the returned list. Use null to disable
     * filtering.
     * @return The list of queued jobs (possbly a subset).
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     * @see QueueFilterResult
     * @see QueueSortResult
     */
    public List<QueuedJob> queue(QueueSortResult sort, QueueFilterResult filter) throws java.rmi.RemoteException;

    /**
     * This method gives the caller an opportunity to get an list of queued jobs that has been
     * enqueued after the given timestamp. The timestamp is the number of seconds since the start of
     * the UNIX epoch (00:00:00 UTC on January 1, 1970).
     *
     * @param stamp The UNIX timestamp.
     * @return The list of jobs enqueued after the UNIX stamp.
     * @throws java.rmi.RemoteException
     */
    public List<QueuedJob> watch(int stamp) throws java.rmi.RemoteException;

    /**
     * Suspend (pause) an already running job. If the job is not in the running state, then this is
     * a noop method. Using suspend must be supported on the server side by enabling job control.
     *
     * @param job The unique job identity.
     * @return True if job where suspended.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public boolean suspend(JobIdentity job) throws java.rmi.RemoteException;

    /**
     * Resume an already suspended (paused) job. If the job is not paused, then this is a noop
     * method. Using resume must be supported on the server side by enabling job control.
     *
     * @param job The unique job identity.
     * @return True if job where resumed.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public boolean resume(JobIdentity job) throws java.rmi.RemoteException;

    /**
     * This method gives the caller an list of all queued jobs. The list can be used at client side
     * to present the remote queue in a tree structure for browsing.
     *
     * @return The list of queued jobs.
     * @throws java.rmi.RemoteException
     */
    public List<JobIdentity> opendir() throws java.rmi.RemoteException;

    /**
     * Get a list of all files and directories in the job directory associated with the JobIdentity
     * object.
     *
     * @param job An unique identifier of the queued job.
     * @return The list of files and directories.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public List<String> readdir(JobIdentity job) throws java.rmi.RemoteException;

    /**
     * Opens the given file from the job directory associated with the JobIdentity object. The
     * filename path should use '/' as path separator to descend into subdirectories.
     *
     * @param job An unique identifier of the queued job.
     * @param file The filename.
     * @return The file contents as an byte array.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public byte[] fopen(JobIdentity job, String file) throws java.rmi.RemoteException;

    /**
     * This method lets the caller check the status of a single job. This could be useful for
     * monitoring an enqueued job for completion (polling).
     *
     * @param job The job identity.
     * @return Details on the queued job.
     * @throws java.rmi.RemoteException
     * @throws java.lang.NullPointerException
     */
    public QueuedJob stat(JobIdentity job) throws java.rmi.RemoteException;

}
