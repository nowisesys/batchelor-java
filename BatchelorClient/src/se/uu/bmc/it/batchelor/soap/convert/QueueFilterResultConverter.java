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
 * QueueFilterResultConverter.java
 *
 * Created: May 4, 2009, 7:26:32 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.soap.convert;

import se.uu.bmc.it.batchelor.soap.schema.QueueFilterResult;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class QueueFilterResultConverter {

    public static QueueFilterResult getQueueFilterResult(se.uu.bmc.it.batchelor.QueueFilterResult in) {
        if(in == null) {
            return QueueFilterResult.ALL;
        }
        return QueueFilterResult.fromValue(in.name());
    }
}
