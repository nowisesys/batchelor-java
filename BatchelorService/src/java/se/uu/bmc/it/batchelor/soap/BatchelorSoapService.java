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
import javax.jws.WebParam;
import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.WebServiceInterface;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * This class has two purposes:
 *
 * 1. Unit test of the Batchelor SOAP client.
 * 2. For generation of the client SOAP classes (thru JAX-WS).
 *
 * It could be deployed under Tomcat and will then act as a source for the WSDL
 * used in step 2 when client classes is generated. Unless you are developing
 * the client, its pointless to deploy this service.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
@WebService()
public class BatchelorSoapService implements WebServiceInterface {

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "version")
    public String version() throws RemoteException {
        return "1.0";
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "enqueue")
    public EnqueueResult[] enqueue(@WebParam(name = "indata") String indata) throws RemoteException {
        EnqueueResult[] result = new EnqueueResult[2];
        result[0] = new EnqueueResult("Job ABC", "ABCD4321", "2009-04-06", "21:43:12", 234567890);
        result[1] = new EnqueueResult("Job XYZ", "EFGH8765", "2009-04-07", "03:01:54", 123456789);
        return result;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "dequeue")
    public boolean dequeue(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        return true;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "queue")
    public QueuedJob[] queue(@WebParam(name = "sort") String sort, @WebParam(name = "filter") String filter) throws RemoteException {
        QueuedJob[] result = new QueuedJob[2];
        result[0] = new QueuedJob(new JobIdentity("Job ABC", "/var/cache/jobs/12345/67890"), "running");
        result[1] = new QueuedJob(new JobIdentity("Job XYZ", "/var/cache/jobs/09876/54321"), "pending");
        return result;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "watch")
    public QueuedJob[] watch(@WebParam(name = "stamp") int stamp) throws RemoteException {
        QueuedJob[] result = new QueuedJob[1];
        result[0] = new QueuedJob(new JobIdentity("Job DEF", "/var/cache/jobs/54321/09876"), "running");
        return result;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "suspend")
    public boolean suspend(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        return true;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "resume")
    public boolean resume(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        return true;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "opendir")
    public JobIdentity[] opendir() throws RemoteException {
        JobIdentity[] result = new JobIdentity[2];
        result[0] = new JobIdentity("Job GHI", "/var/cache/jobs/23456/78901");
        result[1] = new JobIdentity("Job JKL", "/var/cache/jobs/34567/89012");
        return result;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "readdir")
    public java.lang.String[] readdir(@WebParam(name = "job")
            final JobIdentity job) throws RemoteException {
        String[] result = new String[3];
        result[0] = "/var/cache/jobs/23456/78901/file1.txt";
        result[1] = "/var/cache/jobs/23456/78901/image2.jpeg";
        result[2] = "/var/cache/jobs/23456/78901/file3.pdf";
        return result;
    }

    /**
     * Web service operation
     * @see WebServiceInterface
     */
    @WebMethod(operationName = "fopen")
    public byte[] fopen(@WebParam(name = "job")
            final JobIdentity job, @WebParam(name = "file") String file) throws RemoteException {
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
    public QueuedJob stat(@WebParam(name = "job")
    final JobIdentity job) throws RemoteException {
        QueuedJob result = new QueuedJob(job, "error");
        return result;
    }
}
