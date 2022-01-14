package com.pw.sherlock.OshiImpls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessSort;
import oshi.util.ExecutingCommand;

public class OshiMiscFuncs {

	public final String OSFamily = os.getFamily();
	public final String OSManufacturer = os.getManufacturer();
	public final String OSBitness = os.getBitness() + "";
	public final String OSArch = System.getProperty("os.arch") + "";

	private static Logger oshiLogger = Logger.getLogger(OshiMiscFuncs.class);
	//String versionInfo = PropertyUtils.getVersionDetails();

	public OshiMiscFuncs() {
		// TODO Auto-generated constructor stub
	}

	private static final SystemInfo si = new SystemInfo();
	private static final OperatingSystem os = si.getOperatingSystem();

	public void getUlimit(String cmd) {
		String answer = ExecutingCommand.getFirstAnswer(cmd);
		System.out.println(answer);
	}

	public String getNodeWorkingDir(Integer processId) {
		String workingDir = null;
		try {
			List<OSProcess> procs = Arrays.asList(os.getProcesses(0, ProcessSort.NAME));
			OSProcess p;

			p = procs.get(processId);

			workingDir = p.getPath().trim();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return workingDir;
	}

	public String getMachineName() {
		return si.getOperatingSystem().getNetworkParams().getDomainName();
	}

	public String getOsType() {
		return os.getFamily();
	}

	public String getMachineDetailsBrief() {
		return OSManufacturer + " " + OSFamily + " " + OSBitness + "-bit (" + OSArch + ")";
	}

	public String getPathSeparator() {
		if (OSFamily.contains("Windows")) {
			return "\\";
		} else {
			return "/";
		}
	}

	public static String getSystemInfo() {
		try {
			String systemInfo = "";
			HardwareAbstractionLayer hal = si.getHardware();
			GlobalMemory mem = hal.getMemory();
			//preparing the system summary
			//systemInfo = systemInfo + "--------------------------------------------------\nSystem Details\n--------------------------------------------------\n";
			//systemInfo = systemInfo + "<b>Current system details:</b><br/><br/>";
			//Getting OS Details
			systemInfo = systemInfo + "Manufacturer: " + os.getManufacturer() + " " + os.getFamily() + "<br/>";
			systemInfo = systemInfo + "Version: " + os.getVersion() + " (";
			systemInfo = systemInfo + os.getBitness() + "-bit [" + System.getProperty("os.arch") + "])" + "<br/>";
			//Getting Memory Details
			long memUsed = (mem.getTotal() - mem.getAvailable()) / 1024 / 1024;
			long memTotal = mem.getTotal() / 1024 / 1024;

			//systemInfo = systemInfo + "\n--------------------------------------------------\nMemory Details\n--------------------------------------------------\n";
			//systemInfo = systemInfo + "Total system memory: " + memTotal + " MB\n";
			//systemInfo = systemInfo + "Total used memory: " + memUsed + " MB\n";
			//systemInfo = systemInfo + "Total Free Memory: " + mem.getAvailable() / 1024 / 1024 + " MB\n";
			//Getting HW Details
			//systemInfo = systemInfo + "\n--------------------------------------------------\nHardware Details\n--------------------------------------------------\n";
			//systemInfo = systemInfo + "\n--------------------------------------------------\nHardware Details\n--------------------------------------------------\n";
			//systemInfo = systemInfo + "Hardware details: " + si.getHardware().getComputerSystem().getManufacturer() + " ";
			//systemInfo = systemInfo + si.getHardware().getComputerSystem().getFirmware().getName() + " ";
			//systemInfo = systemInfo + si.getHardware().getComputerSystem().getFirmware().getVersion() + "\n";
			//Getting CPU Details
			//systemInfo = systemInfo + "\n--------------------------------------------------\nCPU Details\n--------------------------------------------------\n";
			CentralProcessor processor = si.getHardware().getProcessor();
			systemInfo = systemInfo + "Total system memory: " + memTotal + " MB<br/>";
			systemInfo = systemInfo + "Number of cores: " + processor.getLogicalProcessorCount() + "<br/>";
			systemInfo = systemInfo + "Vendor: " + processor.getVendor() + "<br/>";
			systemInfo = systemInfo + "Name: " + processor.getName() + "<br/>";
			//systemInfo = systemInfo + "Context switching: " + processor.getContextSwitches() + "<br/>";
			systemInfo = systemInfo + "Hardware details: " + si.getHardware().getComputerSystem().getManufacturer() + " ";
			systemInfo = systemInfo + si.getHardware().getComputerSystem().getFirmware().getName() + " ";
			//systemInfo = systemInfo + si.getHardware().getComputerSystem().getFirmware().getVersion();

			return systemInfo.trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error occurred..";
		}
	}

	public String getAgentConfigFile() {
		String workingDir, agentConfigFile = null, pathSeparator;
		try {
			oshiLogger.info("Searching for BW Agent...");
			List<OSProcess> procs = Arrays.asList(os.getProcesses(0, ProcessSort.NAME));
			OSProcess p;

			if (OSFamily.contains("Windows")) {
				pathSeparator = "\\";
			} else {
				pathSeparator = "/";
			}

			for (int i = 0; i < procs.size(); i++) {
				p = procs.get(i);
				if (p.getName().contains("bwagent")) {
					workingDir = p.getPath();
					agentConfigFile = workingDir.substring(0, workingDir.indexOf("bin")) + "config" + pathSeparator + "bwagent.ini";
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return agentConfigFile;
	}

	public List<String[]> createNodeDetailsRows() {
		try {
			oshiLogger.info("Searching for running appnodes...");
			//System.out.println("Findings appnodes started at: " + System.nanoTime());
			List<String[]> nodeTableRowList = new ArrayList<>();
			List<OSProcess> procs = Arrays.asList(os.getProcesses(0, ProcessSort.NAME));
			OSProcess p = null;
			String pathSeparator = null;

			if (OSFamily.contains("Windows")) {
				pathSeparator = "\\";
			} else {
				pathSeparator = "/";
			}

			for (int i = 0; i < procs.size(); i++) {
				p = procs.get(i);
				if (p.getName().contains("bwappnode")) {
					//Simply converting them to String to put in NodeList
					//System.out.println("Printing the process name: " + p.getName());
					String nodeName = p.getName().substring(10, p.getName().length()).trim();
					String PID = p.getProcessID() + "";
					String Cpu = p.calculateCpuPercent() + "";
					String Mem = p.getVirtualSize() + "";
					String TCount = p.getThreadCount() + "";
					String workingDir = p.getPath();

					String domainSubString = workingDir.substring(workingDir.indexOf("domains") + 8, workingDir.length());
					String domain = domainSubString.substring(0, domainSubString.indexOf(pathSeparator));

					String appspaceSubstring = domainSubString.substring(domainSubString.indexOf("appnodes") + 9, domainSubString.length());
					String appspace = appspaceSubstring.substring(0, appspaceSubstring.indexOf(pathSeparator));

					String[] nodeTableRow = new String[]{nodeName, PID, appspace, domain, workingDir, Mem, Cpu};
					//String[] nodeTableRow = new String[]{p.getName(), PID, appspace, domain, workingDir, "show config", "show TRA"};
					nodeTableRowList.add(nodeTableRow);
					//System.out.println(nodeTableRow.toString());
				}
				//System.out.println(nodeTableRowList.toString());
			}
			
			
			if (nodeTableRowList.isEmpty()) {
				//oshiLogger.error("Unable to find any running appnodes on this machine, startup aborted!");
				//System.exit(0);
				return null;
			} else {
				oshiLogger.info(nodeTableRowList.size() + " running appnode(s) found");
				return nodeTableRowList;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
