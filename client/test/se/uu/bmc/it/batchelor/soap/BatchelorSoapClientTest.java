/*
 * Java library for Batchelor (batch job queue)
 * Copyright (C) 2009-2018 Anders Lövgren
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
package se.uu.bmc.it.batchelor.soap;

import se.uu.bmc.it.batchelor.soap.util.BatchelorSoapLoopback;
import se.uu.bmc.it.batchelor.BatchelorWebServiceClientTest;

/**
 * Unit test against local deployed dummy service (BatchelorSoapService). This class emulates calls
 * against the real SOAP service and tests that the client code behaves as expected (regarding
 * remote exceptions and expected returns).
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapClientTest extends BatchelorWebServiceClientTest {

    public BatchelorSoapClientTest() {
        super(new BatchelorSoapLoopback());
    }

}
