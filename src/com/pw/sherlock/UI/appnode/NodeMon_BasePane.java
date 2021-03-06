/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.UI.appnode;

import com.pw.sherlock.UI.agent.AgentMon_HealthCheckup;
import com.pw.sherlock.BWCalls.AppnodeCalls;
import com.pw.sherlock.OshiImpls.OshiMiscFuncs;
import com.pw.sherlock.UI.Home;
import com.pw.sherlock.utils.CustomTabbedPaneUI_V1;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import org.json.JSONObject;

/**
 *
 * @author pwankhed
 */
public class NodeMon_BasePane extends javax.swing.JPanel {

	/**
	 * Creates new form NodeMonitor
	 *
	 * @param domainName
	 * @param appspaceName
	 * @param appnodeName
	 * @param nodePort
	 */
	AppnodeCalls nodeObj;
	OshiMiscFuncs oshiObj;

	private static final org.apache.log4j.Logger nodeMonBasePaneLogger = org.apache.log4j.Logger.getLogger(NodeMon_BasePane.class);
	
	public NodeMon_BasePane(String domainName, String appspaceName, String appnodeName, String nodePort) {
		initComponents();
		nodeObj = new AppnodeCalls();

		String nodeMetaData = "<html>Details of appnode " + appnodeName + " from appspace "
				+ appspaceName + "<br/> that belongs to domain " + domainName + "<br/><br/>"
				+ nodeObj.getEngineState(domainName, appspaceName, appnodeName) + nodeObj.getNodeState(nodePort);

		nodeInfoBrief.setSize(nodeInfoBrief.getPreferredSize());
		nodeInfoBrief.setText(nodeMetaData);

		UIManager.put("TabbedPane.tabInsets", new Insets(2, 1, 2, 20));
		nodeMonTabs.setUI(new CustomTabbedPaneUI_V1());

		domainLbl.setText(domainName);
		appspaceLbl.setText(appspaceName);
		appnodeLbl.setText(appnodeName);
		appnodePortLbl.setText(nodePort);

		recMonExpln.setText("<html>Provide visual representation of appnode's<br/> heap memory and CPU Utilization</html>");

		AgeHealthCheckExpln.setText("<html>Send a REST call to appnode to check the <br/>connectivity status</html>");
		AgeHealthCheckExpln.setSize(AgeHealthCheckExpln.getPreferredSize());

		ageLogExpln.setText("<html>Display the logback.xml file contents <br/>for BW Appnode</html>");
		ageLogExpln.setSize(ageLogExpln.getPreferredSize());

		ageMsgConnExpln.setText("<html>Based on the appspace's trasport, check the <br/>connectivity with message broker</html>");
		ageMsgConnExpln.setSize(ageMsgConnExpln.getPreferredSize());

		ageTraExpln.setText("<html>Lists BW Engine's configuration</html>");
		ageTraExpln.setSize(ageTraExpln.getPreferredSize());

		osgiConExpln.setText("<html>Console to execute OSGi commands available at <br/>appnode level</html>");
		osgiConExpln.setSize(osgiConExpln.getPreferredSize());

		nodeInfoTextArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) nodeInfoTextArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		nodeInfoTextArea.getCaret().setVisible(true);
		nodeInfoTextArea.getCaret().setSelectionVisible(true);
		JSONObject json = new JSONObject(nodeObj.getNodeConfig(domainName, appspaceName, appnodeName)); // Convert text to object
		nodeInfoTextArea.setText(json.toString(4));

