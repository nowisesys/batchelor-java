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
 * LinkObjectConverter.java
 *
 * Created: Apr 27, 2009, 12:33:09 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest.convert;

import se.uu.bmc.it.batchelor.rest.schema.Link;
import se.uu.bmc.it.batchelor.JobIdentity;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class LinkObjectConverter {

    /**
     * Extract the job identity from the link object. A link object from an
     * result request has '/result/jobid' as its last components of the href.
     * @param link The result link object.
     * @return The job identity object.
     */
    public static JobIdentity createJobIdentity(Link link) {
        int posj = link.getHref().lastIndexOf('/');
        int posr = link.getHref().lastIndexOf('/', posj - 1);
        String jobID = link.getHref().substring(posj + 1);
        String result = link.getHref().substring(posr + 1, posj);
        return new JobIdentity(jobID, Integer.parseInt(result));
    }

    /**
     * Extract the relative file path of the link object given the job identity
     * object.
     * @param link The link object to extract file path from.
     * @param job The job identity to search for.
     * @return The relative file path.
     */
    public static String createFilePath(Link link, JobIdentity job) {
        String find = String.format("%d/%s", job.getResult(), job.getJobID());
        int pos = link.getHref().indexOf(find);
        return link.getHref().substring(pos + find.length());
    }
}
