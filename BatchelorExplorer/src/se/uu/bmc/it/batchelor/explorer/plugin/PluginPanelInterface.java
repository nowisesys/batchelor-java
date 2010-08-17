/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.uu.bmc.it.batchelor.explorer.plugin;

import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginData;

/*
 * PluginPanelInterface.java
 *
 * Created: Aug 11, 2010, 5:29:14 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

/**
 * TODO: add description for the interface.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public interface PluginPanelInterface {

    void setActive(boolean active);

    void setPluginData(PluginData data);

}
