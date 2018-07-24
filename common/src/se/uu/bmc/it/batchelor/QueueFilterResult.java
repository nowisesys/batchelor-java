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
 * QueueFilterResult.java
 *
 * Created: Apr 3, 2009, 11:41:01 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor;

/**
 * This enum can be used to map enum constants to string argument to be used as the filter argument
 * of the WebServiceInterface.queue() method.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public enum QueueFilterResult {

    /**
     * Don't filter at all.
     */
    ALL("all"),
    /**
     * Alias for QueueFilterResult.ALL
     */
    NONE("all"),
    /**
     * Return jobs waiting to be scheduled.
     */
    WAITING("waiting"),
    /**
     * Alias for QueueFilterResult.WAITING
     */
    UNFINISHED("waiting"),
    /**
     * Return jobs waiting for execution.
     */
    PENDING("pending"),
    /**
     * Return already running jobs.
     */
    RUNNING("running"),
    /**
     * Return jobs that finished execution.
     */
    FINISHED("finished"),
    /**
     * Alias for QueueFilterResult.FINISHED
     */
    SUCCESS("finished"),
    /**
     * Return jobs who has exited with warnings.
     */
    WARNING("warning"),
    /**
     * Return jobs who has exited in error state.
     */
    ERROR("error"),
    /**
     * Return jobs who has crashed.
     */
    CRASHED("crashed");

    /**
     * Creates the enum type.
     *
     * @param key The enum member value.
     */
    QueueFilterResult(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Get the value of the enum constant (i.e. "running").
     *
     * @return The enum constant value.
     */
    public String getValue() {
        return value;
    }
    private final String value;
}
