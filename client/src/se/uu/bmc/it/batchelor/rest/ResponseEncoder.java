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
 * ResponseEncoder.java
 *
 * Created: Apr 16, 2009, 1:38:56 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public enum ResponseEncoder {

    /**
     * Don't request that the REST service uses a particular encoding method for its response
     * messages. Use the server default instead (XML or FOA).
     */
    DEFAULT,
    /**
     * Request that the REST service encode its reponse messages using the FOA encoding method.
     */
    FOA,
    /**
     * Request that the REST service encode its reponse messages using the XML encoding method.
     */
    XML

}
