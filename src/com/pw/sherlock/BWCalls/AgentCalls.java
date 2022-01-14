/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.BWCalls;

import com.pw.sherlock.OshiImpls.OshiMiscFuncs;
import com.pw.sherlock.UI.Home;
import com.pw.sherlock.utils.PropertyUtils;
import com.pw.sherlock.utils.SherlockUtils;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author pwankhed
 */
public class AgentCalls {

	private OshiMiscFuncs oshiObj = new OshiMiscFuncs();
	public String agentHostName, agentPortName, connectTime, readTimeout;

	private SherlockUtils utilsObj;
	private Logger agentLogger = Logger.getLogger(AgentCalls.class);
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse httpresponse;
	private HttpGet httpget;
	private String url, restResponse, line, jsonString, formattedJson, nodeName, state, PID, httpPort, osgiPort, domainName, appSpaceName, agentName;
	private BufferedReader reader;
	private int status;
	private StringBuilder resposeContent;
	private JSONObject obj_JSONArray;
	
	public static String agentInfo, agentInfoBrief;

	public AgentCalls() {
	}

	public String pingAndgetAgentGeneralInfo(String agentHostName, String agentPortNumber) {
		resposeContent = new StringBuilder();
		utilsObj = new SherlockUtils();
		String agentGeneralInfo = null;
		try {
			//Fetching the config parameters from sherlock.properties file
			String[] agentMetaData = PropertyUtils.readSherlockConfigFile();
			if ("Error".equals(agentMetaData[0])) {
				agentLogger.error("Error while reading Sherlock's configuration, cannot proceed further");
				JOptionPane.showMessageDialog(null, "Exception occured while processing your request, startup terminated. Please check the logs for more details",
						"Configuration read error", JOptionPane.ERROR_MESSAGE);
				return "Error";
			}

			//Set the parameters to Global variable
			Home.master_agentHostName = agentHostName;
			Home.master_agentPortNumber = agentPortNumber;
			Home.master_connectTimeout = Integer.parseInt(agentMetaData[0].trim());
			Home.master_readTimeout = Integer.parseInt(agentMetaData[1].trim());
			Home.master_pollingInterval = Integer.parseInt(agentMetaData[2].trim());
			Home.master_loadAgentMon = agentMetaData[5].trim();

			//Send HTTP request to BW Agent
			String url = "http://" + agentHostName.trim() + ":" + agentPortNumber.trim() + "/bw/v1/agents/info";
			formattedJson = utilsObj.processSimpleHttpRequest(url, "pingAndgetAgentGeneralInfo");

			if (formattedJson.equals("Error")) {
				agentLogger.error("Received Error from HTTP request processor, cannot proceed further.");
				return "Error";
			} else {
				agentLogger.info("Pinged and got 200 OK from BW Agent. Agent running on: " + Home.master_agentHostName + ":" + Home.master_agentPortNumber);
				try {
					//System.out.println(formattedJson);
					agentInfo = formattedJson;
					obj_JSONArray = new JSONObject(formattedJson);
					Home.master_agentMachineName = obj_JSONArray.getString("machineName").trim();
					Home.master_agentName = obj_JSONArray.getString("name").trim();
					Home.master_tibcoHome = obj_JSONArray.getString("tibcoHome").trim();
					Home.master_tibcoBWVersion = obj_JSONArray.getString("description").trim();
					
					agentGeneralInfo = obj_JSONArray.getString("description") + "<br/><br/>";
					agentGeneralInfo = agentGeneralInfo + "<b>TIBCO Home</b>: " + obj_JSONArray.getString("tibcoHome") + "<br/>";
					agentGeneralInfo = agentGeneralInfo + "<b>OS Process</b>: PID " + obj_JSONArray.getString("pid") + " on machine '" + obj_JSONArray.getString("machineName") + "'<br/>";
					agentGeneralInfo = agentGeneralInfo + "<b>Host & Port</b>: " + Home.master_agentHostName + ":" + Home.master_agentPortNumber + "<br/>";
					agentInfoBrief= agentGeneralInfo;

				} catch (JSONException ex) {
					agentLogger.error("Exception occured while parsing agent's JSON response. Error details are given below:\n", ex);
					JOptionPane.showMessageDialog(null, "Exception occured while processing your request, startup terminated. Please check the logs for more details",
							"JSON Parse error", JOptionPane.ERROR_MESSAGE);
					return "Error";
				}
			}

		} catch (HeadlessException | NumberFormatException ex) {
			agentLogger.error("Exception occured while getting BW Agent details, startup terminated. Error details are given below:\n", ex);
			JOptionPane.showMessageDialog(null, "Exception occured while processing your request, startup terminated. Please check the logs for more details",
					"Error", JOptionPane.ERROR_MESSAGE);
			return "Error";
		} finally {
			if (httpclient != null && httpresponse != null) {
				try {
					httpclient.close();
					httpresponse.close();
				} catch (IOException ex) {
					agentLogger.error("Exception while closing the HTTP connection:\n", ex);
				}
			}
		}
		return agentGeneralInfo;
	}

