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
import java.net.MalformedURLException;
// import java.net.HttpURLConnection;

/**
 * This class implements a RESTful based client side for communication with
 * the Batchelor batch job queues web service interface.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorRestClient implements WebServiceInterface {

    public BatchelorRestClient() throws MalformedURLException {
        this.url = new URL("http://localhost/batchelor/ws/rest/");
        this.decoder = new FoaResponseDecoder();
    }

    public BatchelorRestClient(URL url, ResponseDecoder decoder) {
        this.url = url;
        this.decoder = decoder;
    }
    
    public String version() {
        return null;
    }

    public EnqueueResult[] enqueue(String indata) {
        return null;
    }

    public boolean dequeue(JobIdentity job) {
        return false;
    }

    public QueuedJob[] queue(String sort, String filter) {
        return null;
    }

    public QueuedJob[] watch(int stamp) {
        return null;
    }

    public boolean suspend(JobIdentity job) {
        return false;
    }

    public boolean resume(JobIdentity job) {
        return false;
    }

    public JobIdentity[] opendir() {
        return null;
    }

    public String[] readdir(JobIdentity job) {
        return null;
    }

    public byte[] fopen(JobIdentity job, String file) {
        return null;
    }

    public QueuedJob stat(JobIdentity job) {
        return null;
    }

    private URL url;                   // The server URL
    private ResponseDecoder decoder;   // The decoder plugin (XML or FOA)
}
