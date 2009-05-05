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
 * EnqueueResult.java
 *
 * Created: Apr 3, 2009, 08:59:22 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor;

import java.io.Serializable;

/**
 * This class describes an newly enqueued job. The stamp is the UNIX time in
 * seconds since the beginning of the UNIX epoch (usually 01:00 1970-01-01).
 * This stamp is the clock on the server where/when the job was enqueued.
 *
 * @author Anders Lövgren
 */
public class EnqueueResult implements Serializable {

    private String jobID;   // The job ID.
    private int result;     // The job directory.
	private String date;    // Enqueue date.
	private String time;    // Enqueue time.
	private int stamp;      // UNIX timestamp (for calling watch).

    /**
     * Creates an enqueue result object with default values for member fields
     * (null and zeroes).
     */
	public EnqueueResult() {
		jobID = null;
        result = 0;
		date = null;
		time = null;
		stamp = 0;
	}

    /**
     * Creates an enqueue result object.
     * @param jobID The job ID (could be numberic).
     * @param result The result (job directory) identifier.
     * @param date The date as an string (i.e. yyyy-mm-dd).
     * @param time The time as an string (i.e. hh:mm:ss).
     * @param stamp The UNIX timestamp.
     */
	public EnqueueResult(String jobID, int result, String date, String time, int stamp) {
		this.jobID = jobID;
        this.result = result;
		this.date = date;
		this.time = time;
		this.stamp = stamp;
	}

    /**
     * @return The job ID.
     */
	public String getJobID() {
		return jobID;
	}

    /**
     * Sets the job ID.
     * @param jobID The string identifying the job in the batch queue.
     */
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

    /**
     * @return The result (job directory) identifier.
     */
    public int getResult() {
        return result;
    }

    /**
     * Sets the result (job directory) identifier.
     * @param result The result directory.
     */
    public void setResult(int result) {
        this.result = result;
    }

    /**
     * Sets the date (i.e. yyyy-mm-dd) when the job was enqueued.
     * @param date The string representation of the date.
     */
	public void setDate(String date) {
		this.date = date;
	}

    /**
     * @return The date when the job was enqueued.
     */
	public String getDate() {
		return date;
	}

    /**
     * Sets the time (i.e. hh:mm:ss) when the job was enqueued.
     * @param time The string representation of the time.
     */
	public void setTime(String time) {
		this.time = time;
	}

    /**
     * @return The time when the job was enqueued.
     */
	public String getTime() {
		return time;
	}

    /**
     * @param stamp The UNIX timestamp.
     */
	public void setStamp(int stamp) {
		this.stamp = stamp;
	}

    /**
     * @return The UNIX timestamp.
     */
	public int getStamp() {
		return stamp;
	}

    /**
     * Get the job identity of the enqueued job. This is a convenience function.
     * @return The job identity object.
     */
    public JobIdentity getJobIdentity() {
        return new JobIdentity(jobID, result);
    }

    @Override
    public String toString() {
        return String.format("Job ID: %s\t= { Result=%s, Date=%s, Time=%s, Stamp=%s }",
                jobID, result, date, time, stamp);
    }
}
