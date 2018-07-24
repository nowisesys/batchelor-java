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
 * QueuedJob.java
 *
 * Created: Apr 3, 2009, 09:04:45 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor;

import java.io.Serializable;

/**
 * This class represent an already enqueued job as read from the queue.
 *
 * @author Anders Lövgren
 */
public class QueuedJob implements Serializable {

    // TODO: Should we return more job attributes (like name and date/time) in the QueuedJob object?
    private JobIdentity ident;
    private String state;

    /**
     * Creates an object using default job identity and null as state.
     */
    public QueuedJob() {
        ident = new JobIdentity();
        state = null;
    }

    /**
     * Creates an object using the job identity.
     *
     * @param ident The job identity.
     */
    public QueuedJob(JobIdentity ident) {
        this.ident = ident;
        this.state = null;
    }

    /**
     * Creates an object using job ident jobIdentity and also setting the queued jobs state.
     *
     * @param ident The job ident.
     * @param state The state of the queued job (i.e. running or finished).
     */
    public QueuedJob(JobIdentity ident, String state) {
        this.ident = ident;
        this.state = state;
    }

    /**
     * @param ident The job identity.
     */
    public void setJobIdentity(JobIdentity ident) {
        this.ident = ident;
    }

    /**
     * @return The job identity.
     */
    public JobIdentity getJobIdentity() {
        return ident;
    }

    /**
     * Set the job state (like running, pending, ...)
     *
     * @param state The job state.
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return The job state.
     */
    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return String.format("Job ID: %s\t= { Result=%s, State=%s }",
                ident.getJobID(), ident.getResult(), state);
    }
}
