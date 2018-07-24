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
package se.uu.bmc.it.batchelor.soap;

import java.net.URL;
import java.net.MalformedURLException;
import se.uu.bmc.it.batchelor.BatchelorWebServiceClientTest;

/**
 * Unit test against a real server installation of Batchelor. The server installation must have SOAP
 * support enabled. Set a valid URL for the WSDL location (in the SERVICE string) before starting
 * the test.
 *
 * This test depends on that starting a job makes it run for at least the time it takes to complete
 * this test module. Add some sleep(1) calls in utils/script.sh (in the Batchelor installation) to
 * ensure the job don't terminate too soon. The suspend and resume test will fail unless job control
 * is enabled in the Batchelor configuration.
 *
 * Make sure that the indata passed by enqueue() is valid for the remote SERVICE, or all sub sequent
 * tests depending on enqueued job will fail.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class BatchelorSoapRemoteTest extends BatchelorWebServiceClientTest {

    private static final String SERVICE = "http://localhost/batchelor1/ws/schema/wsdl/?wsdl";

    public BatchelorSoapRemoteTest() throws MalformedURLException {
        super(new BatchelorSoapClient(new URL(SERVICE)));
    }
}
