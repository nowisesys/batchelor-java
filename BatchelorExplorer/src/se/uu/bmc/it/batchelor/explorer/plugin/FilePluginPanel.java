/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FilePluginPanel.java
 *
 * Created on Aug 11, 2010, 5:09:01 PM
 */
package se.uu.bmc.it.batchelor.explorer.plugin;

import se.uu.bmc.it.batchelor.explorer.WebServiceClient;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class FilePluginPanel extends javax.swing.JPanel implements PluginPanelInterface {

    private WebServiceClient service;

    /** Creates new form FilePluginPanel */
    public FilePluginPanel() {
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
        jTextAreaContent = new javax.swing.JTextArea();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp.class).getContext().getResourceMap(FilePluginPanel.class);
        jLabelHeader.setFont(resourceMap.getFont("jLabelHeader.font")); // NOI18N
        jLabelHeader.setIcon(resourceMap.getIcon("jLabelHeader.icon")); // NOI18N
        jLabelHeader.setText(resourceMap.getString("jLabelHeader.text")); // NOI18N
        jLabelHeader.setName("jLabelHeader"); // NOI18N

        jScrollPane.setName("jScrollPane"); // NOI18N

        jTextAreaContent.setColumns(20);
        jTextAreaContent.setRows(5);
        jTextAreaContent.setName("jTextAreaContent"); // NOI18N
        jScrollPane.setViewportView(jTextAreaContent);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
            .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JTextArea jTextAreaContent;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setActive(boolean active) {
	setVisible(active);
    }

    @Override
    public void setService(WebServiceClient service) {
	this.service = service;
    }
}
