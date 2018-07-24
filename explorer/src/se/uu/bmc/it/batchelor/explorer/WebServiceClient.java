/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WebServiceClient.java
 *
 * Created: Oct 12, 2009, 2:53:26 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer;

import java.net.URL;
import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.ContentHandlerFactory;
import java.util.Date;

import se.uu.bmc.it.batchelor.*;
import se.uu.bmc.it.batchelor.rest.*;
import se.uu.bmc.it.batchelor.soap.*;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class WebServiceClient {

    private WebServiceInterface service;
    private String name;
    private WebServiceClientType type;
    private URL url;
    private Date date;

    static {
	//
	// Installs content handlers for the text/xml and text/x-foa MIME types:
	//
	ContentHandlerFactory factory = ResponseDecoderFactory.getInstance();
	HttpURLConnection.setContentHandlerFactory(factory);
    }

    /**
     * Construct the web service client object. Use createWebServiceClient() instead
     * of using this constructor direct.
     * @param url The real URL of the web service.
     * @param name The associated service name.
     * @param type The web service type.
     */
    public WebServiceClient(URL url, String name, WebServiceClientType type) {
	this.date = new Date();
	this.name = name;
	this.type = type;
	this.url = url;

	if (type == WebServiceClientType.REST) {
	    service = new BatchelorRestClient(url);
	} else if (type == WebServiceClientType.SOAP) {
	    service = new BatchelorSoapClient(url);
	}
    }

    /**
     * @return Get the web service URL.
     */
    public URL getURL() {
	return url;
    }

    /**
     * @return The datetime for connection establishment.
     */
    public final Date getDate() {
	return date;
    }

    /**
     * @return Get the associated service name.
     */
    public String getName() {
	return name;
    }

    /**
     * @return Get the web service type.
     */
    public WebServiceClientType getType() {
	return type;
    }

    /**
     * @return Get the underlying web service object conforming to the
     * interface WebServiceInterface.
     */
    public WebServiceInterface getService() {
	return service;
    }

    /**
     * Factory method that creates and return the WebServiceClient object. The
     * location string is on form {rest|soap|http|https}://server.example.com/path/, where
     * the path is typical just batchelor.
     * @param name The service name (URL) using rest://, soap:// or http:// as protocol.
     * @return The WebServiceClient object.
     * @throws MalformedURLException
     */
    public static WebServiceClient createWebServiceClient(String name) throws MalformedURLException {
	if (name.startsWith("http://") || name.startsWith("https://")) {
	    if (name.indexOf("/ws/rest") != -1) {
		return new WebServiceClient(new URL(name), name, WebServiceClientType.REST);
	    } else if (name.indexOf("/ws/soap") != -1) {
		return new WebServiceClient(new URL(name), name, WebServiceClientType.SOAP);
	    }
	} else if (name.startsWith("rest://")) {
	    String url = "http://" + name.substring(7) + "/ws/rest";
	    return new WebServiceClient(new URL(url), name, WebServiceClientType.REST);
	} else if (name.startsWith("soap://")) {
	    String url = "http://" + name.substring(7) + "/ws/schema/wsdl/?wsdl";
	    return new WebServiceClient(new URL(url), name, WebServiceClientType.SOAP);
	}
	return null;
    }

    @Override
    public String toString() {
	return name;
    }
}
