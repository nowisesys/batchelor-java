/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DirectoryPluginPanel.java
 *
 * Created on Aug 11, 2010, 4:27:22 PM
 */
package se.uu.bmc.it.batchelor.explorer.plugin;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import se.uu.bmc.it.batchelor.WebServiceInterface;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginData;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class DirectoryPluginPanel extends javax.swing.JPanel implements PluginPanelInterface {

    private PluginData data;

    /** Creates new form DirectoryPluginPanel */
    public DirectoryPluginPanel() {
	initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelHeader = new javax.swing.JLabel();
        jScrollPane = new javax.swing.JScrollPane();
        jListFiles = new javax.swing.JList();
        jLabelFiles = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp.class).getContext().getResourceMap(DirectoryPluginPanel.class);
        jLabelHeader.setFont(resourceMap.getFont("jLabelHeader.font")); // NOI18N
        jLabelHeader.setIcon(resourceMap.getIcon("jLabelHeader.icon")); // NOI18N
        jLabelHeader.setText(resourceMap.getString("jLabelHeader.text")); // NOI18N
        jLabelHeader.setName("jLabelHeader"); // NOI18N

        jScrollPane.setName("jScrollPane"); // NOI18N

        jListFiles.setModel(new DefaultListModel());
        jListFiles.setDragEnabled(true);
        jListFiles.setName("jListFiles"); // NOI18N
        jScrollPane.setViewportView(jListFiles);

        jLabelFiles.setText(resourceMap.getString("jLabelFiles.text")); // NOI18N
        jLabelFiles.setName("jLabelFiles"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabelFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabelHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelFiles)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelFiles;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JList jListFiles;
    private javax.swing.JScrollPane jScrollPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setActive(boolean active) {
	if (active) {
	    setContent();
	}
	this.setVisible(active);
    }

    @Override
    public void setPluginData(PluginData data) {
	this.data = data;
    }

    private void setContent() {
	WebServiceClient client = data.getService();
	WebServiceInterface service = client.getService();

	try {
	    List<String> files = service.readdir(data.getJobIdentity());
	    DefaultListModel model = (DefaultListModel) jListFiles.getModel();
	    model.removeAllElements();
	    for (String file : files) {
		if (file.startsWith(data.getPath())) {
		    model.addElement(file);
		}
	    }
	    model.trimToSize();
	    jLabelFiles.setText(String.format("%d files total", model.getSize()));
	} catch (RemoteException ex) {
	    Logger.getLogger(DirectoryPluginPanel.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
