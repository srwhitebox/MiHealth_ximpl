package com.ximpl.lib.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.JsonElement;
import com.ximpl.lib.util.XcJsonUtils;

public class XcHttpClient {
	
	public static JsonElement getJson(String urlRef){
		try {
			return getJson(new URL(urlRef));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static JsonElement getJson(URL url){
		return XcJsonUtils.toJsonElement(getInputStream(url));
	}
	
	public static JsonElement getJson(URI uri){
		return XcJsonUtils.toJsonElement(getInputStream(uri));
	}
	
	public static InputStream getInputStream(String urlRef){
		try{
			return getInputStream(new URL(urlRef));
		}catch(MalformedURLException e){
			
		}
		
		return null;
	}
	
	public static InputStream getInputStream(URI uri){
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpGet getRequest = new HttpGet(uri);
		try {
			org.apache.http.HttpResponse response = client.execute(getRequest);
			return response.getEntity().getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream getInputStream(URL url){
		try {			
	        URLConnection connection = url.openConnection();
	        return connection.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory(new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {
				request.setParser(new JsonObjectParser(new JacksonFactory()));
			}
	    });

	public static Object toObject(String encodedUrl, Class<?> dataClass){
		try {
			HttpRequest request = requestFactory.buildGetRequest(new GenericUrl(encodedUrl));
			HttpResponse response = request.execute(); 
			return response.parseAs(dataClass);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}	
}
