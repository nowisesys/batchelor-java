/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * QueuedJobTreeNode.java
 *
 * Created: Oct 13, 2009, 2:13:48 AM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import se.uu.bmc.it.batchelor.explorer.tree.*;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.JobIdentity;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class QueuedJobTreeNode extends AbstractJobsTreeNode {

    public QueuedJobTreeNode(QueuedJob job) {
        super(job);
    }

    public QueuedJob getQueuedJob() {
        return (QueuedJob) getUserObject();
    }

    @Override
    public String toString() {
        return String.format("%d", getQueuedJob().getJobIdentity().getResult());
    }

    public void refreshChildNodes() throws RemoteException {
        this.removeAllChildren();
        addChildNodes();
    }

    public void addChildNodes() throws RemoteException {
        WebServiceClient service = getWebServiceClient();
        JobIdentity identity = getQueuedJob().getJobIdentity();

        List<String> list = service.getService().readdir(identity);

        for (String file : list) {
            TreePath path = new TreePath(file.split("/"));
            addFileNode(this, path.getParentPath(), (String) path.getLastPathComponent(), file, identity);
        }
    }

    /**
     * Add remote file node at path relative to parent node.
     * @param parent The parent node for the remote file node.
     * @param path The tree path relative parent.
     * @param file The file name.
     * @param identity The job identity.
     */
    private void addFileNode(TreeNode parent, TreePath path, String name, String file, JobIdentity identity) {
        TreeNode node = createChildNodes(this, path);
        ((DefaultMutableTreeNode) node).add(new RemoteFileTreeNode(name, file, identity));
    }

    /**
     * This function creates the directory nodes under parent based on path. The
     * nodes is only created if they don't exist.
     * @param parent The parent node of the directory nodes.
     * @param path The tree path relative parent.
     * @return Returns the tree node corresponding to the tree path.
     */
    private TreeNode createChildNodes(TreeNode parent, TreePath path) {
        if (path != null && path.getPath() != null) {
            for (Object obj : path.getPath()) {
                TreeNode child = getChildNode(parent, (String) obj);
                if (child == null) {
                    DirectoryTreeNode node = new DirectoryTreeNode(obj);
                    ((DefaultMutableTreeNode) parent).add(node);
                    parent = node;
                } else {
                    parent = child;
                }
            }
        }
        return parent;
    }

    /**
     * An helper function that checks if the name is a child node of parent.
     * @param parent The parent node.
     * @param name The name of the child node to find.
     * @return The child tree node or null if not found.
     */
    private TreeNode getChildNode(TreeNode parent, String name) {
        for (int i = 0; i < parent.getChildCount(); ++i) {
            if (parent.getChildAt(i).toString().compareTo(name) == 0) {
                return parent.getChildAt(i);
            }
        }
        return null;
    }

    /**
     * @return The context menu associated with this queued job tree node.
     */
    public JPopupMenu getContextMenu() {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;

//        JobsTreeManager manager = JobsTreeManager.getManager();
//        QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
        String state = getQueuedJob().getState();

        menuItem = popup.add(new JMenuItem("Suspend"));
        menuItem.setEnabled(state.compareTo("running") == 0);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
                    WebServiceClient service = getWebServiceClient();
                    service.getService().suspend(getQueuedJob().getJobIdentity());
                } catch (RemoteException ex) {
                    Logger.getLogger(QueuedJobTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuItem = popup.add(new JMenuItem("Resume"));
        menuItem.setEnabled(state.compareTo("running") == 0);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
                    WebServiceClient service = getWebServiceClient();
                    service.getService().resume(getQueuedJob().getJobIdentity());
                } catch (RemoteException ex) {
                    Logger.getLogger(QueuedJobTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuItem = popup.add(new JMenuItem("Delete"));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                try {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
//                    ServiceTreeNode root = (ServiceTreeNode) manager.getRootNode();
                    WebServiceClient service = getWebServiceClient();
                    service.getService().dequeue(getQueuedJob().getJobIdentity());
                } catch (RemoteException ex) {
                    Logger.getLogger(QueuedJobTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        popup.add(new JSeparator());
        menuItem = popup.add(new JMenuItem("Refresh"));
        menuItem.addActionListener(JobsTreeManager.getManager());

        return popup;
    }

//    /**
//     * @return The context menu associated with queued job tree nodes.
//     */
//    public JPopupMenu getContextMenu() {
//        JobsTreeManager manager = JobsTreeManager.getManager();
//        return manager.getQueuedJobContextMenu();
//    }
//
//    public static JPopupMenu createContextMenu() {
//        JPopupMenu popup = new JPopupMenu();
//        JMenuItem menuItem;
//
//        menuItem = popup.add(new JMenuItem("Suspend"));
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent event) {
//                try {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
//                    WebServiceClient service = node.getWebServiceClient();
//                    service.getService().suspend(node.getQueuedJob().getJobIdentity());
//                } catch (RemoteException ex) {
//                    Logger.getLogger(QueuedJobTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        menuItem = popup.add(new JMenuItem("Resume"));
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent event) {
//                try {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
//                    WebServiceClient service = node.getWebServiceClient();
//                    service.getService().resume(node.getQueuedJob().getJobIdentity());
//                } catch (RemoteException ex) {
//                    Logger.getLogger(QueuedJobTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        menuItem = popup.add(new JMenuItem("Delete"));
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent event) {
//                try {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    QueuedJobTreeNode node = (QueuedJobTreeNode) manager.getSelectedNode();
//                    ServiceTreeNode root = (ServiceTreeNode) manager.getRootNode();
//                    WebServiceClient service = root.getWebServiceClient();
//                    service.getService().dequeue(node.getQueuedJob().getJobIdentity());
//                } catch (RemoteException ex) {
//                    Logger.getLogger(QueuedJobTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        popup.add(new JSeparator());
//        menuItem = popup.add(new JMenuItem("Refresh"));
//        menuItem.addActionListener(JobsTreeManager.getManager());
//
//        return popup;
//    }
    /**
     * @return The icon for this tree node.
     */
    public Icon getIcon() {
        JobsTreeManager manager = JobsTreeManager.getManager();
        String state = getQueuedJob().getState();
        if (state.compareTo("finished") == 0) {
            return manager.getIcon(JobsTreeNodeIconType.JOB_SUCCESS);
        } else if (state.compareTo("warning") == 0) {
            return manager.getIcon(JobsTreeNodeIconType.JOB_WARNING);
        } else if (state.compareTo("pending") == 0) {
            return manager.getIcon(JobsTreeNodeIconType.JOB_PENDING);
        } else if (state.compareTo("running") == 0) {
            return manager.getIcon(JobsTreeNodeIconType.JOB_RUNNING);
        } else if (state.compareTo("error") == 0) {
            return manager.getIcon(JobsTreeNodeIconType.JOB_ERROR);
        } else if (state.compareTo("crashed") == 0) {
            return manager.getIcon(JobsTreeNodeIconType.JOB_CRASHED);
        } else {
            return null;
        }
    }
}
