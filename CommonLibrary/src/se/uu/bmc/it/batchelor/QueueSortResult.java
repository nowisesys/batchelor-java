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
 * QueueSortResult.java
 *
 * Created: Apr 3, 2009, 11:40:42 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor;

/**
 * This enum can be used to map enum constants to string argument to be used
 * as the sort argument of the WebServiceInterface.queue() method.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public enum QueueSortResult {

    /**
     * Don't sort at all.
     */
    NONE("none"),
    /**
     * Sort result on when jobs where started.
     */
    STARTED("started"),
    /**
     * Sort result on job ID's.
     */
    JOB_ID("jobid"),
    /**
     * Sort on jobs state (running, pending, finished, ...).
     */
    STATUS("state"),
    /**
     * Alias for QueueSortResult.STATUS
     */
    STATE("state"),
    /**
     * Sort on jobs name (might not be implemented on the server side).
     */
    NAME("name");

    /**
     * Creates the enum type.
     * @param value The enum value.
     */
    QueueSortResult(String value) {
        this.value = value;
    }

    /**
     * Get the value of the enum constant (i.e. "jobid").
     * @return The enum constant value.
     */
    public String getValue() {
        return value;
    }
    private String value;
}
