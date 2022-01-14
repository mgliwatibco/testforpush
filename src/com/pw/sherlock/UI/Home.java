/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.UI;

import com.pw.sherlock.UI.appnode.NodeMon_BasePane;
import com.pw.sherlock.UI.agent.AgentMon_BasePane;
import com.pw.sherlock.utils.CustomTabbedPaneUI_V2;
import com.pw.sherlock.charts.NodeMemMonitor;
import com.pw.sherlock.OshiImpls.OshiMiscFuncs;
import com.pw.sherlock.BWCalls.AgentCalls;
import com.pw.sherlock.utils.PropertyUtils;
import com.pw.sherlock.utils.SherlockUtils;
import com.sun.jna.platform.win32.Secur32;
import com.sun.jna.platform.win32.Secur32Util;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import java.util.TimeZone;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;

/**
 *
 * @author pwankhed
 */
public class Home extends javax.swing.JFrame {

	String[] tblHead = {"Appnode", "State", "PID", "Appspace", "Domain", "Agent", "Http Port"};
	//One time string values
	public static String master_agentConnMode, master_agentName, master_agentHostName, master_agentPortNumber,
			master_agentMachineName, master_loadAgentMon, master_tibcoHome, master_tibcoBWVersion, master_teaAgentPort;
	public static int master_connectTimeout, master_readTimeout, master_pollingInterval;
	public static boolean refreshFailed = false;
	private final String welcomeLbl = "<html> Sherlock is runtime time monitoring tool for TIBCO ActiveMatrix BusinessWorks 6.x. "
			+ "It is standalone desktop application that communicates with BW Agent and serves the requests. "
			+ "<br><br>Sherlock simply relies on BW Agent API server therefore it can work as long as the BW Agent is running. "
			+ "<br/><br/>Make a note that, this is not an administration tool and therefore it cannot start or stop the runtime entities, "
			+ "it will merely help you to keep an close eye on them.<br/><br/>With Sherlock, you can:<br/><br/>- Monitor the runtime entities"
			+ " i.e. Appnodes & Agents<br/>- Monitor threads, CPU usage, OS memory, heap memory usage etc. You can also collect the thread-dumps"
			+ " from appnodes<br/>- Along with monitoring, users can configure ruleset on appnodes to change the logging levels, statistic collection"
			+ "<br/>- Sherlock provide users interactive platform to interact with deployed application<br/>- Much more...<br/><br/>So, to get you started,"
			+ " enter the details of BW Agent. You will need is BW Agent's host and port, these details can be found in bwagent.ini file or on TEA UI >"
			+ " Agent Info page.</br></br></html>";

	private static final Logger homeLogger = Logger.getLogger(Home.class);
	private final String versionInfo = PropertyUtils.getVersionDetails();
	private final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");

	Timer homeTimer = new Timer();

	//Sherlock Class references
	private SherlockUtils utilsObj;
	OshiMiscFuncs OshiObj = new OshiMiscFuncs();
	AgentCalls agentObj = new AgentCalls();
	NodeMemMonitor memMonitor = new NodeMemMonitor();
	//Component welcomeTab = new WelcomeTabPanel();

	public static JTabbedPane consoleBaseTabbedPane;
	int ntabs = 0;

	public Home() {
		initComponents();
		sherlockInit();
	}

