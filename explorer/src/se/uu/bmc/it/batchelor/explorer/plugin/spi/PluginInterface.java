/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.uu.bmc.it.batchelor.explorer.plugin.spi;

import java.util.List;
import javax.swing.JComponent;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;

/*
 * PluginInterface.java
 *
 * Created: Aug 11, 2010, 2:00:14 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
/**
 * This is the SPI (service provider interface) that plug-ins should implement.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public interface PluginInterface {

    /**
     * @return The plug-in name.
     */
    String getName();

    /**
     * @return The plug-in description.
     */
    String getDescription();

    /**
     * @return A list of all plug-in types that this plug-in can handle.
     */
    List<PluginType> provides();

    /**
     * @param type The plug-in type to check.
     * @return True if this plug-in handles this type.
     */
    boolean provides(PluginType type);

    /**
     * Set the relevant data for this plug-in. The data object will contains
     * those properties required by the type of plug-in.
     * @param data The relevant data for this plug-in.
     */
    void setData(PluginData data);

    /**
     * Called to get the user interface of this plug-in for embedding in the
     * right hand side of the application (the visualizing area).
     * @return The user interface of the plug-in.
     */
    JComponent getComponent();

    /**
     * This method gets called to tell the plug-in to activate or deactivate
     * itself. It can be used to cancel pending I/O or release resources.
     *
     * This method is called after its embedded (if activated) and before its
     * going to be replaced by another plug-in.
     *
     * @param active True if plug-in is activated.
     */
    void setActive(boolean active);
}
