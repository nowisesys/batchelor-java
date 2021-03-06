/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeManager.java
 *
 * Created: Oct 13, 2009, 12:31:02 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.tree;

import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNode;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNodeIconType;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.rmi.RemoteException;

import java.awt.event.ActionListener;
import javax.swing.Icon;

/**
 * This class encapsulate the tree view management, thus making the main frame
 * UI code more clean.
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class JobsTreeManager implements ActionListener {

    private JTree tree;
    private static JobsTreeManager manager = null;

    /**
     * @return Singleton instance of this class.
     */
    public static JobsTreeManager getManager() {
        return manager;
    }

    /**
     * Set the active tree manager.
     * @param manager The tree manager.
     * @see getManager().
     */
    public static void setManager(JobsTreeManager manager) {
        JobsTreeManager.manager = manager;
    }

    /**
     * Construct a tree manager for the tree.
     * @param tree The tree object.
     */
    public JobsTreeManager(JTree tree) {
        
        this.tree = tree;
        this.tree.addTreeSelectionListener(new JobsTreeSelectionHandler(tree));
        this.tree.addTreeWillExpandListener(new JobsTreeWillExpandHandler(tree));

        setRootNode(null);   // Clear tree nodes
    }

    public void clearTree() {
        tree.removeAll();
        setRootNode(null);
    }

    /**
     * Set the active tree root node. If root is null, then the tree gets emptied.
     * @param node The root node for the tree.
     */
    public final void setRootNode(TreeNode node) {
        ((DefaultTreeModel) tree.getModel()).setRoot(node);
    }

    /**
     * @return The current root node for the tree.
     */
    public TreeNode getRootNode() {
        return (TreeNode) tree.getModel().getRoot();
    }

    /**
     * @return Last selected tree node.
     */
    public TreeNode getSelectedNode() {
        return (TreeNode) tree.getSelectionPath().getLastPathComponent();
    }

    //
    // Called to refresh the tree.
    // 
    public void actionPerformed(ActionEvent event) {
        TreePath path = tree.getSelectionPath();
        if (path != null) {
            try {
                JobsTreeNode node = (JobsTreeNode) path.getLastPathComponent();
                node.refreshChildNodes();
                ((DefaultTreeModel) tree.getModel()).reload(node);
            } catch (RemoteException ex) {
                Logger.getLogger(JobsTreeManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Icon getIcon(JobsTreeNodeIconType type) {
        JobsTreeCellRenderer renderer = (JobsTreeCellRenderer)tree.getCellRenderer();
        return renderer.getIcon(type);
    }

}
