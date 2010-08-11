/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DefaultFilePlugin.java
 *
 * Created: Aug 11, 2010, 2:08:56 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.plugin;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginInterface;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;

/**
 * This class provides an file viewer plug-in.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class DefaultResultPlugin implements PluginInterface {

    private ResultPluginPanel panel = new ResultPluginPanel();
    private List<PluginType> list = new ArrayList<PluginType>();
    private final static String name = "ResultPlugin";
    private final static String desc = "A drop in replacement for a real result viewer that should be provided by the application.";

    public DefaultResultPlugin() {
	list.add(PluginType.RESULT);
    }

    @Override
    public final String getName() {
	return name;
    }

    @Override
    public final String getDescription() {
	return desc;
    }

    @Override
    public final List<PluginType> provides() {
	return list;
    }

    @Override
    public boolean provides(PluginType type) {
	return type == PluginType.RESULT;
    }

    @Override
    public void setService(WebServiceClient service) {
	panel.setService(service);
    }

    @Override
    public JComponent getComponent() {
	return panel;
    }

    @Override
    public void setActive(boolean active) {
	panel.setActive(active);
    }
}
