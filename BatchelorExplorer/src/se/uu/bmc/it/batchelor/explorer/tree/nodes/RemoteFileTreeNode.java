/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RemoteFileTreeNode.java
 *
 * Created: Oct 13, 2009, 3:34:14 PM
 * Author:  Anders Lövgren (QNET/BMC CompDept)
 */
package se.uu.bmc.it.batchelor.explorer.tree.nodes;

import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;
import se.uu.bmc.it.batchelor.explorer.tree.*;
import se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;
import se.uu.bmc.it.batchelor.explorer.WebServiceClientType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import se.uu.bmc.it.batchelor.JobIdentity;
import se.uu.bmc.it.batchelor.rest.BatchelorRestClient;
import se.uu.bmc.it.batchelor.soap.BatchelorSoapClient;

/**
 * This class encapsulate an remote file in a tree node.
 * 
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class RemoteFileTreeNode extends AbstractJobsTreeNode {

    private JobIdentity identity;
    private String file;

    /**
     * Create the remote file node object.
     * @param name The display name.
     * @param file The remote file name.
     * @param identity The nodes associated job identity.
     */
    public RemoteFileTreeNode(String name, String file, JobIdentity identity) {
	super(name);
	this.file = file;
	this.identity = identity;
    }

    /**
     * @return The display name.
     */
    public String getName() {
	return (String) getUserObject();
    }

    /**
     * @return The remote file path.
     */
    public String getFile() {
	return file;
    }

    /**
     * @return The associated job identity.
     */
    public JobIdentity getJobIdentity() {
	return identity;
    }

    /**
     * This method do nothing because file nodes are always leaf nodes.
     * @throws RemoteException
     */
    @Override
    public void addChildNodes() throws RemoteException {
	// Objects of this class is always a leaf nodes.
    }

    @Override
    public void refreshChildNodes() throws RemoteException {
	// Ignore
    }

    /**
     * @return The context menu associated with this remote file node.
     */
    @Override
    public JPopupMenu getContextMenu() {
	JPopupMenu popup = new JPopupMenu();
	JMenuItem menuItem;

	menuItem = popup.add(new JMenuItem("Download..."));
	menuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
//                JobsTreeManager manager = JobsTreeManager.getManager();
//                RemoteFileTreeNode node = (RemoteFileTreeNode) manager.getSelectedNode();

		JFrame frame = BatchelorExplorerApp.getApplication().getMainFrame();
		JFileChooser dialog = new JFileChooser();
		dialog.setSelectedFile(new File(file));

		if (dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
//                    ServiceTreeNode root = (ServiceTreeNode) manager.getRootNode();
//                    root = (ServiceTreeNode) manager.getRootNode();

		    WebServiceClient service = getWebServiceClient();
		    if (service.getType() == WebServiceClientType.REST) {
			try {
			    BatchelorRestClient client = (BatchelorRestClient) service.getService();
			    client.fopen(identity, file, dialog.getSelectedFile());
			} catch (RemoteException ex) {
			    Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
			} catch (FileNotFoundException ex) {
			    Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
			    Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
			}
		    } else if (service.getType() == WebServiceClientType.SOAP) {
			{
			    OutputStream stream = null;
			    try {
				BatchelorSoapClient client = (BatchelorSoapClient) service.getService();
				byte[] bytes = client.fopen(identity, file);
				stream = new FileOutputStream(dialog.getSelectedFile());
				stream.write(bytes);
			    } catch (FileNotFoundException ex) {
				Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
			    } catch (RemoteException ex) {
				Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
			    } catch (IOException ex) {
				Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
			    } finally {
				try {
				    stream.close();
				} catch (IOException ex) {
				    Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
				}
			    }
			}
		    }
		}
	    }
	});

	menuItem = popup.add(new JMenuItem("Delete"));
	menuItem.setEnabled(false);
	menuItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		// TODO: add unlink method to batchelor web service API.
		throw new UnsupportedOperationException("Not supported yet.");
	    }
	});
	return popup;
    }

