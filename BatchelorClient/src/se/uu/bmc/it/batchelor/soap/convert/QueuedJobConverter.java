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
 * QueuedJobConverter.java
 *
 * Created: Apr 23, 2009, 2:59:04 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.soap.convert;

import se.uu.bmc.it.batchelor.soap.schema.QueuedJob;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class QueuedJobConverter {

    public static se.uu.bmc.it.batchelor.QueuedJob createQueuedJob(QueuedJob in) {
        se.uu.bmc.it.batchelor.JobIdentity job = JobIdentityConverter.createJobIdentity(in.getJobIdentity());
        return new se.uu.bmc.it.batchelor.QueuedJob(job, in.getState());
    }
}
