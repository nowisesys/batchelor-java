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

import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginData;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;
import se.uu.bmc.it.batchelor.explorer.tree.*;
import java.rmi.RemoteException;
import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreeNode;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;

/**
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class DirectoryTreeNode extends AbstractJobsTreeNode {

    private JobIdentity identity;
    private String file;

    public DirectoryTreeNode(String name, JobIdentity identity) {
	super(name);
	this.identity = identity;
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
     * @return The display name.
     */
    public String getName() {
	return (String) getUserObject();
    }

    /**
     * @return The remote directory path.
     */
    public String getFile() {
	if (file == null) {
	    TreeNode node = getParent();
	    file = (String) getUserObject();
	    while (node instanceof DirectoryTreeNode) {
		file = String.format("%s/%s", node.toString(), file);
		node = node.getParent();
	    }
	}
	return file;
    }

    /**
     * @return The associated job identity.
     */
    public JobIdentity getJobIdentity() {
	return identity;
    }

    /**
     * @return This function returns null always because there is no valid
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

    @Override
    public PluginData getPluginData() {
	WebServiceClient service = getWebServiceClient();

	PluginData data = new PluginData(service, identity, getFile());
	return data;
    }
}
