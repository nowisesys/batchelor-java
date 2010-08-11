/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.uu.bmc.it.batchelor.explorer.plugin.service;

import java.util.ServiceLoader;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginInterface;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;

/**
 * Uses ServiceLoader to load and create plug-in (service provider) object
 * instances that implements the Plugin interface. These plug-ins classes are
 * either found in the application jar or inside external jar-files appended
 * to the class-path.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class PluginService {

    private PluginService() {
	plugins = ServiceLoader.load(PluginInterface.class);
    }

    public static synchronized PluginService getInstance() {
	return PluginServiceHolder.INSTANCE;
    }

    public synchronized PluginInterface getPlugin(PluginType type) {
	for(PluginInterface plugin : plugins) {
	    if(plugin.provides(type)) {
		return plugin;
	    }
	}
	return null;
    }

    private static class PluginServiceHolder {

	private static final PluginService INSTANCE = new PluginService();
    }
    private ServiceLoader<PluginInterface> plugins;
}
