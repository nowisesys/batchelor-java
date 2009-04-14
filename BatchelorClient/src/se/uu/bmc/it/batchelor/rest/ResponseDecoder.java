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
 * ResponseDecoder.java
 *
 * Created: Apr 3, 2009, 10:30:15 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 *
 * Batchelors REST service can send the response encoded in two formats,
 * either as XML or as FOA. By using an interface we can abstact away the
 * details and let us concentrate on the logic inside the RestWebService class.
 */

package se.uu.bmc.it.batchelor.rest;

/**
 * The interface for response decoding plugin classes to implement.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public interface ResponseDecoder {

}
