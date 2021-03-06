/*
 * BatchelorExplorerView.java
 */
package se.uu.bmc.it.batchelor.explorer;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.swing.JOptionPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import se.uu.bmc.it.batchelor.explorer.plugin.service.PluginService;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginInterface;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginType;

import se.uu.bmc.it.batchelor.explorer.tree.JobsTreeCellRenderer;
import se.uu.bmc.it.batchelor.explorer.tree.JobsTreeManager;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNode;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNodeIcon;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.JobsTreeNodeIconType;
import se.uu.bmc.it.batchelor.explorer.tree.nodes.ServiceTreeNode;

/**
 * The application's main frame.
 */
public class BatchelorExplorerView extends FrameView {

    public BatchelorExplorerView(SingleFrameApplication app) {
	super(app);

	initComponents();

	// status bar initialization - message timeout, idle icon and busy animation, etc
	ResourceMap resourceMap = getResourceMap();
	int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
	messageTimer = new Timer(messageTimeout, new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		statusMessageLabel.setText("");
	    }
	});
	messageTimer.setRepeats(false);
	int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
	for (int i = 0; i < busyIcons.length; i++) {
	    busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
	}
	busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
		statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
	    }
	});
	idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
	statusAnimationLabel.setIcon(idleIcon);
	progressBar.setVisible(false);

	// connecting action tasks to status bar via TaskMonitor
	TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
	taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

	    public void propertyChange(java.beans.PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if ("started".equals(propertyName)) {
		    if (!busyIconTimer.isRunning()) {
			statusAnimationLabel.setIcon(busyIcons[0]);
			busyIconIndex = 0;
			busyIconTimer.start();
		    }
		    progressBar.setVisible(true);
		    progressBar.setIndeterminate(true);
		} else if ("done".equals(propertyName)) {
		    busyIconTimer.stop();
		    statusAnimationLabel.setIcon(idleIcon);
		    progressBar.setVisible(false);
		    progressBar.setValue(0);
		} else if ("message".equals(propertyName)) {
		    String text = (String) (evt.getNewValue());
		    statusMessageLabel.setText((text == null) ? "" : text);
		    messageTimer.restart();
		} else if ("progress".equals(propertyName)) {
		    int value = (Integer) (evt.getNewValue());
		    progressBar.setVisible(true);
		    progressBar.setIndeterminate(false);
		    progressBar.setValue(value);
		}
	    }
	});

	JobsTreeCellRenderer renderer = new JobsTreeCellRenderer();
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.WebService"), JobsTreeNodeIconType.WEB_SERVICE));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Directory"), JobsTreeNodeIconType.DIRECTORY));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.File.Compress"), JobsTreeNodeIconType.FILE_TGZ));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.File.Stamp"), JobsTreeNodeIconType.FILE_STAMP));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.File.Log"), JobsTreeNodeIconType.FILE_LOG));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.File.Ascii"), JobsTreeNodeIconType.FILE_ASCII));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.File.Indata"), JobsTreeNodeIconType.FILE_INDATA));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.File.Result"), JobsTreeNodeIconType.FILE_RESULT));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.WebService"), JobsTreeNodeIconType.WEB_SERVICE));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Jobs.Warning"), JobsTreeNodeIconType.JOB_WARNING));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Jobs.Crashed"), JobsTreeNodeIconType.JOB_CRASHED));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Jobs.Finished"), JobsTreeNodeIconType.JOB_SUCCESS));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Jobs.Running"), JobsTreeNodeIconType.JOB_RUNNING));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Jobs.Error"), JobsTreeNodeIconType.JOB_ERROR));
	renderer.addIcon(new JobsTreeNodeIcon(resourceMap.getIcon("TreeIcon.Jobs.Pending"), JobsTreeNodeIconType.JOB_PENDING));
	jTreeJobs.setCellRenderer(renderer);
	jTreeJobs.getSelectionModel().setSelectionMode(jTreeJobs.getSelectionModel().SINGLE_TREE_SELECTION);

	jobsTreeManager = new JobsTreeManager(jTreeJobs);
	JobsTreeManager.setManager(jobsTreeManager);

