/*
 * PluginData.java
 *
 * Created: Aug 17, 2010, 12:09:50 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.plugin.spi;

import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;

/**
 * Tree node data to be used by the plug-in. Instances of this class is created
 * by the tree nodes and passed on to the plug-ins.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class PluginData {
    
    private final WebServiceClient service;
    private final JobIdentity job;
    private final String path;

    /**
     * Constructs the plug-in data object.
     * @param service The web service client object.
     * @param job The queued job identity.
     * @param path The remote file or directory (relative the job root).
     */
    public PluginData(WebServiceClient service, JobIdentity job, String path) {
	this.service = service;
	this.job = job;
	this.path = path;
    }

    /**
     * Constructs the plug-in data object with unset path.
     * @param service The web service client object.
     * @param job The queued job identity.
     */
    public PluginData(WebServiceClient service, JobIdentity job) {
	this(service, job, null);
    }

    /**
     * Construct the plug-in data object with unset job identity and path.
     * @param service The web service client object.
     */
    public PluginData(WebServiceClient service) {
	this(service, null, null);
    }

    /**
     * @return The web service instance.
     */
    public WebServiceClient getService() {
	return service;
    }

    /**
     * @return The path of the remote file or directory relative the queued 
     * jobs root. This member might be null for some plug-ins and tree node
     * types (typical service and queued job nodes).
     */
    public String getPath() {
	return path;
    }

    /**
     * @return The job identity.
     */
    public JobIdentity getJobIdentity() {
	return job;
    }

}
