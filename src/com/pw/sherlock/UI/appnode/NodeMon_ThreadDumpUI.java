/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.UI.appnode;

import com.pw.sherlock.UI.Home;
import com.pw.sherlock.utils.SherlockUtils;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author pwankhed
 */
public class NodeMon_ThreadDumpUI extends javax.swing.JPanel {

	/**
	 * Creates new form NodeMon_ThreadDumpUI
	 *
	 * @param nodeHost
	 * @param nodePort
	 * @param currState
	 */
	public NodeMon_ThreadDumpUI(String nodeHost, String nodePort, String currState) {
		initComponents();
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					td_TextArea.setEditable(false);
					DefaultCaret caret = (DefaultCaret) td_TextArea.getCaret();
					caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
					td_TextArea.getCaret().setVisible(true);
					td_TextArea.getCaret().setSelectionVisible(true);

					String currTime = "Thread dump collected at: " + java.util.Calendar.getInstance().getTime().toString();
					td_DetailsLbl.setText(currTime);
					SherlockUtils obj = new SherlockUtils();
					String URL = "http://" + nodeHost + ":" + nodePort + "/bw/framework.json/osgi?command=td";
					
					String nodEnDomainName = Home.consoleBaseTabbedPane.getTitleAt(Home.consoleBaseTabbedPane.getSelectedIndex());
					nodEnDomainName = nodEnDomainName.replace("(", "in domain ");
					nodEnDomainName = nodEnDomainName.replace(")", ".");
										
					currTime = currTime + " for appnode " + nodEnDomainName;

					String ThreadDump = obj.processOSGiCommands(URL, "NodeMon_ThreadDumpUI");
					td_TextArea.setText("################################################################\n" + currTime + "\n"
							+ currState + "\n################################################################\n\n" + ThreadDump);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeTdTab = new javax.swing.JButton();
        td_DetailsLbl = new javax.swing.JLabel();
        tdScrollPane = new javax.swing.JScrollPane();
        td_TextArea = new javax.swing.JTextArea();

        closeTdTab.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/icons8-close-window-24.png"))); // NOI18N
        closeTdTab.setToolTipText("Close");
        closeTdTab.setBorderPainted(false);
        closeTdTab.setContentAreaFilled(false);
        closeTdTab.setFocusPainted(false);
        closeTdTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeTdTabActionPerformed(evt);
            }
        });

        td_DetailsLbl.setText("Loading...");

        td_TextArea.setColumns(20);
        td_TextArea.setLineWrap(true);
        td_TextArea.setRows(5);
        td_TextArea.setWrapStyleWord(true);
        td_TextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tdScrollPane.setViewportView(td_TextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tdScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(td_DetailsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 843, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(closeTdTab, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closeTdTab, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(td_DetailsLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tdScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void closeTdTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeTdTabActionPerformed
		// TODO add your handling code here:
		//this.getParent().remove(nodeMonTabs.indexOfTab("Appnode runtime monitor"));
		SwingUtilities.getAncestorOfClass(JTabbedPane.class, this).remove(this);
		//nodeMonTabs.remove(nodeMonTabs.indexOfTab("Appnode runtime monitor"));
    }//GEN-LAST:event_closeTdTabActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeTdTab;
    private javax.swing.JScrollPane tdScrollPane;
    private javax.swing.JLabel td_DetailsLbl;
    private javax.swing.JTextArea td_TextArea;
    // End of variables declaration//GEN-END:variables
}
