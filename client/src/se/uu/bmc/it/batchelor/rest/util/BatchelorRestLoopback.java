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
 * BatchelorRestLoopback.java
 *
 * Created: Apr 16, 2009, 2:20:07 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.rest.util;

import se.uu.bmc.it.batchelor.rest.BatchelorRestClient;
import se.uu.bmc.it.batchelor.rest.ResponseEncoder;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is only intended to be used with the REST service at localhost. Its main purpose is
 * for unit testing of the REST service interface.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorRestLoopback extends BatchelorRestClient {

    /**
     * Creates an Batchelor REST service client using the REST service on localhost. The object
     * created is only useful for testing.
     *
     * @throws java.net.MalformedURLException The URL is invalid.
     */
    public BatchelorRestLoopback() throws MalformedURLException {
        super(new URL("http://localhost/batchelor/ws/rest/"), ResponseEncoder.DEFAULT);
    }
}
