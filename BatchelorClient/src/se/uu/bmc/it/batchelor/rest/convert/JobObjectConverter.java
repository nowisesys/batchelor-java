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
 * JobObjectConverter.java
 *
 * Created: Apr 22, 2009, 10:59:02 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest.convert;

import se.uu.bmc.it.batchelor.rest.schema.Job;
import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueuedJob;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class JobObjectConverter {

    public static EnqueueResult createEnqueueResult(Job job) {
        EnqueueResult result = new EnqueueResult(
            job.getJobid(),
            job.getResult(),
            job.getDate(),
            job.getTime(),
            job.getStamp());
        return result;
    }

    public static JobIdentity createJobIdentity(Job job) {
        JobIdentity result = new JobIdentity(
            job.getJobid(),
            job.getResult());
        return result;
    }

    public static QueuedJob createQueuedJob(Job job) {
        return new QueuedJob(createJobIdentity(job), job.getState());
    }
}
