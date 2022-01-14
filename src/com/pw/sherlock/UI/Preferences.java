/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.UI;

import com.pw.sherlock.BWCalls.AgentCalls;
import com.pw.sherlock.utils.PropertyUtils;
import javax.swing.JOptionPane;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.log4j.Logger;

/**
 *
 * @author pwankhed
 */
public class Preferences extends javax.swing.JDialog {

	private Logger prefLogger = Logger.getLogger(AgentCalls.class);

	public Preferences() {
		//super(parent, modal);
		initComponents();
		setTitle("Preferences");
		String[] agentMetaData = PropertyUtils.readSherlockConfigFile();
		if ("Error".equals(agentMetaData[0])) {
			prefLogger.error("Error while reading Sherlock's configuration, cannot proceed further");
			System.exit(0);
		}

		//3.) prop.getProperty("bw.agent.hostname"),
		pref_AgentInterface.setText(agentMetaData[3]);
		//4.) prop.getProperty("bw.agent.port"),
		pref_AgentPort.setValue(Integer.parseInt(agentMetaData[4].trim()));
		// 0.) prop.getProperty("sherlock.connect.timeout"),
		pref_HttpConnTimeOut.setValue(Integer.parseInt(agentMetaData[0].trim()));
		//1.) prop.getProperty("sherlock.read.timeout"),
		pref_SockConnTimeout.setValue(Integer.parseInt(agentMetaData[1].trim()));
		//2.) prop.getProperty("sherlock.polling.interval"),
		pref_HttpReqPolling.setValue(Integer.parseInt(agentMetaData[2].trim()));
		//5.) prop.getProperty("sherlock.agent.monitoring")};
		pref_agentMonitor.setSelectedItem(agentMetaData[5]);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        prefTabPane = new javax.swing.JTabbedPane();
        prefGenralTab = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pref_AgentInterface = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        pref_AgentPort = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pref_agentMonitor = new javax.swing.JComboBox<>();
        pref_HttpConnTimeOut = new javax.swing.JSpinner();
        pref_SockConnTimeout = new javax.swing.JSpinner();
        pref_HttpReqPolling = new javax.swing.JSpinner();
        prefCloseButton = new javax.swing.JButton();
        applyReload = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setModal(true);
        setName("nodePropsDialog"); // NOI18N
        setResizable(false);

        prefGenralTab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Default BW Agent Interface:");

        pref_AgentInterface.setText("jTextField1");
        pref_AgentInterface.setToolTipText("<html>Set the HTTP interface from BW Agent. Once saved, Sherlock will always choose this interface as default. <br/>Note that, this will not change the ongoing session, changes will take effect after restart</html>");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Default BW Agent Port:");

        pref_AgentPort.setEditor(new javax.swing.JSpinner.NumberEditor(pref_AgentPort, "#"));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Http Connection Timeout (ms):");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("HTTP Socket Connection timeout (ms):");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("HTTP requests  polling interval (ms):");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Auto start agent/node monitors on tab:");

        pref_agentMonitor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Yes", "No" }));
        pref_agentMonitor.setToolTipText("<html>Decide if Sherlock should start the monitoring service on startup.<br/>\nIf set to true, on startup BW Agent's memory and CPU stats will be monitored<html/>");

        pref_HttpConnTimeOut.setToolTipText("<html>Sets a specified timeout value, in milliseconds. It is used when opening a communications link to the BW Agent.<br/> This value is dynamically applied in current session and no restart is required<html/>");
        pref_HttpConnTimeOut.setEditor(new javax.swing.JSpinner.NumberEditor(pref_HttpConnTimeOut, "#"));

        pref_SockConnTimeout.setToolTipText("<html>Sets a specified timeout value, in milliseconds.<br/>\nSets the read timeout value when Sherlock is reading stream of data from BW Agent APIs.<br/>\nThis value is dynamically applied in current session and no restart is required<html/>");
        pref_SockConnTimeout.setEditor(new javax.swing.JSpinner.NumberEditor(pref_SockConnTimeout, "#"));