	public List<String[]> getNodesList() {
		try {
			List<String[]> nodeTableRowList = new ArrayList<>();
			url = "http://" + Home.master_agentHostName + ":" + Home.master_agentPortNumber + "/bw/v1/machines/" + Home.master_agentMachineName + "?status=true";
			restResponse = utilsObj.processSimpleHttpRequest(url, "getNodesList");

			if (restResponse.equals("Error")) {
				agentLogger.error("Received Error from HTTP request processor, cannot proceed further.");
				Home.refreshFailed = true;
				return null;
			} else {
				try {
					obj_JSONArray = new JSONObject(restResponse);
					JSONArray appNodeRefs = obj_JSONArray.getJSONArray("appNodeRefs");

					for (int i = 0; i < appNodeRefs.length(); i++) {
						JSONObject refObject = appNodeRefs.getJSONObject(i);
						restResponse = utilsObj.processSimpleHttpRequest(refObject.optString("href"), "getNodesList_forLoop");
						//System.out.println(refObject.optString("href"));
						obj_JSONArray = new JSONObject(restResponse);

						nodeName = obj_JSONArray.getString("name").trim();
						state = obj_JSONArray.getString("state").trim();
						//PID = obj_JSONArray.getString("pid").trim();
						if (obj_JSONArray.isNull("pid")) {
							PID = "Not Applicable";
						} else {
							PID = obj_JSONArray.getString("pid").trim();
						}
						httpPort = obj_JSONArray.getString("httpPort").trim();
						domainName = obj_JSONArray.getString("domainName").trim();
						appSpaceName = obj_JSONArray.getString("appSpaceName").trim();
						agentName = obj_JSONArray.getString("agentName").trim();

						String[] nodeTableRow = new String[]{nodeName, state, PID, appSpaceName, domainName, agentName, httpPort};
						nodeTableRowList.add(nodeTableRow);
					}

				} catch (JSONException ex) {
					agentLogger.error("Error details are given below:\n", ex);
				}
			}
			if (nodeTableRowList.isEmpty()) {
				return null;
			} else {
				return nodeTableRowList;
			}
		} catch (Exception ex) {
			agentLogger.error("Exception occured while loading appnode list, startup terminated. Error details are given below:\n", ex);
			Home.refreshFailed = true;
			return null;
		}
	}

	public int[] getAgentRT_Info() {

		String agentUrl = Home.master_agentHostName + ":" + Home.master_agentPortNumber;
		utilsObj = new SherlockUtils();
		//int[] agentUsageDetails = new int[8];
		int[] agentUsageDetails = new int[8];
		int toMBs = 1024 * 1024;

		try {
			//http://localhost:8079/bw/v1/agents/processinfo
			String agentURL = "http://" + agentUrl + "/bw/v1/agents/processinfo";
			//String formattedJsonResp = processSimpleHttpRequest(agentURL);
			String formattedJsonResp = utilsObj.processSimpleHttpRequest(agentURL, "getAgentRT_Info");
			//System.out.println(formattedJsonResp + "\n");
			try {
				JSONObject obj_agentJSONArray = new JSONObject(formattedJsonResp);
				agentUsageDetails[0] = obj_agentJSONArray.getInt("upSince");
				agentUsageDetails[1] = obj_agentJSONArray.getInt("percentCpuUsed") / 100;
				agentUsageDetails[2] = (int) (obj_agentJSONArray.getLong("totalMemoryInBytes") / toMBs);
				//System.out.println(obj_agentJSONArray.getInt("totalMemoryInBytes"));
				agentUsageDetails[3] = obj_agentJSONArray.getInt("activeThreadCount");
				agentUsageDetails[4] = obj_agentJSONArray.getInt("systemProcessId");
				agentUsageDetails[5] = obj_agentJSONArray.getInt("freeMemoryInBytes") / toMBs;
				agentUsageDetails[6] = obj_agentJSONArray.getInt("percentMemoryUsed");
				agentUsageDetails[7] = obj_agentJSONArray.getInt("usedMemoryInBytes") / toMBs;

			} catch (JSONException ex) {
				agentUsageDetails[0] = 0;
			}
		} catch (Exception e) {
			agentLogger.error("Exception occured in getAgentRT_Info() call. Error details are given below:\n", e);
			agentUsageDetails[0] = 0;
		}
		return agentUsageDetails;

	}
//Class End	
}
