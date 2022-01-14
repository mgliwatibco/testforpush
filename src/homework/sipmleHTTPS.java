/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

public class sipmleHTTPS {
	public static void main(String[] args) {
		        String httpURL = "http://localhost:8227/bw/framework.json/osgi?command=td";
		        try {
			        URL myUrl = new URL(httpURL);
			        HttpURLConnection conn = (HttpURLConnection)myUrl.openConnection();
			        InputStream is = conn.getInputStream();
			        InputStreamReader isr = new InputStreamReader(is);
			        BufferedReader br = new BufferedReader(isr);
	
			        String inputLine;
	
			        while ((inputLine = br.readLine()) != null) {
			            System.out.println(inputLine);
			        }
	
						br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}