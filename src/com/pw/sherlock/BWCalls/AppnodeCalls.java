/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.BWCalls;

import com.pw.sherlock.UI.Home;
import com.pw.sherlock.utils.SherlockUtils;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author pwankhed
 */
public class AppnodeCalls {

	private static HttpURLConnection connection;
	private static final Logger nodeLogger = Logger.getLogger(AgentCalls.class);
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse httpresponse;
	private HttpGet httpget;
	private String url, restResponse, line, jsonString, formattedJson, nodeName, state, PID, httpPort, osgiPort, domainName, agentName;
	private BufferedReader reader;
	private int status;
	private StringBuilder resposeContent;
	private SherlockUtils utilsObj;
	JSONObject obj_JSONArray;

	public String agentHostName, agentPortName, connectTime, readTimeout;

	public int[] getNodeRT_Info(String domainName, String appspaceName, String appnodeName) {

		String agentHostPort = Home.master_agentHostName + ":" + Home.master_agentPortNumber;
		utilsObj = new SherlockUtils();
		int[] nodeUsageDetails = new int[13];
		int toMBs = 1024 * 1024;

		try {
			//http://localhost:8079/bw/v1/agents/processinfo
			String agentURL = "http://" + agentHostPort + "/bw/v1/domains/" + domainName + "/appspaces/" + appspaceName + "/appnodes/" + appnodeName + "/info";
			//String formattedJsonResp = processSimpleHttpRequest(agentURL);
			String formattedJsonResp = utilsObj.processSimpleHttpRequest(agentURL, "getNodeRT_Info");
			//System.out.println(formattedJsonResp + "\n");
			try {
				JSONObject obj_agentJSONArray = new JSONObject(formattedJsonResp);
				nodeUsageDetails[0] = obj_agentJSONArray.getInt("activeThreadCount");
				nodeUsageDetails[1] = (int) (obj_agentJSONArray.getLong("totalMemoryInBytes") / toMBs);
				nodeUsageDetails[2] = obj_agentJSONArray.getInt("upSince");
				nodeUsageDetails[3] = obj_agentJSONArray.getInt("percentMemoryUsed");
				nodeUsageDetails[4] = obj_agentJSONArray.getInt("percentCpuUsed") / 10;
				nodeUsageDetails[5] = obj_agentJSONArray.getInt("systemProcessId");
				nodeUsageDetails[6] = (int) (obj_agentJSONArray.getLong("freeMemoryInBytes") / toMBs);
				nodeUsageDetails[7] = (int) (obj_agentJSONArray.getLong("freeHeapMemoryInBytes") / toMBs);
				nodeUsageDetails[8] = (int) (obj_agentJSONArray.getLong("usedHeapMemoryInBytes") / toMBs);
				nodeUsageDetails[9] = (int) (obj_agentJSONArray.getLong("maxHeapMemoryInBytes") / toMBs);
				nodeUsageDetails[10] = (int) (obj_agentJSONArray.getLong("usedMemoryInBytes") / toMBs);
				nodeUsageDetails[11] = (int) (obj_agentJSONArray.getLong("totalHeapMemoryInBytes") / toMBs);

			} catch (JSONException ex) {
				nodeUsageDetails[0] = 0;
			}
		} catch (Exception e) {
			nodeLogger.error("Exception occured in getAgentRT_Info() call");
			nodeUsageDetails[0] = 0;

		}
		return nodeUsageDetails;
	}

	
	public String getEngineState(String domainName, String appspaceName, String appnodeName) {
		try {
			//http://localhost:8079/bw/v1/domains/Sherlock/appspaces/AS1/appnodes/AN1/engine
			utilsObj = new SherlockUtils();
			String engineStateHttpResponse = utilsObj.processSimpleHttpRequest("http://" + Home.master_agentHostName + ":" + Home.master_agentPortNumber
					+ "/bw/v1/domains/" + domainName + "/appspaces/" + appspaceName + "/appnodes/" + appnodeName + "/engine", "getEngineState");
			
			String formattedResponse;
			try {
				JSONObject obj_agentJSONArray = new JSONObject(engineStateHttpResponse);
				formattedResponse = "BW Engine Details:<br/><br/><b>Name: </b>" + obj_agentJSONArray.getString("engineName") + "<br/>" ;
				formattedResponse = formattedResponse + "<b>Step Count: </b>" +  obj_agentJSONArray.getString("engineStepCount") + "<br/>" ;
				formattedResponse = formattedResponse + "<b>Thread Count: </b>" + obj_agentJSONArray.getString("engineThreadCount") + "<br/>";
				formattedResponse = formattedResponse + "<b>Persistence Mode: </b>" + obj_agentJSONArray.getString("persistenceMode") + "<br/><br/>";
						
				return formattedResponse;

			} catch (JSONException ex) {
				ex.printStackTrace();
				return "Error";
			}
		} catch (Exception e) {
			//nodeLogger.error("Exception occured in getNodeState() call" + e.printStackTrace());
			e.printStackTrace();
			return "Error";
		}
	}
	
	
	public String getNodeState(String nodeHttpPort) {
		try {
			//http://localhost:8070/bw/framework.json/state
			utilsObj = new SherlockUtils();
			String nodeStateHttpResponse = utilsObj.processSimpleHttpRequest("http://" + Home.master_agentHostName + ":" + nodeHttpPort + "/bw/framework.json/state", "getNodeState");
			
			String formattedResponse;
			try {
				JSONObject obj_agentJSONArray = new JSONObject(nodeStateHttpResponse);
				formattedResponse = "" + obj_agentJSONArray.get("details");
				formattedResponse = formattedResponse + " & config state is: '" + obj_agentJSONArray.getString("configState") + "'.</html>";
				formattedResponse = formattedResponse.replace("[\"", "");
				formattedResponse = formattedResponse.replace("\"]", "");
				formattedResponse = formattedResponse.replace("started", "was started");
				
				return formattedResponse;

			} catch (JSONException ex) {
				ex.printStackTrace();
				return "Error";
			}
		} catch (Exception e) {
			//nodeLogger.error("Exception occured in getNodeState() call" + e.printStackTrace());
			e.printStackTrace();
			return "Error";
		}
	}
	
	
		
	public String getNodeConfig(String domainName, String appspaceName, String appnodeName) {
		try {
			//http://localhost:8079/bw/v1/domains/Sherlock/appspaces/AS1/appnodes/AN1/config/content
			utilsObj = new SherlockUtils();
			String nodeConfigHttpResponse = utilsObj.processSimpleHttpRequest("http://" + Home.master_agentHostName + ":" + Home.master_agentPortNumber
					+ "/bw/v1/domains/" + domainName + "/appspaces/" + appspaceName + "/appnodes/" + appnodeName + "/config/content", "getNodeConfig");
			return nodeConfigHttpResponse;
		} catch (Exception e) {
			//nodeLogger.error("Exception occured in getNodeState() call" + e.printStackTrace());
			e.printStackTrace();
			return "Error";
		}
	}
}
