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
 * BatchelorSoapLoopback.java
 *
 * Created: Apr 16, 2009, 2:09:33 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.soap.util;

import se.uu.bmc.it.batchelor.soap.BatchelorSoapClient;

/**
 * This class is only intended to be used with the SOAP service used at build
 * time. Its main purpose is for unit testing of the SOAP interface.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapLoopback extends BatchelorSoapClient {

    /**
     * Create an Batchelor SOAP client using the WSDL location at build time.
     * This constructor is only useful for communication with the web service
     * that JAX-WS has generated the client source from.
     *
     * Use this constructor if you like to consume the service at:
     * http://localhost:8080/BatchelorService/BatchelorSoapService?wsdl
     */
    public BatchelorSoapLoopback() {
        super(null, null);
    }
}
