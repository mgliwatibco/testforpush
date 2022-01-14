/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author pwankhed
 */
public class ClientMultiThreaded extends Thread {

	CloseableHttpClient httpClient;
	HttpGet httpget;
	int id;
	private static final Logger homeLogger = Logger.getLogger(ClientMultiThreaded.class);

	public ClientMultiThreaded(CloseableHttpClient httpClient, HttpGet httpget, int id) {
		this.httpClient = httpClient;
		this.httpget = httpget;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			//Executing the request
			CloseableHttpResponse httpresponse = httpClient.execute(httpget);

			//Displaying the status of the request.
			//System.out.println("status of thread "+id+":"+httpresponse.getStatusLine());
			homeLogger.info("status of thread " + id + ":" + httpresponse.getStatusLine());
			//Retrieving the HttpEntity and displaying the no.of bytes read
			HttpEntity entity = httpresponse.getEntity();
			
			if (entity != null) {
				//System.out.println("Bytes read by thread thread " + id + ": " + EntityUtils.toByteArray(entity).length);
				homeLogger.info("Bytes read by thread thread " + id + ": " + EntityUtils.toByteArray(entity).length);
			}
			httpresponse.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
	}

	public static void main(String[] args) throws Exception {

		//Creating the Client Connection Pool Manager by instantiating the PoolingHttpClientConnectionManager class.
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

		//Set the maximum number of connections in the pool
		connManager.setMaxTotal(50);

		//Create a ClientBuilder Object by setting the connection manager
		HttpClientBuilder clientbuilder = HttpClients.custom().setConnectionManager(connManager);

		//Build the CloseableHttpClient object using the build() method.
		CloseableHttpClient httpclient = clientbuilder.build();

		//Creating the HttpGet requests
		HttpGet httpget1 = new HttpGet("http://localhost:8047/bw/tdump.json");
		HttpGet httpget2 = new HttpGet("http://localhost:8048/bw/tdump.json");
		HttpGet httpget3 = new HttpGet("http://localhost:8049/bw/tdump.json");
		HttpGet httpget4 = new HttpGet("http://localhost:8050/bw/tdump.json");
		
		
		//Creating the Thread objects
		ClientMultiThreaded thread1 = new ClientMultiThreaded(httpclient, httpget1, 1);
		ClientMultiThreaded thread2 = new ClientMultiThreaded(httpclient, httpget2, 2);
		ClientMultiThreaded thread3 = new ClientMultiThreaded(httpclient, httpget3, 3);
		ClientMultiThreaded thread4 = new ClientMultiThreaded(httpclient, httpget4, 4);

		//Starting all the threads
		thread1.start();
		//thread1.join();

		thread2.start();
		//thread2.join();

		thread3.start();
		//thread3.join();

		thread4.start();
		//thread4.join();
		
		//connManager.close();
		//httpclient.close();

	}
}