//        jobsTreeManager.setServiceContextMenu(ServiceTreeNode.createContextMenu());
//        jobsTreeManager.setQueuedJobContextMenu(QueuedJobTreeNode.createContextMenu());
//        jobsTreeManager.setFileContextMenu(RemoteFileTreeNode.createContextMenu());

	jTreeJobs.addMouseListener(new MouseAdapter() {

	    @Override
	    public void mousePressed(MouseEvent event) {
		if (event.getButton() == MouseEvent.BUTTON1) {
		    setPluginComponent(event);
		} else {
		    showContextMenu(event);
		}
	    }

	    @Override
	    public void mouseReleased(MouseEvent event) {
		showContextMenu(event);
	    }

	    //
	    // Popup the context menu associated with this tree node.
	    //
	    private void showContextMenu(MouseEvent event) {
		if (event.isPopupTrigger()) {
		    try {
			TreePath path = jTreeJobs.getPathForLocation(event.getX(), event.getY());
			if (path != null) {
			    jTreeJobs.setSelectionPath(path);
			    JobsTreeNode node = (JobsTreeNode) path.getLastPathComponent();
			    JPopupMenu popup = node.getContextMenu();
			    popup.show(event.getComponent(), event.getX(), event.getY());
			}
		    } catch (Exception e) {
		    }
		}

	    }

	    //
	    // Set the plugin associated with this tree node.
	    //
	    private void setPluginComponent(MouseEvent event) {
		try {
		    TreePath path = jTreeJobs.getPathForLocation(event.getX(), event.getY());
		    if (path != null) {
			jTreeJobs.setSelectionPath(path);
			JobsTreeNode node = (JobsTreeNode) path.getLastPathComponent();
			if (plugin != null) {
			    plugin.setActive(false);
			}
			PluginService loader = PluginService.getInstance();
			plugin = loader.getPlugin(node.getPluginType());
			if (plugin != null) {
			    jSplitPanel.setRightComponent(plugin.getComponent());
			    plugin.setData(node.getPluginData());
			    plugin.setActive(true);
			}
		    }
		} catch (Exception e) {
		    Logger.getLogger(BatchelorExplorerView.class.getName()).log(Level.SEVERE, null, e);
		}

	    }
	});

	PluginService loader = PluginService.getInstance();
	plugin = loader.getPlugin(PluginType.START);
	if (plugin != null) {
	    jSplitPanel.setRightComponent(plugin.getComponent());
	    plugin.setActive(true);
	}
    }

    @Action
    public void showAboutBox() {
	if (aboutBoxDialog == null) {
	    JFrame mainFrame = BatchelorExplorerApp.getApplication().getMainFrame();
	    aboutBoxDialog = new AboutBoxDialog(mainFrame);
	    aboutBoxDialog.setLocationRelativeTo(mainFrame);
	}
	BatchelorExplorerApp.getApplication().show(aboutBoxDialog);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSplitPanel = new javax.swing.JSplitPane();
        jPanelContent = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeJobs = new javax.swing.JTree();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        connectMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        selectAllMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenuItem();
        pluginsMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        jSplitPanel.setDividerLocation(150);
        jSplitPanel.setName("jSplitPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp.class).getContext().getResourceMap(BatchelorExplorerView.class);
        jPanelContent.setBackground(resourceMap.getColor("jPanelContent.background")); // NOI18N
        jPanelContent.setName("jPanelContent"); // NOI18N

        javax.swing.GroupLayout jPanelContentLayout = new javax.swing.GroupLayout(jPanelContent);
        jPanelContent.setLayout(jPanelContentLayout);
        jPanelContentLayout.setHorizontalGroup(
            jPanelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 623, Short.MAX_VALUE)
        );
        jPanelContentLayout.setVerticalGroup(
            jPanelContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 339, Short.MAX_VALUE)
        );

        jSplitPanel.setRightComponent(jPanelContent);

        jScrollPane1.setName("jScrollPane1"); // NOI18N
        jScrollPane1.setPreferredSize(new java.awt.Dimension(170, 363));

        jTreeJobs.setBackground(resourceMap.getColor("jTreeJobs.background")); // NOI18N
        jTreeJobs.setName("jTreeJobs"); // NOI18N
        jTreeJobs.setPreferredSize(null);
        jScrollPane1.setViewportView(jTreeJobs);

        jSplitPanel.setLeftComponent(jScrollPane1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp.class).getContext().getActionMap(BatchelorExplorerView.class, this);
        connectMenuItem.setAction(actionMap.get("showServerConnectDialog")); // NOI18N
        connectMenuItem.setText(resourceMap.getString("connectMenuItem.text")); // NOI18N
        connectMenuItem.setName("connectMenuItem"); // NOI18N
        fileMenu.add(connectMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copyMenuItem.setText(resourceMap.getString("copyMenuItem.text")); // NOI18N
        copyMenuItem.setName("copyMenuItem"); // NOI18N
        editMenu.add(copyMenuItem);

        selectAllMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        selectAllMenuItem.setText(resourceMap.getString("selectAllMenuItem.text")); // NOI18N
        selectAllMenuItem.setName("selectAllMenuItem"); // NOI18N
        editMenu.add(selectAllMenuItem);

        menuBar.add(editMenu);

        toolsMenu.setText(resourceMap.getString("toolsMenu.text")); // NOI18N
        toolsMenu.setName("toolsMenu"); // NOI18N

        optionsMenuItem.setText(resourceMap.getString("optionsMenuItem.text")); // NOI18N
        optionsMenuItem.setName("optionsMenuItem"); // NOI18N
        toolsMenu.add(optionsMenuItem);

        pluginsMenuItem.setAction(actionMap.get("showPluginsDialog")); // NOI18N
        pluginsMenuItem.setText(resourceMap.getString("pluginsMenuItem.text")); // NOI18N
        pluginsMenuItem.setName("pluginsMenuItem"); // NOI18N
        toolsMenu.add(pluginsMenuItem);

        menuBar.add(toolsMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 653, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 469, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public void showServerConnectDialog() {
	try {
	    if (serverConnectDialog == null) {
		JFrame mainFrame = BatchelorExplorerApp.getApplication().getMainFrame();
		serverConnectDialog = new ServiceConnectionDialog(mainFrame, true);
		serverConnectDialog.setLocationRelativeTo(mainFrame);
	    }
	    serverConnectDialog.setVisible(true);
	    if (serverConnectDialog.getReturnStatus() == ServiceConnectionDialog.RET_OK) {
		String serviceName = serverConnectDialog.getServerURL();
		WebServiceClient service = WebServiceClient.createWebServiceClient(serviceName);
		ServiceTreeNode node = new ServiceTreeNode(service);
		node.addChildNodes();
		jobsTreeManager.clearTree();
		jobsTreeManager.setRootNode(node);
	    }
	} catch (MalformedURLException e) {
	    JOptionPane.showMessageDialog(this.getFrame(),
		    "URL error: " + e.getMessage(),
		    "Malformed URL Exception",
		    JOptionPane.ERROR_MESSAGE);
	} catch (RemoteException e) {
	    JOptionPane.showMessageDialog(this.getFrame(),
		    "Failed communicate with web service: " + e.getMessage(),
		    "Remote Exception",
		    JOptionPane.ERROR_MESSAGE);
	}
    }

    @Action
    public void showPluginsDialog() {
	PluginsDialog dialog = new PluginsDialog(null, true);
	dialog.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem connectMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPanel;
    private javax.swing.JTree jTreeJobs;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JMenuItem pluginsMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem selectAllMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu toolsMenu;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBoxDialog;
    private ServiceConnectionDialog serverConnectDialog;
    private JobsTreeManager jobsTreeManager;
    private PluginInterface plugin;
}
