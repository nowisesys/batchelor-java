/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DefaultStartPlugin.java
 *
 * Created: Aug 12, 2010, 1:28:58 AM
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
 * The default welcome and introduction plug-in.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class DefaultStartPlugin implements PluginInterface {

    private StartPluginPanel panel = new StartPluginPanel();
    private List<PluginType> list = new ArrayList<PluginType>();
    private final static String name = "StartPlugin";
    private final static String desc = "Shows welcome and introduction information when no web service connection has yet been established.";

    public DefaultStartPlugin() {
	list.add(PluginType.START);
    }

    @Override
    public String getName() {
	return name;
    }

    @Override
    public String getDescription() {
	return desc;
    }

    @Override
    public List<PluginType> provides() {
	return list;
    }

    @Override
    public boolean provides(PluginType type) {
	return type == PluginType.START;
    }

    @Override
    public void setService(WebServiceClient service) {
	// ignored
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