        pref_HttpReqPolling.setToolTipText("<html>Sets a polling interval to ping and get data from BW Agent, in milliseconds.<br/>\nSmaller the value, greater the load to BW Agent. Frequent polling may stress out the BW Agent hence <br/>\nit is recommended to set this value to 3000 ms or more.<br/>\nIf you have more appnodes to monitor, increase the polling to give breathing space to BW Agent<html/>");
        pref_HttpReqPolling.setEditor(new javax.swing.JSpinner.NumberEditor(pref_HttpReqPolling, "#"));

        javax.swing.GroupLayout prefGenralTabLayout = new javax.swing.GroupLayout(prefGenralTab);
        prefGenralTab.setLayout(prefGenralTabLayout);
        prefGenralTabLayout.setHorizontalGroup(
            prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefGenralTabLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(prefGenralTabLayout.createSequentialGroup()
                        .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pref_AgentPort, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pref_AgentInterface, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(prefGenralTabLayout.createSequentialGroup()
                        .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(prefGenralTabLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(pref_SockConnTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(prefGenralTabLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pref_HttpReqPolling, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(prefGenralTabLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pref_HttpConnTimeOut, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(prefGenralTabLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(pref_agentMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(180, 180, 180)))
                .addGap(32, 32, 32))
        );
        prefGenralTabLayout.setVerticalGroup(
            prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prefGenralTabLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pref_AgentInterface, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(pref_AgentPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(pref_HttpConnTimeOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(pref_SockConnTimeout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pref_HttpReqPolling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(prefGenralTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pref_agentMonitor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        prefTabPane.addTab("General", prefGenralTab);

        prefCloseButton.setText("Close");
        prefCloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefCloseButtonActionPerformed(evt);
            }
        });

        applyReload.setText("Apply");
        applyReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyReloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(applyReload)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prefCloseButton))
                    .addComponent(prefTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prefTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(applyReload)
                    .addComponent(prefCloseButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prefCloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefCloseButtonActionPerformed
		dispose();
    }//GEN-LAST:event_prefCloseButtonActionPerformed

    private void applyReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyReloadActionPerformed
		try {
			Parameters params = new Parameters();
			FileBasedConfigurationBuilder<FileBasedConfiguration> builder
					= new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
							.configure(params.properties()
									.setFileName("config/sherlock.properties"));

			Configuration config = builder.getConfiguration();
			config.setProperty("bw.agent.hostname", pref_AgentInterface.getText());
			config.setProperty("bw.agent.port", pref_AgentPort.getValue());
			config.setProperty("sherlock.connect.timeout", pref_HttpConnTimeOut.getValue());
			Home.master_connectTimeout = (int) pref_HttpConnTimeOut.getValue();
			config.setProperty("sherlock.read.timeout", pref_SockConnTimeout.getValue());
			Home.master_readTimeout = (int) pref_SockConnTimeout.getValue();
			config.setProperty("sherlock.polling.interval", pref_HttpReqPolling.getValue());
			config.setProperty("sherlock.agent.monitoring", pref_agentMonitor.getSelectedItem());
			builder.save();

			JOptionPane.showMessageDialog(prefTabPane, "Your preferences are saved. Some values will be in effect after the application restart",
					"Success", JOptionPane.INFORMATION_MESSAGE);
			//JOptionPane.showMessageDialog(prefTabPane, config);
		} catch (ConfigurationException e) {
			System.out.println(e.getMessage());
		}
    }//GEN-LAST:event_applyReloadActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyReload;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JButton prefCloseButton;
    private javax.swing.JPanel prefGenralTab;
    private javax.swing.JTabbedPane prefTabPane;
    private javax.swing.JTextField pref_AgentInterface;
    private javax.swing.JSpinner pref_AgentPort;
    private javax.swing.JSpinner pref_HttpConnTimeOut;
    private javax.swing.JSpinner pref_HttpReqPolling;
    private javax.swing.JSpinner pref_SockConnTimeout;
    private javax.swing.JComboBox<String> pref_agentMonitor;
    // End of variables declaration//GEN-END:variables
}
