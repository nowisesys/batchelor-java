/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ServiceTreeNode.java
 *
 * Created: Oct 13, 2009, 4:06:08 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import se.uu.bmc.it.batchelor.explorer.tree.*;
import se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;

import se.uu.bmc.it.batchelor.explorer.WebServiceClientType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;
import java.rmi.RemoteException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import se.uu.bmc.it.batchelor.EnqueueResult;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.QueueFilterResult;
import se.uu.bmc.it.batchelor.QueueSortResult;
import se.uu.bmc.it.batchelor.QueuedJob;
import se.uu.bmc.it.batchelor.rest.BatchelorRestClient;
import se.uu.bmc.it.batchelor.soap.BatchelorSoapClient;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class ServiceTreeNode extends AbstractJobsTreeNode {

    public ServiceTreeNode(WebServiceClient service) {
        super(service);
    }

    @Override
    public WebServiceClient getWebServiceClient() {
        return (WebServiceClient) getUserObject();
    }

    /**
     * Insert all remotelly queued jobs as child nodes in the tree.
     * @throws RemoteException
     */
    public void addChildNodes() throws RemoteException {
        WebServiceClient service = getWebServiceClient();
        List<QueuedJob> list = service.getService().queue(QueueSortResult.NONE, QueueFilterResult.ALL);
        for (QueuedJob job : list) {
            addChildNode(job);
        }
    }

    /**
     * Add a single queued job in the tree.
     * @param identity The job identity object.
     */
    private void addChildNode(JobIdentity identity) {
        addChildNode(new QueuedJob(identity));
    }

    /**
     * Add a single queued job in the tree.
     * @param job The enqueued job object.
     */
    private void addChildNode(QueuedJob job) {
            DefaultMutableTreeNode node = new QueuedJobTreeNode(job);
            node.add(new DefaultMutableTreeNode(""));  // Makes node appear as a branch node.
            add(node);
    }

    /**
     * Refresh the tree by removing all child nodes and then reading all
     * remotelly queued jobs from the web service and inserting them in the tree.
     * @throws RemoteException
     */
    public void refreshChildNodes() throws RemoteException {
        removeAllChildren();
        addChildNodes();
    }

    @Override
    public String toString() {
        return getWebServiceClient().getName();
    }

    /**
     * @return The context menu associated with the service tree node.
     */
    public JPopupMenu getContextMenu() {
        JMenuItem menuItem;
        JPopupMenu popup = new JPopupMenu();
        
        menuItem = popup.add(new JMenuItem("Disconnect"));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JobsTreeManager manager = JobsTreeManager.getManager();
                manager.clearTree();
            }
        });
        menuItem = popup.add(new JMenuItem("Submit..."));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JFrame frame = BatchelorExplorerApp.getApplication().getMainFrame();
                JFileChooser dialog = new JFileChooser();
                if (dialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    WebServiceClient service = getWebServiceClient();
                    if (service.getType() == WebServiceClientType.REST) {
                        try {
                            BatchelorRestClient client = (BatchelorRestClient) service.getService();
                            List<EnqueueResult> list = client.enqueue(dialog.getSelectedFile());
                            for (EnqueueResult result : list) {
                                addChildNode(result.getJobIdentity());
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        InputStreamReader stream = null;
                        try {
                            BatchelorSoapClient client = (BatchelorSoapClient) service.getService();
                            stream = new InputStreamReader(new FileInputStream(dialog.getSelectedFile()));
                            BufferedReader reader = new BufferedReader(stream);
                            StringBuilder input = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                input.append(line);
                            }
                            List<EnqueueResult> list = client.enqueue(input.toString());
                            for (EnqueueResult result : list) {
                                addChildNode(result.getJobIdentity());
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                if (stream != null) {
                                    stream.close();
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        });
        popup.addSeparator();
        menuItem = popup.add(new JMenuItem("Refresh"));
        menuItem.addActionListener(JobsTreeManager.getManager());

        return popup;
    }

//    /**
//     * @return The context menu associated with the service tree node.
//     */
//    public JPopupMenu getContextMenu() {
//        JobsTreeManager manager = JobsTreeManager.getManager();
//        return manager.getServiceContextMenu();
//    }
//
//    /**
//     * Create the shared popup (context) menu for all service tree nodes.
//     * @return The popup menu.
//     */
//    public static JPopupMenu createContextMenu() {
//        JMenuItem menuItem;
//        JPopupMenu popup = new JPopupMenu();
//        menuItem = popup.add(new JMenuItem("Disconnect"));
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent event) {
//                JobsTreeManager manager = JobsTreeManager.getManager();
//                manager.clearTree();
//            }
//        });
//        menuItem = popup.add(new JMenuItem("Submit..."));
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent event) {
//                JFrame frame = BatchelorExplorerApp.getApplication().getMainFrame();
//                JFileChooser dialog = new JFileChooser();
//                if (dialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
//                    JobsTreeManager manager = JobsTreeManager.getManager();
//                    ServiceTreeNode root = (ServiceTreeNode) manager.getRootNode();
//                    WebServiceClient service = root.getWebServiceClient();
//                    if (service.getType() == WebServiceClientType.REST) {
//                        try {
//                            BatchelorRestClient client = (BatchelorRestClient) service.getService();
//                            List<EnqueueResult> list = client.enqueue(dialog.getSelectedFile());
//                            for (EnqueueResult result : list) {
//                                root.addChildNode(result.getJobIdentity());
//                            }
//                        } catch (RemoteException ex) {
//                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (FileNotFoundException ex) {
//                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    } else {
//                        InputStreamReader stream = null;
//                        try {
//                            BatchelorSoapClient client = (BatchelorSoapClient) service.getService();
//                            stream = new InputStreamReader(new FileInputStream(dialog.getSelectedFile()));
//                            BufferedReader reader = new BufferedReader(stream);
//                            StringBuilder input = new StringBuilder();
//                            String line;
//                            while ((line = reader.readLine()) != null) {
//                                input.append(line);
//                            }
//                            List<EnqueueResult> list = client.enqueue(null);
//                            for (EnqueueResult result : list) {
//                                root.addChildNode(result.getJobIdentity());
//                            }
//                        } catch (RemoteException ex) {
//                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (FileNotFoundException ex) {
//                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (IOException ex) {
//                            Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        } finally {
//                            try {
//                                if (stream != null) {
//                                    stream.close();
//                                }
//                            } catch (IOException ex) {
//                                Logger.getLogger(ServiceTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//        popup.addSeparator();
//        menuItem = popup.add(new JMenuItem("Refresh"));
//        menuItem.addActionListener(JobsTreeManager.getManager());
//
//        return popup;
//    }

    /**
     * @return The icon for this tree node.
     */
    public Icon getIcon() {
        return JobsTreeManager.getManager().getIcon(JobsTreeNodeIconType.WEB_SERVICE);
    }
}
