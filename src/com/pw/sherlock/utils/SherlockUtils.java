/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.utils;

import com.pw.sherlock.UI.Home;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

/**
 *
 * @author pwankhed
 */
public class SherlockUtils {

	private static HttpURLConnection connection;
	private static final org.apache.log4j.Logger utilLogger = org.apache.log4j.Logger.getLogger(SherlockUtils.class);
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse httpresponse;
	private HttpGet httpget;
	private String url, restResponse, line, jsonString, formattedJson, nodeName, state, PID, httpPort, domainName, appSpaceName, agentName;
	private BufferedReader reader;
	private int status;
	private StringBuilder resposeContent;
	JSONObject obj_JSONArray;
	public String agentHostName, agentPortName, connectTime, readTimeout;

	CloseableHttpClient complexHttpclient;
	CloseableHttpResponse complexHttpresponse;
	BufferedReader complexReader;
	StringBuilder complexResposeContent = new StringBuilder();
	String inputLine, complexRespJsonString, complexFormattedJson;

	String[] responseData = {"responseLine", "responseBody"};

        public String processOSGiCommands(String URL, String callerName) {
		try {
			utilLogger.debug("OSGi Command processor handled request for: " + URL);
			resposeContent = new StringBuilder();
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Home.master_connectTimeout).setSocketTimeout(Home.master_readTimeout).build();
			httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			httpget = new HttpGet(URL);
			httpresponse = httpclient.execute(httpget);

			status = httpresponse.getStatusLine().getStatusCode();

			if (status > 299) {
				utilLogger.error("Problem while processing the HTTP request for OSGi Command. The server sent HTTP code: " + status);
				return "Error";
				//System.exit(0);
			} else {

				//Home.agentStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/available_24px.png")));
				reader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));
				while ((line = reader.readLine()) != null) {
					resposeContent.append(line + "\n");
				}

				httpresponse.close();
				reader.close();

