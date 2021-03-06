/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ServicePluginPanel.java
 *
 * Created on Aug 11, 2010, 5:15:21 PM
 */
package se.uu.bmc.it.batchelor.explorer.plugin;

import javax.swing.JPanel;
import se.uu.bmc.it.batchelor.explorer.WebServiceClient;
import se.uu.bmc.it.batchelor.explorer.plugin.spi.PluginData;

/**
 *
 * @author Anders Lövgren (QNET/BMC CompDept)
 */
public class ServicePluginPanel extends JPanel implements PluginPanelInterface {

    private WebServiceClient service;

    /** Creates new form ServicePluginPanel */
    public ServicePluginPanel() {
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
        jLabelNameHeader = new javax.swing.JLabel();
        jLabelTypeHeader = new javax.swing.JLabel();
        jLabelUrlHeader = new javax.swing.JLabel();
        jLabelNameValue = new javax.swing.JLabel();
        jLabelTypeValue = new javax.swing.JLabel();
        jLabelUrlValue = new javax.swing.JLabel();
        jLabelDateHeader = new javax.swing.JLabel();
        jLabelDateValue = new javax.swing.JLabel();

        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(se.uu.bmc.it.batchelor.explorer.BatchelorExplorerApp.class).getContext().getResourceMap(ServicePluginPanel.class);
        jLabelHeader.setFont(resourceMap.getFont("jLabelHeader.font")); // NOI18N
        jLabelHeader.setIcon(resourceMap.getIcon("jLabelHeader.icon")); // NOI18N
        jLabelHeader.setText(resourceMap.getString("jLabelHeader.text")); // NOI18N
        jLabelHeader.setName("jLabelHeader"); // NOI18N

        jLabelNameHeader.setFont(resourceMap.getFont("jLabelUrlHeader.font")); // NOI18N
        jLabelNameHeader.setText(resourceMap.getString("jLabelNameHeader.text")); // NOI18N
        jLabelNameHeader.setName("jLabelNameHeader"); // NOI18N

        jLabelTypeHeader.setFont(resourceMap.getFont("jLabelUrlHeader.font")); // NOI18N
        jLabelTypeHeader.setText(resourceMap.getString("jLabelTypeHeader.text")); // NOI18N
        jLabelTypeHeader.setName("jLabelTypeHeader"); // NOI18N

        jLabelUrlHeader.setFont(resourceMap.getFont("jLabelUrlHeader.font")); // NOI18N
        jLabelUrlHeader.setText(resourceMap.getString("jLabelUrlHeader.text")); // NOI18N
        jLabelUrlHeader.setName("jLabelUrlHeader"); // NOI18N

        jLabelNameValue.setText(resourceMap.getString("jLabelNameValue.text")); // NOI18N
        jLabelNameValue.setName("jLabelNameValue"); // NOI18N

        jLabelTypeValue.setText(resourceMap.getString("jLabelTypeValue.text")); // NOI18N
        jLabelTypeValue.setName("jLabelTypeValue"); // NOI18N

        jLabelUrlValue.setText(resourceMap.getString("jLabelUrlValue.text")); // NOI18N
        jLabelUrlValue.setName("jLabelUrlValue"); // NOI18N

        jLabelDateHeader.setFont(resourceMap.getFont("jLabelDateHeader.font")); // NOI18N
        jLabelDateHeader.setText(resourceMap.getString("jLabelDateHeader.text")); // NOI18N
        jLabelDateHeader.setName("jLabelDateHeader"); // NOI18N

        jLabelDateValue.setText(resourceMap.getString("jLabelDateValue.text")); // NOI18N
        jLabelDateValue.setName("jLabelDateValue"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelDateHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelDateValue, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabelHeader, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNameHeader, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelTypeHeader, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelUrlHeader, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTypeValue, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jLabelNameValue, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jLabelUrlValue, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNameHeader)
                    .addComponent(jLabelNameValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTypeHeader)
                    .addComponent(jLabelTypeValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelUrlHeader)
                    .addComponent(jLabelUrlValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDateHeader)
                    .addComponent(jLabelDateValue))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelDateHeader;
    private javax.swing.JLabel jLabelDateValue;
    private javax.swing.JLabel jLabelHeader;
    private javax.swing.JLabel jLabelNameHeader;
    private javax.swing.JLabel jLabelNameValue;
    private javax.swing.JLabel jLabelTypeHeader;
    private javax.swing.JLabel jLabelTypeValue;
    private javax.swing.JLabel jLabelUrlHeader;
    private javax.swing.JLabel jLabelUrlValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setPluginData(PluginData data) {
	this.service = data.getService();
    }

    @Override
    public void setActive(boolean active) {
	jLabelNameValue.setText(service.getName());
	jLabelTypeValue.setText(service.getType().name());
	jLabelUrlValue.setText(service.getURL().toString());
	jLabelDateValue.setText(service.getDate().toString());
	setVisible(active);
    }
}
