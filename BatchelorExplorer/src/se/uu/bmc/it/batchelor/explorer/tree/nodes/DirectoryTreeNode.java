/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DirectoryTreeNode.java
 *
 * Created: Oct 14, 2009, 3:24:45 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;
import se.uu.bmc.it.batchelor.explorer.tree.*;
import java.rmi.RemoteException;
import javax.swing.Icon;
import javax.swing.JPopupMenu;

/**
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class DirectoryTreeNode extends AbstractJobsTreeNode {

    public DirectoryTreeNode(Object obj) {
        super(obj);
    }

    @Override
    public void addChildNodes() throws RemoteException {
        // Ignore
    }

    @Override
    public void refreshChildNodes() throws RemoteException {
        // Ignore
    }

    /**
     * @return This function returns null always becasue theres no valid 
     * operations to perform on directory tree nodes.
     */
    @Override
    public JPopupMenu getContextMenu() {
        return null;
    }

    /**
     * @return The icon for this tree node.
     */
    @Override
    public Icon getIcon() {
        return JobsTreeManager.getManager().getIcon(JobsTreeNodeIconType.DIRECTORY);
    }

    @Override
    public PluginType getPluginType() {
	return PluginType.DIRECTORY;
    }

}
