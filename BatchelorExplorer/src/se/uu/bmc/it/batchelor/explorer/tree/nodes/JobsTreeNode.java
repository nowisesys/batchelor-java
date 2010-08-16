/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeNode.java
 *
 * Created: Oct 13, 2009, 4:04:43 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import se.uu.bmc.it.batchelor.explorer.WebServiceClient;
import java.rmi.RemoteException;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.tree.MutableTreeNode;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;

/**
 * Interface for all tree node types in the jobs tree.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public interface JobsTreeNode extends MutableTreeNode {

    /**
     * Load child nodes.
     * @throws RemoteException
     */
    void addChildNodes() throws RemoteException;

    /**
     * Reload all child nodes.
     * @throws RemoteException
     */
    void refreshChildNodes() throws RemoteException;

    /** 
     * @return The web service client object.
     */
    WebServiceClient getWebServiceClient();

    /**
     * @return The context menu associated with this tree node type.
     */
    JPopupMenu getContextMenu();

    /**
     * @return The service provider type for this tree node type.
     */
    PluginType getPluginType();
    
    /**
     * @return The icon for this tree node.
     */
    Icon getIcon();
}
