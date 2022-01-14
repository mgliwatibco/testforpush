/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import com.pw.sherlock.UI.Home;
import com.pw.sherlock.utils.SherlockUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author pwankhed
 */
public class jsonParser {

	public SherlockUtils utilsObj;
	public String JsonInput, line, jsonString;
	public int status;
	public String[] compNames;
	private JSONObject obj_JSONArray;
	private CloseableHttpClient httpclient;
	private CloseableHttpResponse httpresponse;
	private HttpGet httpget;
	private StringBuilder resposeContent;
	private BufferedReader reader;

	public jsonParser() {

	}

	public String[] getCompNames(String JsonInput) throws IOException {
		utilsObj = new SherlockUtils();
		String restResponse;
		String url = "http://localhost:8079/bw/v1/domains/Sherlock/appspaces/AS1/applications/PW_GetCompInXML.application/1.0/components";

		resposeContent = new StringBuilder();
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(15000).build();
		httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		httpget = new HttpGet(url);
		httpresponse = httpclient.execute(httpget);
		status = httpresponse.getStatusLine().getStatusCode();

		if (status > 299) {
			System.out.print("Problem while processing the simple HTTP request. The server sent HTTP code: " + status);
		} else {
			reader = new BufferedReader(new InputStreamReader(httpresponse.getEntity().getContent()));
			while ((line = reader.readLine()) != null) {
				resposeContent.append(line);
			}

			httpresponse.close();
			reader.close();

			jsonString = resposeContent.toString();

			System.out.println(jsonString);

			JSONObject reader = new JSONObject(jsonString);
                Iterator  iteratorObj = reader.keys();
                //while (iteratorObj.hasNext())
                for (int i=0; iteratorObj.hasNext(); i++)
				{
                    String getJsonObj = (String)iteratorObj.next();
                    System.out.println("KEY: " + "------>" + getJsonObj);	
                }			
		}
		return compNames;
	}

	public static void main(String args[]) {
		jsonParser obj = new jsonParser();
		try {
			obj.getCompNames("test");
		} catch (IOException ex) {
			Logger.getLogger(jsonParser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
