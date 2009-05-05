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
 * JobIdentity.java
 *
 * Created: Apr 3, 2009, 09:03:18 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor;

import java.io.Serializable;

/**
 * This class describes an enqueued job.
 *
 * @author Anders Lövgren
 */
public class JobIdentity implements Serializable {

    private String jobID;      // The Job ID
    private int result;        // The job directory

    /**
     * Creates an object with all member fields set to null.
     */
    public JobIdentity() {
        jobID = null;
        result = 0;
    }

    /**
     * Creates an object.
     * @param jobID The job ID.
     * @param result The result directory (job directory).
     */
    public JobIdentity(String jobID, int result) {
        this.jobID = jobID;
        this.result = result;
    }

    /**
     * @return The job ID.
     */
    public String getJobID() {
        return jobID;
    }

    /**
     * @return The result directory (job directory).
     */
    public int getResult() {
        return result;
    }

    /**
     * Sets the job identity.
     * @param jobID The job ID.
     */
    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    /**
     * Sets the result directory (job directory).
     * @param result The result directory.
     */
    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format("Job ID: %s\t= { Result=%s }",
                jobID, result);
    }
}
