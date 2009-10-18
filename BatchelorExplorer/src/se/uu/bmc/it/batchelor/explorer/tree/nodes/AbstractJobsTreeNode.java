/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AbstractJobsTreeNode.java
 *
 * Created: Oct 18, 2009, 12:52:03 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */

package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import se.uu.bmc.it.batchelor.explorer.WebServiceClient;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public abstract class AbstractJobsTreeNode extends DefaultMutableTreeNode implements JobsTreeNode {

    protected AbstractJobsTreeNode(Object obj) {
        super(obj);
    }

    public WebServiceClient getWebServiceClient() {
        return ((ServiceTreeNode) getRoot()).getWebServiceClient();
    }
}
