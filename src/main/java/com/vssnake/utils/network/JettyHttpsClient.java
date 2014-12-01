package com.vssnake.utils.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;







import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;










import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class JettyHttpsClient extends AbstractHttpsClient{
	
	static JettyHttpsClient singleton;
	

	HttpClient httpClient;

	@Override
	public void getDataUrl(String inputUrl, Map<String,String> requestProperties,
			final HttpClientReadHandler readHandler) {
		
		HttpGet request = new HttpGet(inputUrl);
		
		

		
		for (Iterator<String> property = requestProperties.keySet().iterator(); property.hasNext();){
            String key = property.next();
  
           request.addHeader(key,requestProperties.get(key));
        }
		try {
			HttpResponse response = httpClient.execute(request);
			
			// Get the response
			BufferedReader rd = new BufferedReader
				  (new InputStreamReader(response.getEntity().getContent()));
			String result = "";	    
			String line = "";
				
			while ((line = rd.readLine()) != null) {
				result = result + line + "\n";
			}
		} catch (IOException e) {
			readHandler.onHttpClientDataReceived(0, result);
		} 
		
	
			
		

	}
	
	
	public void getDataUrl(String inputUrl,Map<String,String> requestProperties) {
		
		HttpGet request = new HttpGet(inputUrl);

		for (Iterator<String> property = requestProperties.keySet().iterator(); property.hasNext();){
            String key = property.next();
  
           request.addHeader(key,requestProperties.get(key));
        }
		try {
			HttpResponse response = httpClient.execute(request);
			
			// Get the response
			BufferedReader rd = new BufferedReader
				  (new InputStreamReader(response.getEntity().getContent()));
			String result ="";	    
			String line = "";
				
			while ((line = rd.readLine()) != null) {
				result = result + line + "\n";
			}
			code = response.getStatusLine().getStatusCode();
			this.result = result;
		} catch (IOException e) {
			code = 0;
			result = e.getMessage();
		} 

	}
	
	public JettyHttpsClient() {
		// Instantiate and configure the SslContextFactory
		SslContextFactory sslContextFactory = new SslContextFactory();
		httpClient = new DefaultHttpClient();
		
	}

	@Override
	public void sendData(String inputUrl, byte[] data,
			HttpClientReadHandler readHandler) {
		// TODO Auto-generated method stub
		
	}
	
	public static JettyHttpsClient getHttpClient(){
    	if (singleton == null){
    		singleton = new JettyHttpsClient();
    	}
    	return singleton;
    }

}