				jsonString = resposeContent.toString();
				formattedJson = jsonString;
			}

			return formattedJson;

		} catch (IOException | NumberFormatException ex) {
			utilLogger.error("Exception occured while processing the HTTP request for OSGi command for URL: '" + URL + "' requested by '" + callerName
					+ "'. Exception details are: " + ex.getMessage());
			utilLogger.debug("Exception occured while processing the HTTP request for OSGi command for URL: '" + URL + "' requested by '" + callerName
					+ "'. Exception details for DEBUG session are given below:\n", ex);
			return "Error";
		} finally {
			Home.refreshConnectionStatus();
			if (httpclient != null && httpresponse != null) {
				try {
					httpclient.close();
					httpresponse.close();
				} catch (IOException ex) {
					utilLogger.error("Exception while closing the HTTP connection:\n" + ex.getMessage());
					utilLogger.debug("Detailed exception while closing the HTTP connection:\n", ex);
				}
			}
		}
	}
	
	public String processSimpleHttpRequest(String URL, String callerName) {
		try {
			utilLogger.debug("HTTP Request processor handled request for: " + URL);
			resposeContent = new StringBuilder();
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Home.master_connectTimeout).setSocketTimeout(Home.master_readTimeout).build();
			//RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(15000).build();
			httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			httpget = new HttpGet(URL);
			httpresponse = httpclient.execute(httpget);

			status = httpresponse.getStatusLine().getStatusCode();

			if (status > 299) {
				utilLogger.error("Problem while processing the simple HTTP request. The server sent HTTP code: " + status);
				return "Error";
				//System.exit(0);
			} else {

				Home.agentStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/available_24px.png")));

				reader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));
				while ((line = reader.readLine()) != null) {
					resposeContent.append(line);
					//System.out.println(line);
					//utilLogger.info(line);
				}

				httpresponse.close();
				reader.close();

				jsonString = resposeContent.toString();

				if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
					formattedJson = jsonString.substring(1, jsonString.length() - 1);
				} else {
					formattedJson = jsonString;
				}
			}

			return formattedJson;

		} catch (IOException | NumberFormatException ex) {
			utilLogger.error("Exception occured while processing the HTTP request for URL: '" + URL + "' requested by '" + callerName
					+ "'. Exception details are: " + ex.getMessage());
			utilLogger.debug("Exception occured while processing the HTTP request for URL: '" + URL + "' requested by '" + callerName
					+ "'. Exception details for DEBUG session are given below:\n", ex);
			Home.agentStatusIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pw/sherlock/resources/unavailable_24px.png")));
			return "Error";
		} finally {
			Home.refreshConnectionStatus();
			if (httpclient != null && httpresponse != null) {
				try {
					httpclient.close();
					httpresponse.close();
				} catch (IOException ex) {
					utilLogger.error("Exception while closing the HTTP connection:\n" + ex.getMessage());
					utilLogger.debug("Detailed exception while closing the HTTP connection:\n", ex);
				}
			}
		}
	}

	public String[] processComplexHttpRequest(String URL) {
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(Home.master_connectTimeout).setSocketTimeout(Home.master_readTimeout).build();
			complexHttpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

			HttpPut httpPut = new HttpPut(URL);
			httpPut.setHeader("Content-type", "application/json");

			String messageBody = "{\"principal\":\"admin\",\"operationName\":\"getAllAgents\",\"sessionId\":\"mytestsessionid\""
					+ ",\"samlToken\":\"\",\"methodType\":\"READ\",\"showStatus\":false,\"objectType\":\"BusinessWorks\",\"key\""
					+ ":\"BusinessWorks\"}";

			StringEntity stringEntity = new StringEntity(messageBody);
			httpPut.setEntity(stringEntity);
			complexHttpresponse = complexHttpclient.execute(httpPut);

			String complexResStatusLine = "<html><b>Response status:</b> " + complexHttpresponse.getStatusLine().getStatusCode() + " " + complexHttpresponse.getStatusLine().getReasonPhrase() + ".</html>";
			responseData[0] = complexResStatusLine;
			
			complexReader = new BufferedReader(new InputStreamReader(complexHttpresponse.getEntity().getContent()));
			while ((inputLine = complexReader.readLine()) != null) {
				complexResposeContent.append(inputLine);
			}

			complexHttpclient.close();
			complexHttpresponse.close();
			complexReader.close();

			complexRespJsonString = complexResposeContent.toString();
			//System.out.println(complexRespJsonString);
	
			if ("".equals(complexRespJsonString)){
			complexFormattedJson = "{\"Response\":\"No response received from BW Agent due to response code "+ complexResStatusLine +"\"}";
			} else if (complexRespJsonString.startsWith("[") && complexRespJsonString.endsWith("]")) {
				complexFormattedJson = complexRespJsonString.substring(1, complexRespJsonString.length() - 1);
			} else {
				complexFormattedJson = complexRespJsonString;
			}
			responseData[1] = complexFormattedJson;

			return responseData;

		} catch (IOException | UnsupportedOperationException ex) {
			utilLogger.error("Exception occured while processing the HTTP request for BW Agent healthcheck"
					+ ". Exception details are: " + ex.getMessage());
			utilLogger.debug("Exception occured while processing the HTTP request for BW Agent healthcheck"
					+ ". Exception details for DEBUG session are given below:\n", ex);
			return responseData;
		} finally {
			Home.refreshConnectionStatus();
			if (complexHttpclient != null && complexHttpresponse != null) {
				try {
					complexHttpclient.close();
					complexHttpresponse.close();
				} catch (IOException ex) {
					utilLogger.error("Exception while closing the complex HTTP connection:\n" + ex.getMessage());
					utilLogger.debug("Detailed exception while closing the complex HTTP connection:\n", ex);
				}
			}
		}
	}

	public static String readFile(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded);
		} catch (IOException IOEX) {
			Logger.getLogger(Home.class
					.getName()).log(Level.SEVERE, null, IOEX);
		} catch (Exception Ex) {
			Logger.getLogger(Home.class
					.getName()).log(Level.SEVERE, null, Ex);
		}
		return null;
	}

}