	private void sherlockInit() {
		try {
			//Cosmetics
			setExtendedState(MAXIMIZED_BOTH);
			welcomeTabLbl.setText(welcomeLbl);
			welcomeTabLbl.setSize(welcomeTabLbl.getPreferredSize());

			homeLogger.info("\n\n==================================\n"
					+ "Sherlock - Runtime monitoring and statistics collection tool for TIBCO ActiveMatrix BusinessWorks 6\n"
					+ versionInfo + "\n==================================\n");
		
			nodeScrollPane.setVisible(false);
			maxMonitor.setVisible(false);
			homeLogger.info("Hostname: " + OshiObj.getMachineName() + " (" + OshiObj.getMachineDetailsBrief() + ")");
			lblUserName.setText(getUserName() + ", " + formatter.format(new Date()) + " (" + TimeZone.getDefault().getID()
					+ " - " + TimeZone.getDefault().getDisplayName() + ")");
			lblSessInfo_sysDetails.setText(OshiObj.getMachineName() + " | " + OshiObj.getMachineDetailsBrief());
			sherlockVersion.setText(versionInfo);

			String[] agentHostPortData = PropertyUtils.readHostPort();
			if ("Error".equals(agentHostPortData[0])) {
				homeLogger.error("Error while reading Sherlock's configuration, cannot proceed further");
				System.exit(0);
			}
			agentHost_tf.setText(agentHostPortData[0]);
			agentPort_tf.setValue(Integer.parseInt(agentHostPortData[1].trim()));

			sysInfoLbl.setText("<html>" + OshiMiscFuncs.getSystemInfo() + "</html>");
		
			agentStatusIcon.setText(" Unknown");
			statusBarLbl.setText("Waiting for user inputs..");
			homeLogger.info("Waiting for user inputs to connect to BW Agent...");
		} catch (Exception ex) {
			homeLogger.error("Exception occured while refreshing appnode table. Error details are given below:", ex);
			JOptionPane.showMessageDialog(null, "Exception occured while refreshing appnodes table. Please check the logs for more details",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void connectAgentButtonAction() {
		try {
			//nodeScrollPane.setVisible(false);
			maxMonitor.setVisible(true);
			//Ping the agent and load the basic details
			String agentDetails = agentObj.pingAndgetAgentGeneralInfo(agentHost_tf.getText().trim(), agentPort_tf.getValue().toString().trim());
			if (agentDetails.equals("Error")) {
				JOptionPane.showMessageDialog(null, "Exception occured while contacting BW Agent, unable to proceed further. Check logs for more details", "Connect error", JOptionPane.ERROR_MESSAGE);
			} else {
				//sysInfoLbl.setText("<html>" + OshiMiscFuncs.getSystemInfo() + "<br/><br/>" + agentDetails + "</html>");
				sysInfoLbl.setText("<html>" + OshiMiscFuncs.getSystemInfo() + "</html>");
				sysInfoLbl.setSize(sysInfoLbl.getPreferredSize());
				//Dynamically creating TabbedPane. This is very base JTabbed Pane with custom UI, this will add and hold all the monitor tabs for agent and nodes
				consoleBaseTabbedPane = new JTabbedPane();
				UIManager.put("TabbedPane.tabInsets", new Insets(2, 1, 2, 20));
				consoleBaseTabbedPane.setUI(new CustomTabbedPaneUI_V2());
				//creating an object i.e. agentInfoPane from AgentMon_BasePane (this is again a pre-designed JtabbedPane on JPanel for agent info + charts etc.)
				Component agentInfoPane = new AgentMon_BasePane();
				Icon agentIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/agent-24.png"));
				consoleBaseTabbedPane.addTab(master_agentName + " (Agent)    ", agentIcon, agentInfoPane);
				consoleBaseTabbedPane.revalidate();
				consoleBaseTabbedPane.repaint();

				//Adding newly created JTabbledPane i.e. consoleBaseTabbedPane to base JPatenl scrollPane i.e. consoleBasScrollPane
				consoleBasScrollPane.setLayout(new BorderLayout());
				consoleBasScrollPane.remove(tempWelcomePane);
				consoleBasScrollPane.add(BorderLayout.CENTER, consoleBaseTabbedPane);

				//Loading appnodes in JTable	
				loadNodeList();

				//Load the Domain structure in JTree View
				LoadDomainTree();

				//Start the auto-refresh of Node list. At present triggers after every 10 seconds
				//triggerHomeTimerJob();
				agentStatusIcon.setText("");

				connModeLbl.setText(connModeLbl.getText());
				homeLogger.info("Sherlock is ready");
				statusBarLbl.setText(" Sherlock is ready");
				homePgBarUpdate(false);
			}
		} catch (Exception ex) {
			homeLogger.error("Exception occured while refreshing appnode table. Error details are given below:", ex);
			JOptionPane.showMessageDialog(null, "Exception occured while refreshing appnodes table. Please check the logs for more details",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void LoadDomainTree() {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
				utilsObj = new SherlockUtils();
				String url = "http://" + Home.master_agentHostName + ":" + Home.master_agentPortNumber + "/bw/v1/browse/domains?full=true&status=true";
				String restResponse = utilsObj.processSimpleHttpRequest(url, "LoadDomainTree");	
				//homeLogger.info(restResponse);
				homeLogger.info("Domain is loaded in tree");
				}
			});

		} catch (Exception ex) {
			homeLogger.error("Exception occured while loading the domain structure in the Tree view. Error details are given below:", ex);
			JOptionPane.showMessageDialog(null, "Exception occured while loading the domain structure in UI. Please check the logs for more details",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void homePgBarUpdate(boolean visibility) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				pgBarImageLbl.setVisible(visibility);
				pgBarImageLbl.revalidate();
				pgBarImageLbl.repaint();
			}
		});
	}

	public void refreshNodeList(String callerName) {
		List<String[]> nodeTableRows = agentObj.getNodesList();
		try {
			if (nodeTableRows == null) {
				//ManualRefresh
				if (callerName.equals("ManualRefresh")) {
					JOptionPane.showMessageDialog(null, "No appnodes found, appnode list cannot be refreshed", "Warning: No appnodes found", JOptionPane.WARNING_MESSAGE);
				} else {
					statusBarLbl.setText("No appnodes found, appnode list cannot be refreshed");
				}
				homeLogger.warn("No appnodes found, node list will not be refreshed.");
				DefaultTableModel dtm = new DefaultTableModel(tblHead, 0);
				nodeTable.setModel(dtm);
				nodeTable.removeAll();
				refreshFailed = true;
			} else {
				refreshFailed = false;
				DefaultTableModel dtm = new DefaultTableModel(tblHead, 0);
				nodeTable.setModel(dtm);
				nodeTableRows.forEach((nodeTableRow) -> {
					dtm.addRow(nodeTableRow);
				});
				nodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				//setting table as read-only
				nodeTable.setDefaultEditor(Object.class, null);

				for (int column = 0; column < nodeTable.getColumnCount(); column++) {
					TableColumn tableColumn = nodeTable.getColumnModel().getColumn(column);
					int preferredWidth = tableColumn.getMinWidth();
					int maxWidth = tableColumn.getMaxWidth();

					for (int row = 0; row < nodeTable.getRowCount(); row++) {
						TableCellRenderer cellRenderer = nodeTable.getCellRenderer(row, column);
						Component c = nodeTable.prepareRenderer(cellRenderer, row, column);
						int width = c.getPreferredSize().width + nodeTable.getIntercellSpacing().width;
						preferredWidth = Math.max(preferredWidth, width);

						//  We've exceeded the maximum width, no need to check other rows
						if (preferredWidth >= maxWidth) {
							preferredWidth = maxWidth;
							break;
						}
					}
					tableColumn.setPreferredWidth(preferredWidth + 50);
				}
			}
		} catch (Exception ex) {
			refreshFailed = true;
			homeLogger.error("Exception occured while refreshing appnode table. Error details are given below:", ex);
		}
	}

	public static void refreshConnectionStatus() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				agentStatusIcon.revalidate();
				agentStatusIcon.repaint();
			}
		});
	}

	public void triggerHomeTimerJob() {
		TimerTask homeTimerTask = new TimerTask() {
			@Override
			public void run() {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!refreshFailed) {
							refreshNodeList("AutTask");
						} else {
							homeTimer.cancel();
							homeLogger.warn("Scheduler to refresh node list has been terminated");
						}
						refreshConnectionStatus();
					}
				});
			}
		};
		homeTimer.scheduleAtFixedRate(homeTimerTask, 0, 10000);
	}

	public void loadNodeList() {
		try {
			homeLogger.info("Searching for appnodes...");
			List<String[]> nodeTableRows = agentObj.getNodesList();
			if (nodeTableRows == null) {
				homeLogger.error("No appnodes (BW Runtime) found. Either there exist no appnode or there was a connectivity issue. If there are appnodes, "
						+ "Try re-starting the BW Agent or try to connect to BW administative entities via other tools to verify the connectivity. Cannot "
						+ "proceed further, startup terminated.");
				JOptionPane.showMessageDialog(null, "No appnodes found, startup terminated. Cannot proceed further, please check the logs for more details", "Error: No appnodes found", JOptionPane.WARNING_MESSAGE);
				System.exit(0);
				//DefaultTableModel dtm = new DefaultTableModel(tblHead, 0);
				//nodeTable.setModel(dtm);
			} else {
				DefaultTableModel dtm = new DefaultTableModel(tblHead, 0);
				nodeTable.setModel(dtm);

				nodeTableRows.forEach((nodeTableRow) -> {
					dtm.addRow(nodeTableRow);
				});
				nodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				//setting table as read-only
				nodeTable.setDefaultEditor(Object.class, null);
				homeLogger.info(nodeTable.getRowCount() + " appnode(s) found");

				for (int column = 0; column < nodeTable.getColumnCount(); column++) {
					TableColumn tableColumn = nodeTable.getColumnModel().getColumn(column);
					int preferredWidth = tableColumn.getMinWidth();
					int maxWidth = tableColumn.getMaxWidth();

					for (int row = 0; row < nodeTable.getRowCount(); row++) {
						TableCellRenderer cellRenderer = nodeTable.getCellRenderer(row, column);
						Component c = nodeTable.prepareRenderer(cellRenderer, row, column);
						int width = c.getPreferredSize().width + nodeTable.getIntercellSpacing().width;
						preferredWidth = Math.max(preferredWidth, width);

						//  We've exceeded the maximum width, no need to check other rows
						if (preferredWidth >= maxWidth) {
							preferredWidth = maxWidth;
							break;
						}
					}
					tableColumn.setPreferredWidth(preferredWidth + 50);
				}

				nodeScrollPane.setVisible(true);
				//Following code will generate the pop-up menu and set the event handlers
				nodeListPopupMenu.add(refreshList);
				nodeListPopupMenu.add(addMonitor);
				nodeTable.setComponentPopupMenu(nodeListPopupMenu);
			}
		} catch (Exception ex) {
			homeLogger.error("Exception occured while loading appnode table. Error details are given below:", ex);
			JOptionPane.showMessageDialog(null, "Exception occured while loading appnodes table. Please check the logs for more details",
					"Error", JOptionPane.ERROR_MESSAGE);
		}

		nodeTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table = (JTable) mouseEvent.getSource();
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					homePgBarUpdate(true);
					addNodeToMonitorConsole();
				}
			}
		});

		//Setting up the pop-up menu on node JTable
		nodeListPopupMenu.addPopupMenuListener(new PopupMenuListener() {
			//Setting this up to display the pop-up menu on right click of given node table row.
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						int rowAtPoint = nodeTable.rowAtPoint(SwingUtilities.convertPoint(nodeListPopupMenu, new Point(0, 0), nodeTable));
						if (rowAtPoint > -1) {
							nodeTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
						}
					}
				});
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				//JOptionPane.showMessageDialog(null, "Invisible","", JOptionPane.WARNING_MESSAGE);
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				// TODO Auto-generated method stub
				//JOptionPane.showMessageDialog(null, "Cancelled","", JOptionPane.WARNING_MESSAGE);
			}
		});

		refreshList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshNodeList("ManualRefresh");
				if (refreshFailed) {
					JOptionPane.showMessageDialog(null, "Exception occured while refreshing appnodes table. Please check the logs for more details",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		addMonitor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNodeToMonitorConsole();
			}
		});
	}

	private void addNodeToMonitorConsole() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					boolean tabExist = false;
					String nodeName = nodeTable.getValueAt(nodeTable.getSelectedRow(), nodeTable.getColumnModel().getColumnIndex("Appnode")).toString().trim();
					String appspaceName = nodeTable.getValueAt(nodeTable.getSelectedRow(), nodeTable.getColumnModel().getColumnIndex("Appspace")).toString().trim();
					String domainName = nodeTable.getValueAt(nodeTable.getSelectedRow(), nodeTable.getColumnModel().getColumnIndex("Domain")).toString().trim();
					String nodePort = nodeTable.getValueAt(nodeTable.getSelectedRow(), nodeTable.getColumnModel().getColumnIndex("Http Port")).toString().trim();
					String tabNameFromRow = nodeName + " (" + domainName + ")    ";

					try {
						if (!"Running".equals(nodeTable.getValueAt(nodeTable.getSelectedRow(), nodeTable.getColumnModel().getColumnIndex("State")).toString().trim())) {
							JOptionPane.showMessageDialog(null, "BW Appnode is not in 'Running' state, cannot initiate monitoring services", "Warning", JOptionPane.WARNING_MESSAGE);
							homePgBarUpdate(false);
						} else {
							for (int i = 0; i < consoleBaseTabbedPane.getTabCount(); i++) {
								if (tabNameFromRow.equals(consoleBaseTabbedPane.getTitleAt(i))) {
									tabExist = true;
									break;
								}
							}

							if (tabExist) {
								JOptionPane.showMessageDialog(null, "This node is already being monitored, cannot have duplicate tabs", "Warning", JOptionPane.WARNING_MESSAGE);
								homePgBarUpdate(false);
							} else {
								Component nodeInfoPane = new NodeMon_BasePane(domainName, appspaceName, nodeName, nodePort);
								Icon nodeIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/nodeMonitor_tabIco_v1.png"));
								consoleBaseTabbedPane.addTab(nodeName + " (" + domainName + ")    ", nodeIcon, nodeInfoPane);
								consoleBaseTabbedPane.revalidate();
								consoleBaseTabbedPane.repaint();
								homePgBarUpdate(false);
							}
						}
					} catch (Exception ex) {
						homePgBarUpdate(false);
						homeLogger.error("Exception occured while adding monitor tab for an appnode'" + nodeName + "' from domain '" + domainName
								+ "'. Error details are given below:", ex);
						JOptionPane.showMessageDialog(null, "Exception occured while adding monitor tab for an appnode'" + nodeName + "' from domain '" + domainName
								+ "'. Please check logs for more details.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception ex) {
					homePgBarUpdate(false);
					homeLogger.error("Exception occured while adding monitor tab. Error details are given below:" + ex.getMessage());
					JOptionPane.showMessageDialog(null, "Error occured while adding monitor tab for an appnode."
							+ ". Please check logs for more details.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	private String getUserName() {
		String sysUserName = null;
		try {
			sysUserName = Secur32Util.getUserNameEx(Secur32.EXTENDED_NAME_FORMAT.NameDisplay);
			return sysUserName;
		} catch (Exception ex) {
			return sysUserName = System.getProperty("user.name");
		} finally {
			if (sysUserName == null) {
				return sysUserName = System.getProperty("user.name");
			} else {
				return sysUserName;
			}
		}
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        lblSessInfo_sysDetails = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        sherlockLogo = new javax.swing.JLabel();
        sherlockVersion = new javax.swing.JLabel();
        profilePic = new javax.swing.JLabel();
        midPanel = new javax.swing.JPanel();
        nodeScrollPane = new javax.swing.JScrollPane();
        nodeTable = new javax.swing.JTable();
        midPanSplitter = new javax.swing.JSplitPane();
        leftSplitScrollPane = new javax.swing.JSplitPane();
        InfoBasePanel = new javax.swing.JPanel();
        sysInfoLbl = new javax.swing.JLabel();
        domBrowserPane1 = new javax.swing.JPanel();
        domBrowserBanr1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        treeAndBannerPanel = new javax.swing.JPanel();
        domBrowserPane = new javax.swing.JPanel();
        domBrowserBanr = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        treeScrollPane = new javax.swing.JScrollPane();
        domainTree = new javax.swing.JTree();
        rightSplitScrollPane = new javax.swing.JScrollPane();
        consolePlatformPanel = new javax.swing.JPanel();
        consoleTitlePane = new javax.swing.JPanel();
        monitorLbl = new javax.swing.JLabel();
        maxMonitor = new javax.swing.JButton();
        consoleBasScrollPane = new javax.swing.JPanel();
        tempWelcomePane = new javax.swing.JPanel();
        welcomeTabLbl = new javax.swing.JLabel();
        agentHost_tf_lbl = new javax.swing.JLabel();
        agentHost_tf = new javax.swing.JTextField();
        agentPort_tf_lbl = new javax.swing.JLabel();
        agentPort_tf = new javax.swing.JSpinner();
        connect2Agent = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        progressPanel = new javax.swing.JPanel();
        pgBarImageLbl = new javax.swing.JLabel();
        statusBarLbl = new javax.swing.JLabel();
        agentStatusIcon = new javax.swing.JLabel();
        connModeLbl = new javax.swing.JLabel();
        sherlockMenu = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        prefMenuItem = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sherlock - Home");

        headerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Session Details"));

        lblSessInfo_sysDetails.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblSessInfo_sysDetails.setText("miscOsDetails");

        lblUserName.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lblUserName.setText("greeting_User");
        lblUserName.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        sherlockLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/Sherlock-Banner_v1.png"))); // NOI18N

        sherlockVersion.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        sherlockVersion.setText("Version 1.0 Build 1");

        profilePic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilePic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/profilePic_v1.png"))); // NOI18N

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(profilePic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSessInfo_sysDetails)
                    .addComponent(lblUserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sherlockLogo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sherlockVersion, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addComponent(sherlockLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sherlockVersion)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(profilePic)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addComponent(lblSessInfo_sysDetails)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUserName)))
                .addContainerGap())
        );

        nodeScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Appnodes"));

        nodeTable.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        nodeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {}
            },
            new String [] {

            }
        ));
        nodeScrollPane.setViewportView(nodeTable);

        midPanSplitter.setDividerLocation(270);

        leftSplitScrollPane.setDividerLocation(30);
        leftSplitScrollPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        leftSplitScrollPane.setResizeWeight(0.5);
        leftSplitScrollPane.setAutoscrolls(true);

        sysInfoLbl.setText("Loading...");
        sysInfoLbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        domBrowserPane1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        domBrowserBanr1.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        domBrowserBanr1.setText("System Information");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/system-info-24.png"))); // NOI18N

        javax.swing.GroupLayout domBrowserPane1Layout = new javax.swing.GroupLayout(domBrowserPane1);
        domBrowserPane1.setLayout(domBrowserPane1Layout);
        domBrowserPane1Layout.setHorizontalGroup(
            domBrowserPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, domBrowserPane1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(domBrowserBanr1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        domBrowserPane1Layout.setVerticalGroup(
            domBrowserPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(domBrowserBanr1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout InfoBasePanelLayout = new javax.swing.GroupLayout(InfoBasePanel);
        InfoBasePanel.setLayout(InfoBasePanelLayout);
        InfoBasePanelLayout.setHorizontalGroup(
            InfoBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(domBrowserPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(InfoBasePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sysInfoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addContainerGap())
        );
        InfoBasePanelLayout.setVerticalGroup(
            InfoBasePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoBasePanelLayout.createSequentialGroup()
                .addComponent(domBrowserPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sysInfoLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        leftSplitScrollPane.setLeftComponent(InfoBasePanel);

        domBrowserPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        domBrowserBanr.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        domBrowserBanr.setText(" Domain Browser");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/org-chart-24.png"))); // NOI18N

        javax.swing.GroupLayout domBrowserPaneLayout = new javax.swing.GroupLayout(domBrowserPane);
        domBrowserPane.setLayout(domBrowserPaneLayout);
        domBrowserPaneLayout.setHorizontalGroup(
            domBrowserPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, domBrowserPaneLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(domBrowserBanr, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
        );
        domBrowserPaneLayout.setVerticalGroup(
            domBrowserPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(domBrowserBanr, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        domainTree.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("pwankhed-p52");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("Domains");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Sherlock");
        javax.swing.tree.DefaultMutableTreeNode treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Appspaces");
        javax.swing.tree.DefaultMutableTreeNode treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("AS1");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Appnodes");
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("AN1");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("AN2");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("Domain1");
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Appspaces");
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("AS1");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode4 = new javax.swing.tree.DefaultMutableTreeNode("Appnodes");
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("AN1");
        treeNode4.add(treeNode5);
        treeNode5 = new javax.swing.tree.DefaultMutableTreeNode("AN2");
        treeNode4.add(treeNode5);
        treeNode3.add(treeNode4);
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        domainTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        domainTree.setAutoscrolls(true);
        treeScrollPane.setViewportView(domainTree);

        javax.swing.GroupLayout treeAndBannerPanelLayout = new javax.swing.GroupLayout(treeAndBannerPanel);
        treeAndBannerPanel.setLayout(treeAndBannerPanelLayout);
        treeAndBannerPanelLayout.setHorizontalGroup(
            treeAndBannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
            .addComponent(domBrowserPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        treeAndBannerPanelLayout.setVerticalGroup(
            treeAndBannerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, treeAndBannerPanelLayout.createSequentialGroup()
                .addComponent(domBrowserPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE))
        );

        leftSplitScrollPane.setRightComponent(treeAndBannerPanel);

        midPanSplitter.setLeftComponent(leftSplitScrollPane);

        monitorLbl.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        monitorLbl.setText("BusinessWorks Runtime monitor");

        maxMonitor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/maximize-window-24.png"))); // NOI18N
        maxMonitor.setToolTipText("Maximize");
        maxMonitor.setBorder(null);
        maxMonitor.setBorderPainted(false);
        maxMonitor.setContentAreaFilled(false);
        maxMonitor.setFocusPainted(false);
        maxMonitor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxMonitorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout consoleTitlePaneLayout = new javax.swing.GroupLayout(consoleTitlePane);
        consoleTitlePane.setLayout(consoleTitlePaneLayout);
        consoleTitlePaneLayout.setHorizontalGroup(
            consoleTitlePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consoleTitlePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(monitorLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1209, Short.MAX_VALUE)
                .addComponent(maxMonitor)
                .addContainerGap())
        );
        consoleTitlePaneLayout.setVerticalGroup(
            consoleTitlePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consoleTitlePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(consoleTitlePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxMonitor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monitorLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        welcomeTabLbl.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        welcomeTabLbl.setForeground(new java.awt.Color(51, 51, 51));
        welcomeTabLbl.setText("test");
        welcomeTabLbl.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        agentHost_tf_lbl.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        agentHost_tf_lbl.setText("Agent HTTP Interface:");

        agentHost_tf.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        agentHost_tf.setToolTipText("Enter BW Agent's HTTP Interface or IP Address. Default is localhost");

        agentPort_tf_lbl.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        agentPort_tf_lbl.setText("Agent HTTP Port:");

        agentPort_tf.setToolTipText("Enter BW Agent's port number. Default is 8079");
        agentPort_tf.setEditor(new javax.swing.JSpinner.NumberEditor(agentPort_tf, "#"));

        connect2Agent.setText("Connect");
        connect2Agent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connect2AgentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tempWelcomePaneLayout = new javax.swing.GroupLayout(tempWelcomePane);
        tempWelcomePane.setLayout(tempWelcomePaneLayout);
        tempWelcomePaneLayout.setHorizontalGroup(
            tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempWelcomePaneLayout.createSequentialGroup()
                .addGroup(tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(welcomeTabLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(tempWelcomePaneLayout.createSequentialGroup()
                        .addGroup(tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(agentPort_tf_lbl)
                            .addComponent(agentHost_tf_lbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tempWelcomePaneLayout.createSequentialGroup()
                                .addComponent(agentPort_tf, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(connect2Agent, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(agentHost_tf))
                        .addGap(0, 1117, Short.MAX_VALUE)))
                .addContainerGap())
        );
        tempWelcomePaneLayout.setVerticalGroup(
            tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempWelcomePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(welcomeTabLbl)
                .addGap(18, 18, 18)
                .addGroup(tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agentHost_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(agentHost_tf_lbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tempWelcomePaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agentPort_tf_lbl)
                    .addComponent(connect2Agent)
                    .addComponent(agentPort_tf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(319, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout consoleBasScrollPaneLayout = new javax.swing.GroupLayout(consoleBasScrollPane);
        consoleBasScrollPane.setLayout(consoleBasScrollPaneLayout);
        consoleBasScrollPaneLayout.setHorizontalGroup(
            consoleBasScrollPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consoleBasScrollPaneLayout.createSequentialGroup()
                .addComponent(tempWelcomePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        consoleBasScrollPaneLayout.setVerticalGroup(
            consoleBasScrollPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consoleBasScrollPaneLayout.createSequentialGroup()
                .addComponent(tempWelcomePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout consolePlatformPanelLayout = new javax.swing.GroupLayout(consolePlatformPanel);
        consolePlatformPanel.setLayout(consolePlatformPanelLayout);
        consolePlatformPanelLayout.setHorizontalGroup(
            consolePlatformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(consoleTitlePane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(consolePlatformPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consoleBasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        consolePlatformPanelLayout.setVerticalGroup(
            consolePlatformPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(consolePlatformPanelLayout.createSequentialGroup()
                .addComponent(consoleTitlePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(consoleBasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        rightSplitScrollPane.setViewportView(consolePlatformPanel);

        midPanSplitter.setRightComponent(rightSplitScrollPane);

        javax.swing.GroupLayout midPanelLayout = new javax.swing.GroupLayout(midPanel);
        midPanel.setLayout(midPanelLayout);
        midPanelLayout.setHorizontalGroup(
            midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(midPanSplitter)
            .addComponent(nodeScrollPane)
        );
        midPanelLayout.setVerticalGroup(
            midPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(midPanelLayout.createSequentialGroup()
                .addComponent(nodeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midPanSplitter))
        );

        statusPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new java.awt.Dimension(1581, 27));

        pgBarImageLbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/new-pgbar.gif"))); // NOI18N

        statusBarLbl.setText("jLabel1");

        javax.swing.GroupLayout progressPanelLayout = new javax.swing.GroupLayout(progressPanel);
        progressPanel.setLayout(progressPanelLayout);
        progressPanelLayout.setHorizontalGroup(
            progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, progressPanelLayout.createSequentialGroup()
                .addComponent(statusBarLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pgBarImageLbl)
                .addContainerGap())
        );
        progressPanelLayout.setVerticalGroup(
            progressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pgBarImageLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(statusBarLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        connModeLbl.setText("Agent Connectivity:");

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(progressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(connModeLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(agentStatusIcon))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(progressPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(connModeLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(agentStatusIcon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        fileMenu.setText("File");

        jMenuItem1.setText("Add Tab");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        sherlockMenu.add(fileMenu);

        jMenu2.setText("Edit");
        sherlockMenu.add(jMenu2);

        jMenu3.setText("Tools");

        prefMenuItem.setText("Preferences");
        prefMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prefMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(prefMenuItem);

        sherlockMenu.add(jMenu3);

        jMenu4.setText("Help");
        sherlockMenu.add(jMenu4);

        setJMenuBar(sherlockMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(midPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1745, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(midPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void prefMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prefMenuItemActionPerformed
		Preferences dialog = new Preferences();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
    }//GEN-LAST:event_prefMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
		// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void connect2AgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connect2AgentActionPerformed
		// TODO add your handling code here:
		connectAgentButtonAction();
		repaint();
		revalidate();
    }//GEN-LAST:event_connect2AgentActionPerformed

    private void maxMonitorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxMonitorActionPerformed
		// TODO add your handling code here:

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (nodeScrollPane.isVisible()) {
					nodeScrollPane.setVisible(false);
					leftSplitScrollPane.setVisible(false);
					midPanSplitter.setDividerLocation(0.06);
					Icon buttonIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/restore-window-24.png"));
					maxMonitor.setIcon(buttonIcon);
					maxMonitor.setToolTipText("Restore");
				} else {
					nodeScrollPane.setVisible(true);
					leftSplitScrollPane.setVisible(true);
					midPanSplitter.setDividerLocation(0.13);
					Icon buttonIcon = new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/maximize-window-24.png"));
					maxMonitor.setIcon(buttonIcon);
					maxMonitor.setToolTipText("Maximize");
				}

//				AgentMon_ChartsUI.CpuMemSplitPane.setDividerLocation(0.50);
//				AgentMon_ChartsUI.CpuMemSplitPane.repaint();
//				AgentMon_ChartsUI.CpuMemSplitPane.revalidate();
				midPanel.repaint();
				midPanel.revalidate();
				//AgentMon_BasePane.CpuMemSplitPane.setDividerLocation(0.50);
				//NodeMon_ChartsUI.CpuMemSplitPane.setDividerLocation(0.50);
				//NodeMon_ChartsUI.DialsSplitPane.setDividerLocation(0.50);
				//NodeMon_ChartsUI.DialsSplitPane.repaint();
				//NodeMon_ChartsUI.CpuMemSplitPane.repaint();
			}
		});
    }//GEN-LAST:event_maxMonitorActionPerformed

	public static void main(String args[]) {
		//System.out.println("In main method now...");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			OshiMiscFuncs oshiObjMain = new OshiMiscFuncs();
//
//			if (oshiObjMain.getOsType().toUpperCase().equals("WINDOWS")) {
//				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			} else {
//				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//					if ("Metal".equals(info.getName())) {
//						javax.swing.UIManager.setLookAndFeel(info.getClassName());
//						break;
//					}
//				}
//			}
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
			homeLogger.error(e);

		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Home().setVisible(true);
			}
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel InfoBasePanel;
    private javax.swing.JTextField agentHost_tf;
    private javax.swing.JLabel agentHost_tf_lbl;
    private javax.swing.JSpinner agentPort_tf;
    private javax.swing.JLabel agentPort_tf_lbl;
    public static javax.swing.JLabel agentStatusIcon;
    private javax.swing.JLabel connModeLbl;
    private javax.swing.JButton connect2Agent;
    private javax.swing.JPanel consoleBasScrollPane;
    private javax.swing.JPanel consolePlatformPanel;
    private javax.swing.JPanel consoleTitlePane;
    private javax.swing.JLabel domBrowserBanr;
    private javax.swing.JLabel domBrowserBanr1;
    private javax.swing.JPanel domBrowserPane;
    private javax.swing.JPanel domBrowserPane1;
    private javax.swing.JTree domainTree;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JLabel lblSessInfo_sysDetails;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JSplitPane leftSplitScrollPane;
    private javax.swing.JButton maxMonitor;
    private javax.swing.JSplitPane midPanSplitter;
    private javax.swing.JPanel midPanel;
    private javax.swing.JLabel monitorLbl;
    private javax.swing.JScrollPane nodeScrollPane;
    private javax.swing.JTable nodeTable;
    public static javax.swing.JLabel pgBarImageLbl;
    private javax.swing.JMenuItem prefMenuItem;
    private javax.swing.JLabel profilePic;
    private javax.swing.JPanel progressPanel;
    private javax.swing.JScrollPane rightSplitScrollPane;
    private javax.swing.JLabel sherlockLogo;
    private javax.swing.JMenuBar sherlockMenu;
    private javax.swing.JLabel sherlockVersion;
    public static javax.swing.JLabel statusBarLbl;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JLabel sysInfoLbl;
    private javax.swing.JPanel tempWelcomePane;
    private javax.swing.JPanel treeAndBannerPanel;
    private javax.swing.JScrollPane treeScrollPane;
    private javax.swing.JLabel welcomeTabLbl;
    // End of variables declaration//GEN-END:variables

	//PW Added these controls manually
	private JPopupMenu nodeListPopupMenu = new JPopupMenu();
	private JMenuItem addMonitor = new JMenuItem("Monitor");
	private JMenuItem refreshList = new JMenuItem("Refresh");
}

//	public static void refreshConnectionStatus() {
//		SwingUtilities.invokeLater(new Runnable() {
//			@Override
//			public void run() {
//				agentStatusIcon.revalidate();
//				agentStatusIcon.repaint();
//			}
//		});
//	}
