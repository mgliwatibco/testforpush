package com.pw.sherlock.utils;

import com.pw.sherlock.BWCalls.AgentCalls;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;

public class PropertyUtils {

	private static Logger propFileRedLogger = Logger.getLogger(AgentCalls.class);

	public static String getVersionDetails() {
		String versionInfo = null;
		try {
			InputStream fis = PropertyUtils.class.getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(fis);
			versionInfo = prop.getProperty("productVersion") + " " + prop.getProperty("productBuild") + ", " + prop.getProperty("releaseDate");
			fis.close();
		} catch (FileNotFoundException fnfe) {
			propFileRedLogger.error("Sherlock did not find it's configuration file named 'sherlock.properties file'. Stacktrace is as below:\n", fnfe);
		} catch (IOException ioe) {
			propFileRedLogger.error(ioe);
		}
		return versionInfo;
	}

	public static String[] readHostPort() {
		String[] agentHostPort = new String[]{"Error"};
		InputStreamReader fis = null;
		try {
			fis = new InputStreamReader(new FileInputStream("config/sherlock.properties"), "UTF-8");
			Properties prop = new Properties();
			prop.load(fis);
			agentHostPort = new String[]{
				prop.getProperty("bw.agent.hostname"),
				prop.getProperty("bw.agent.port")};
			fis.close();
			return agentHostPort;
		} catch (FileNotFoundException fnfe) {
			propFileRedLogger.error("Sherlock did not find it's configuration file named 'sherlock.properties file'. Stacktrace is as below:\n", fnfe);
			return agentHostPort;
		} catch (UnsupportedEncodingException UeE) {
			propFileRedLogger.error(UeE);
			return agentHostPort;
		} catch (IOException IoE) {
			propFileRedLogger.error(IoE);
			return agentHostPort;
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	public static String[] readSherlockConfigFile() {
		String[] agentMetaData = new String[]{"Error"};
		InputStreamReader fis = null;
		try {
			fis = new InputStreamReader(new FileInputStream("config/sherlock.properties"), "UTF-8");
			Properties prop = new Properties();
			prop.load(fis);
			agentMetaData = new String[]{
				prop.getProperty("sherlock.connect.timeout"),
				prop.getProperty("sherlock.read.timeout"),
				prop.getProperty("sherlock.polling.interval"),
				prop.getProperty("bw.agent.hostname"),
				prop.getProperty("bw.agent.port"),
				prop.getProperty("sherlock.agent.monitoring")};
			fis.close();
			return agentMetaData;
		} catch (FileNotFoundException fnfe) {
			propFileRedLogger.error("Sherlock did not find it's configuration file named 'sherlock.properties file'. Stacktrace is as below:\n", fnfe);
			return agentMetaData;
		} catch (UnsupportedEncodingException UeE) {
			propFileRedLogger.error(UeE);
			return agentMetaData;
		} catch (IOException IoE) {
			propFileRedLogger.error(IoE);
			return agentMetaData;
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	

//End of class
}