		nodeMonTabs.revalidate();
		nodeMonTabs.repaint();
		if ("Yes".equals(Home.master_loadAgentMon)) {
			nodeMonSwitch.setSelected(true);
			Component nodeChartPane = new NodeMon_ChartsUI(domainName, appspaceName, appnodeName, nodePort);
			Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/sys-monitor-24.png"));
			nodeMonTabs.addTab("Appnode monitor", nodeIcon, nodeChartPane);
		} else {
			nodeMonSwitch.setSelected(false);
		}
	}

	public NodeMon_BasePane() {
		//Empty constructor
		initComponents();
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
		System.out.println("The tab is just closed on this JTabbedPane (NodeMonBase_Pane) " + this.getName());
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nodeMonTabs = new javax.swing.JTabbedPane();
        nodeInfo = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nodeInfoTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        domainLbl = new javax.swing.JLabel();
        appspaceLbl = new javax.swing.JLabel();
        appnodeLbl = new javax.swing.JLabel();
        appnodePortLbl = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        recMonExpln = new javax.swing.JLabel();
        AgeHealthCheckExpln = new javax.swing.JLabel();
        ageLogExpln = new javax.swing.JLabel();
        ageTraExpln = new javax.swing.JLabel();
        ageMsgConnExpln = new javax.swing.JLabel();
        nodeHealthcheckSwitch = new javax.swing.JCheckBox();
        nodeMsgConnSwitch = new javax.swing.JCheckBox();
        nodeLogbackSwitch = new javax.swing.JCheckBox();
        nodeTraSwitch = new javax.swing.JCheckBox();
        nodeMonSwitch = new javax.swing.JCheckBox();
        nodeInfoBrief = new javax.swing.JLabel();
        osgiConSwitch = new javax.swing.JCheckBox();
        osgiConExpln = new javax.swing.JLabel();

        nodeMonTabs.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        nodeMonTabs.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        nodeInfoTextArea.setColumns(20);
        nodeInfoTextArea.setLineWrap(true);
        nodeInfoTextArea.setRows(5);
        nodeInfoTextArea.setWrapStyleWord(true);
        nodeInfoTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(nodeInfoTextArea);

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        jLabel6.setText("Appnode configuration:");

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        jLabel7.setText("Appnode Metadata:");

        domainLbl.setText("Domain Name");

        appspaceLbl.setText("Appspace Name");

        appnodeLbl.setText("Appnode Name");

        appnodePortLbl.setText("Appnode Name");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        jLabel5.setText("Available services on this node:");

        recMonExpln.setText("Loading...");

        AgeHealthCheckExpln.setText("Loading...");

        ageLogExpln.setText("Loading...");

        ageTraExpln.setText("Loading...");

        ageMsgConnExpln.setText("Loading...");
        ageMsgConnExpln.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        nodeHealthcheckSwitch.setText("Node Healthcheck");
        nodeHealthcheckSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeHealthcheckSwitchActionPerformed(evt);
            }
        });

        nodeMsgConnSwitch.setText("Node Messaging connectivity");
        nodeMsgConnSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeMsgConnSwitchActionPerformed(evt);
            }
        });

        nodeLogbackSwitch.setText("Node logging configuration");
        nodeLogbackSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeLogbackSwitchActionPerformed(evt);
            }
        });

        nodeTraSwitch.setText("Engine configuration");
        nodeTraSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeTraSwitchActionPerformed(evt);
            }
        });

        nodeMonSwitch.setText("Resource Monitor");
        nodeMonSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodeMonSwitchActionPerformed(evt);
            }
        });

        nodeInfoBrief.setText("Loading...");
        nodeInfoBrief.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        osgiConSwitch.setText("OSGi Console");
        osgiConSwitch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                osgiConSwitchActionPerformed(evt);
            }
        });

        osgiConExpln.setText("Loading...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(nodeInfoBrief, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                    .addComponent(nodeMsgConnSwitch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nodeLogbackSwitch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nodeTraSwitch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ageLogExpln)
                            .addComponent(ageTraExpln))
                        .addGap(122, 122, 122))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(nodeHealthcheckSwitch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nodeMonSwitch)
                            .addComponent(osgiConSwitch)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(recMonExpln)
                                    .addComponent(osgiConExpln)
                                    .addComponent(ageMsgConnExpln)
                                    .addComponent(AgeHealthCheckExpln))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nodeInfoBrief)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addComponent(nodeMonSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recMonExpln)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(osgiConSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(osgiConExpln)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nodeHealthcheckSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AgeHealthCheckExpln)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nodeMsgConnSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageMsgConnExpln)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nodeLogbackSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageLogExpln)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nodeTraSwitch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageTraExpln)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout nodeInfoLayout = new javax.swing.GroupLayout(nodeInfo);
        nodeInfo.setLayout(nodeInfoLayout);
        nodeInfoLayout.setHorizontalGroup(
            nodeInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nodeInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nodeInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(nodeInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nodeInfoLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(domainLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appspaceLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appnodeLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appnodePortLbl)
                        .addGap(0, 104, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        nodeInfoLayout.setVerticalGroup(
            nodeInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nodeInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nodeInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(domainLbl)
                    .addComponent(appspaceLbl)
                    .addComponent(appnodeLbl)
                    .addComponent(appnodePortLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(nodeInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE))
                .addContainerGap())
        );

        nodeMonTabs.addTab("Appnode Details", new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/info-24.png")), nodeInfo); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nodeMonTabs)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nodeMonTabs)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void nodeMonSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeMonSwitchActionPerformed
		try {
			//SwingUtilities.invokeLater(new Runnable() {
				//@Override
				//public void run() {
					if (nodeMonSwitch.isSelected()) {
					
						Component nodeChartPane = new NodeMon_ChartsUI(domainLbl.getText(), appspaceLbl.getText(), appnodeLbl.getText(), appnodePortLbl.getText());
						Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/sys-monitor-24.png"));
						//nodeMonTabs.addTab("Monitor - " + appnodeLbl.getText() + " (" + domainLbl.getText() + ")", nodeIcon, nodeChartPane);
						this.nodeMonTabs.addTab("Monitor - " + appnodeLbl.getText() + " (" + domainLbl.getText() + ")", nodeIcon, nodeChartPane);
						//nodeMonTabs.setSelectedIndex(nodeMonTabs.indexOfTab("Monitor - " + appnodeLbl.getText() + " (" + domainLbl.getText() + ")"));
						this.nodeMonTabs.setSelectedIndex(nodeMonTabs.indexOfTab("Monitor - " + appnodeLbl.getText() + " (" + domainLbl.getText() + ")"));
						
					} else {
						
						int dialogResult = JOptionPane.showConfirmDialog(null, "Closing monitoring tab and reopening it later will not show the historic monitoring"
								+ " statistics. Are you sure you want to close this tab?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
						if (dialogResult == JOptionPane.YES_OPTION) {
							//nodeMonTabs.remove(nodeMonTabs.indexOfTab("Monitor - " + appnodeLbl.getText() + " (" + domainLbl.getText() + ")"));
							nodeMonTabs.remove(nodeMonTabs.indexOfTab("Monitor - " + appnodeLbl.getText() + " (" + domainLbl.getText() + ")"));
							//NodeMon_BasePane.nodeMonSwitch.setSelected(false);
							nodeMonSwitch.setSelected(false);
						} else {
							nodeMonSwitch.setSelected(true);
						}
					}
				//}
			//});
		} catch (Exception ex) {
			ex.printStackTrace();
			nodeMonBasePaneLogger.error("Error while processing request in 'nodeMonSwitchActionPerformed'. Error: \n" + ex.getMessage());
		}
    }//GEN-LAST:event_nodeMonSwitchActionPerformed

    private void nodeTraSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeTraSwitchActionPerformed
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (nodeTraSwitch.isSelected()) {
							Component nodeTraTab = new NodeMon_FileContentsTab(appnodePortLbl.getText(), "lec");
							Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/properties-24.png"));
							nodeMonTabs.addTab("Engine Configuration", nodeIcon, nodeTraTab);
							nodeMonTabs.setSelectedIndex(nodeMonTabs.indexOfTab("Engine Configuration"));
					} else {
						nodeMonTabs.remove(nodeMonTabs.indexOfTab("Engine Configuration"));
					}
				}
			});
		} catch (Exception ex) {
			nodeMonBasePaneLogger.error("Error while processing request in 'nodeTraSwitchActionPerformed'. Error: \n" + ex.getMessage());
		}
    }//GEN-LAST:event_nodeTraSwitchActionPerformed

    private void nodeLogbackSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeLogbackSwitchActionPerformed

		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (nodeLogbackSwitch.isSelected()) {
							
						Component nodeLogbackTab = new NodeMon_FileContentsTab(appnodePortLbl.getText(), "frwk:lloggers");
						Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/logback-xml-file-24.png"));
						nodeMonTabs.addTab("Appnode Logback", nodeIcon, nodeLogbackTab);
						nodeMonTabs.setSelectedIndex(nodeMonTabs.indexOfTab("Appnode Logback"));						
					} else {
						nodeMonTabs.remove(nodeMonTabs.indexOfTab("Appnode Logback"));
					}
				}
			});
		} catch (Exception ex) {
			nodeMonBasePaneLogger.error("Error while processing request in 'nodeLogbackSwitchActionPerformed'. Error: \n" + ex.getMessage());
		}
    }//GEN-LAST:event_nodeLogbackSwitchActionPerformed

    private void nodeMsgConnSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeMsgConnSwitchActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_nodeMsgConnSwitchActionPerformed

    private void nodeHealthcheckSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodeHealthcheckSwitchActionPerformed
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (nodeHealthcheckSwitch.isSelected()) {
						String jsonString = nodeInfoTextArea.getText();
						Home.master_teaAgentPort = jsonString.substring(jsonString.indexOf("bw.agent.tea.agent.port") + 27, jsonString.indexOf("bw.agent.tea.agent.port") + 31);
						Component agentHealthCheckTab = new AgentMon_HealthCheckup();
						Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/healthCheck-24.png"));
						nodeMonTabs.addTab("Agent Healthcheck", nodeIcon, agentHealthCheckTab);
						nodeMonTabs.setSelectedIndex(nodeMonTabs.indexOfTab("Agent Healthcheck"));
					} else {
						nodeMonTabs.remove(nodeMonTabs.indexOfTab("Agent Logback"));
					}
				}
			});
		} catch (Exception ex) {
			nodeMonBasePaneLogger.error("Error while processing request in 'nodeHealthcheckSwitchActionPerformed'. Error: \n" + ex.getMessage());
		}
    }//GEN-LAST:event_nodeHealthcheckSwitchActionPerformed

    private void osgiConSwitchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_osgiConSwitchActionPerformed
		// TODO add your handling code here:
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (osgiConSwitch.isSelected()) {
						Component osgiConsoleTab = new NodeMon_OsgiConsole(domainLbl.getText(), appspaceLbl.getText(), appnodeLbl.getText(), appnodePortLbl.getText());
						Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/healthCheck-24.png"));
						nodeMonTabs.addTab("OSGi Console", nodeIcon, osgiConsoleTab);
						nodeMonTabs.setSelectedIndex(nodeMonTabs.indexOfTab("OSGi Console"));
					} else {
						nodeMonTabs.remove(nodeMonTabs.indexOfTab("OSGi Console"));
					}
				}
			});
		} catch (Exception ex) {
			nodeMonBasePaneLogger.error("Error while processing request in 'osgiConSwitchActionPerformed'. Error: \n" + ex.getMessage());
		}
    }//GEN-LAST:event_osgiConSwitchActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AgeHealthCheckExpln;
    private javax.swing.JLabel ageLogExpln;
    private javax.swing.JLabel ageMsgConnExpln;
    private javax.swing.JLabel ageTraExpln;
    public static javax.swing.JLabel appnodeLbl;
    public static javax.swing.JLabel appnodePortLbl;
    public static javax.swing.JLabel appspaceLbl;
    private javax.swing.JLabel domainLbl;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JCheckBox nodeHealthcheckSwitch;
    private javax.swing.JPanel nodeInfo;
    private javax.swing.JLabel nodeInfoBrief;
    private javax.swing.JTextArea nodeInfoTextArea;
    public static javax.swing.JCheckBox nodeLogbackSwitch;
    public javax.swing.JCheckBox nodeMonSwitch;
    public javax.swing.JTabbedPane nodeMonTabs;
    public static javax.swing.JCheckBox nodeMsgConnSwitch;
    public static javax.swing.JCheckBox nodeTraSwitch;
    private javax.swing.JLabel osgiConExpln;
    public static javax.swing.JCheckBox osgiConSwitch;
    private javax.swing.JLabel recMonExpln;
    // End of variables declaration//GEN-END:variables

}
