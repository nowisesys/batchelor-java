/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeSelectionHandler.java
 *
 * Created: Oct 13, 2009, 2:27:57 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.tree;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class JobsTreeSelectionHandler implements TreeSelectionListener {
    
    private JTree tree;

    public JobsTreeSelectionHandler(JTree tree) {
        this.tree = tree;
    }

    public void valueChanged(TreeSelectionEvent event) {
    }

}
