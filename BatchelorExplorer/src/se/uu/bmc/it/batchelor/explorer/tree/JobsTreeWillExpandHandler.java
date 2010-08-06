/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeWillExpandHandler.java
 *
 * Created: Oct 13, 2009, 2:31:31 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.tree;

import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNode;
import javax.swing.JOptionPane;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.ExpandVetoException;
import javax.swing.JTree;
import java.rmi.RemoteException;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class JobsTreeWillExpandHandler implements TreeWillExpandListener {

    private JTree tree;

    public JobsTreeWillExpandHandler(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        try {
            JobsTreeNode node = (JobsTreeNode) event.getPath().getLastPathComponent();
            if (node.getChildCount() == 1 && node.getChildAt(0).toString().compareTo("") == 0) {
                node.remove(0);   // Remove empty child
            }
            if (node.getChildCount() == 0) {
                node.addChildNodes();
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                "Failed communicate with web service: " + e.getMessage(),
                "Remote Exception",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
    }
}
