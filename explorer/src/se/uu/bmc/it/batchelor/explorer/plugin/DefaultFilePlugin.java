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
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginData;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginInterface;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;

/**
 * This class provides an file viewer plug-in.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class DefaultFilePlugin implements PluginInterface {

    private List<PluginType> list = new ArrayList<PluginType>();
    private FilePluginPanel panel = new FilePluginPanel();
    private final static String name = "FilePlugin";
    private final static String desc = "Show content of selected file.";

    public DefaultFilePlugin() {
	list.add(PluginType.FILE);
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
	return type == PluginType.FILE;
    }

    @Override
    public JComponent getComponent() {
	return panel;
    }

    @Override
    public void setData(PluginData data) {
	panel.setPluginData(data);
    }

    @Override
    public void setActive(boolean active) {
	panel.setActive(active);
    }
}
