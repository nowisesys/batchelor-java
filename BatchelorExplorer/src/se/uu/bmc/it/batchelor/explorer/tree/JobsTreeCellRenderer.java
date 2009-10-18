/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JobsTreeCellRenderer.java
 *
 * Created: Oct 16, 2009, 2:21:00 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.tree;

import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNodeIcon;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNode;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNodeIconType;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class JobsTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

    private HashMap<JobsTreeNodeIconType, Icon> map = new HashMap<JobsTreeNodeIconType, Icon>();

    public void addIcon(JobsTreeNodeIcon icon) {
        map.put(icon.getType(), icon.getIcon());
    }

    public Icon getIcon(JobsTreeNodeIconType type) {
        return map.get(type);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,
        Object value,
        boolean selected,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        try {
            JobsTreeNode node = (JobsTreeNode) value;
            setIcon(node.getIcon());
        } catch (ClassCastException e) {
        }
        return this;
    }
}
