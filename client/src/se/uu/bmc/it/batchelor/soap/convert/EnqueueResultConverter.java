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
 * EnqueueResultConverter.java
 *
 * Created: Apr 23, 2009, 1:05:46 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.soap.convert;

import se.uu.bmc.it.batchelor.soap.schema.EnqueueResult;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class EnqueueResultConverter {

    public static se.uu.bmc.it.batchelor.EnqueueResult createEnqueueResult(EnqueueResult in) {
        if (in == null) {
            return new se.uu.bmc.it.batchelor.EnqueueResult();
        } else {
            return new se.uu.bmc.it.batchelor.EnqueueResult(
                    in.getJobID(), in.getResult(), in.getDate(), in.getTime(), in.getStamp());
        }
    }
}
