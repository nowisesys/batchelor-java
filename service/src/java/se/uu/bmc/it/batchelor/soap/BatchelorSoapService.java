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
package se.uu.bmc.it.batchelor.soap;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

import javax.jws.WebParam;
import javax.jws.WebMethod;
import javax.jws.WebService;

import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.WebServiceInterface;
import se.uu.bmc.it.batchelor.QueueSortResult;
import se.uu.bmc.it.batchelor.QueueFilterResult;

/**
 * This class has two purposes:
 *
 * 1. Unit test of the Batchelor SOAP client. 2. For generation of the client SOAP classes (thru
 * JAX-WS).
 *
 * It could be deployed under Tomcat and will then act as a source for the WSDL used in step 2 when
 * client classes is generated. Unless you are developing the client, its pointless to deploy this
 * service.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
@WebService()
public class BatchelorSoapService implements WebServiceInterface {

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "version")
    @Override
    public String version() throws RemoteException {
        return "1.0";
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "enqueue")
    @Override
    public List<EnqueueResult> enqueue(@WebParam(name = "indata") String indata) throws RemoteException {
        if (indata == null) {
            throw new RemoteException("Invalid indata (null)");
        }
        List<EnqueueResult> result = new ArrayList<EnqueueResult>();
        result.add(new EnqueueResult("Job ABC", 123456789, "2009-04-06", "21:43:12", 234567890));
        result.add(new EnqueueResult("Job XYZ", 243546567, "2009-04-07", "03:01:54", 123456789));
        return result;
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "dequeue")
    @Override
    public boolean dequeue(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        if (job == null) {
            return false;
        }
        return !(job.getJobID() == null || job.getResult() == 0);
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "queue")
    @Override
    public List<QueuedJob> queue(@WebParam(name = "sort") QueueSortResult sort, @WebParam(name = "filter") QueueFilterResult filter) throws RemoteException {
        List<QueuedJob> result = new ArrayList<QueuedJob>();
        result.add(new QueuedJob(new JobIdentity("Job ABC", 123456789), "running"));
        result.add(new QueuedJob(new JobIdentity("Job XYZ", 987654321), "pending"));
        return result;
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "watch")
    @Override
    public List<QueuedJob> watch(@WebParam(name = "stamp") int stamp) throws RemoteException {
        List<QueuedJob> result = new ArrayList<QueuedJob>();
        result.add(new QueuedJob(new JobIdentity("Job DEF", 123456789), "running"));
        return result;
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "suspend")
    @Override
    public boolean suspend(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        if (job == null) {
            throw new RemoteException("Invalid indata (null)");
        }
        return !(job.getJobID() == null || job.getResult() == 0);
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "resume")
    @Override
    public boolean resume(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        if (job == null) {
            throw new RemoteException("Invalid indata (null)");
        }
        return !(job.getJobID() == null || job.getResult() == 0);
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "opendir")
    @Override
    public List<JobIdentity> opendir() throws RemoteException {
        List<JobIdentity> result = new ArrayList<JobIdentity>();
        result.add(new JobIdentity("Job GHI", 123456789));
        result.add(new JobIdentity("Job JKL", 987654321));
        return result;
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "readdir")
    @Override
    public List<String> readdir(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        if (job == null) {
            throw new RemoteException("Invalid indata (null)");
        }
        List<String> result = new ArrayList<String>();
        if (job.getJobID() == null || job.getResult() == 0) {
            return result;
        }
        result.add("/var/cache/jobs/23456/78901/file1.txt");
        result.add("/var/cache/jobs/23456/78901/image2.jpeg");
        result.add("/var/cache/jobs/23456/78901/file3.pdf");
        return result;
    }

    /**
     * Web service operation
     *
     * @throws java.rmi.RemoteException
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "fopen")
    @Override
    public byte[] fopen(@WebParam(name = "job")
            final JobIdentity job, @WebParam(name = "file") String file) throws RemoteException {
        if (job == null || file == null) {
            throw new RemoteException("Invalid indata (null)");
        }
        if (job.getJobID() == null || job.getResult() == 0) {
            return null;
        }
        int num = 100;  // Send 100 bytes.
        byte[] result = new byte[num];
        for (int i = 0; i < num; ++i) {
            result[i] = (byte) ((i + 33) % 127);
        }
        return result;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "stat")
    @Override
    public QueuedJob stat(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        if (job == null) {
            throw new RemoteException("Invalid indata (null)");
        }
        if (job.getJobID() == null || job.getResult() == 0) {
            return null;
        }
        QueuedJob result = new QueuedJob(job, "error");
        return result;
    }
}