//    /**
//     * @return The context menu associated with remote file tree nodes.
//     */
//    public JPopupMenu getContextMenu() {
//        JobsTreeManager manager = JobsTreeManager.getManager();
//        return manager.getFileContextMenu();
//    }
//
//    /**
//     * @return The context (popup) menu for remote file nodes in the tree.
//     */
//    public static JPopupMenu createContextMenu() {
//        JPopupMenu popup = new JPopupMenu();
//        JMenuItem menuItem;
//
//        menuItem = popup.add(new JMenuItem("Download..."));
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                JobsTreeManager manager = JobsTreeManager.getManager();
//                RemoteFileTreeNode node = (RemoteFileTreeNode) manager.getSelectedNode();
//
//                JFrame frame = BatchelorExplorerApp.getApplication().getMainFrame();
//                JFileChooser dialog = new JFileChooser();
//                dialog.setSelectedFile(new File(node.getFile()));
//
//                if (dialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
//                    ServiceTreeNode root = (ServiceTreeNode) manager.getRootNode();
//                    root = (ServiceTreeNode) manager.getRootNode();
//                    WebServiceClient service = root.getWebServiceClient();
//
//                    if (service.getType() == WebServiceClientType.REST) {
//                        try {
//                            BatchelorRestClient client = (BatchelorRestClient) service.getService();
//                            client.fopen(node.getJobIdentity(), node.getFile(), dialog.getSelectedFile());
//                        } catch (RemoteException ex) {
//                            Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (FileNotFoundException ex) {
//                            Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (IOException ex) {
//                            Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    } else if (service.getType() == WebServiceClientType.SOAP) {
//                        {
//                            OutputStream stream = null;
//                            try {
//                                BatchelorSoapClient client = (BatchelorSoapClient) service.getService();
//                                byte[] bytes = client.fopen(node.getJobIdentity(), node.getFile());
//                                stream = new FileOutputStream(dialog.getSelectedFile());
//                                stream.write(bytes);
//                            } catch (FileNotFoundException ex) {
//                                Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (RemoteException ex) {
//                                Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                            } catch (IOException ex) {
//                                Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                            } finally {
//                                try {
//                                    stream.close();
//                                } catch (IOException ex) {
//                                    Logger.getLogger(RemoteFileTreeNode.class.getName()).log(Level.SEVERE, null, ex);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
//
//        menuItem = popup.add(new JMenuItem("Delete"));
//        menuItem.setEnabled(false);
//        menuItem.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                JobsTreeManager manager = JobsTreeManager.getManager();
//                RemoteFileTreeNode node = (RemoteFileTreeNode) manager.getSelectedNode();
//                // TODO: add unlink method to batchelor web service API.
//                throw new UnsupportedOperationException("Not supported yet.");
//            }
//        });
//        return popup;
//    }
    /**
     * @return The icon for this tree node.
     */
    @Override
    public Icon getIcon() {
	JobsTreeManager manager = JobsTreeManager.getManager();
	String name = getName();

	if (name.compareTo("result.zip") == 0) {
	    return manager.getIcon(JobsTreeNodeIconType.FILE_TGZ);
	} else if (name.compareTo("started") == 0 || name.compareTo("queued") == 0 || name.compareTo("finished") == 0) {
	    return manager.getIcon(JobsTreeNodeIconType.FILE_STAMP);
	} else if (name.compareTo("stdout") == 0 || name.compareTo("stderr") == 0) {
	    return manager.getIcon(JobsTreeNodeIconType.FILE_LOG);
	} else if (name.compareTo("indata") == 0) {
	    return manager.getIcon(JobsTreeNodeIconType.FILE_INDATA);
	} else if (getParent().toString().indexOf("result") != -1) {
	    return manager.getIcon(JobsTreeNodeIconType.FILE_RESULT);
	} else {
	    return manager.getIcon(JobsTreeNodeIconType.FILE_ASCII);
	}
    }

    @Override
    public PluginType getPluginType() {
	return PluginType.FILE;
    }
}
