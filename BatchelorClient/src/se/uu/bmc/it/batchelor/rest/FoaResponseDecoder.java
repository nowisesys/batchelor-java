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
 * FoaResponseDecoder.java
 *
 * Created: Apr 3, 2009, 10:37:16 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.rest;

import java.net.ContentHandler;
import java.net.URLConnection;

/**
 * This class extends the ContentHandler class to decoding FOA encoded response
 * messages from the Batchelor REST service.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 * @see ResponseDecoder
 * @see <a href="http://it.bmc.uu.se/andlov/proj/libfoa">FOA</a>
 */
public class FoaResponseDecoder extends ContentHandler {

    /**
     * Get next FOA encoded object from input stream (contained in the URL
     * connection).
     * @param connection The URL connection to read from.
     * @return The next object from decoded FOA encoded response message.
     */
    public Object getContent(URLConnection connection) {
        // TODO: implement this function to decode FOA encoded objects from the stream and return them as String, EnqueueResult, JobIdentity or QueuedJob objects.
        return null;
    }
}
