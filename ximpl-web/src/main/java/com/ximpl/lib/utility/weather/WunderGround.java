package com.ximpl.lib.utility.weather;

import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ximpl.lib.net.XcHttpClient;
import com.ximpl.lib.util.XcStringUtils;

/**
 * WunderGround Weather API service Handler
 */
public class WunderGround {
	private static final String PARAM_IP = "geo_ip";
	private static final String QUERY_AUTO_IP = "autoip";
	private static final String FORMAT_JSON = ".json";
	
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("'history_'yyyyMMdd");
	
	private String apiKey;
	
	private static final String baseUrl = "http://api.wunderground.com/api";

	/**
	 * Set API Key
	 * @param apiKey
	 */
	public void setApiKey(String apiKey){
		this.apiKey = apiKey;
	}

	/**
	 * Get API Key
	 * @return
	 */
	public String getApiKey(){
		return this.apiKey;
	}
	
	/**
	 * return Weather information
	 * @param feature
	 * @param date
	 * @param latitude
	 * @param longitude
	 * @param ip
	 * @param airport
	 * @param pwd
	 * @return
	 */
	public JsonElement get(FEATURE feature, DateTime date, String latitude, String longitude, String ip, String airport, String pwd){
		String urlRef = Joiner.on('/').join(baseUrl, apiKey); // , dateTimeFormatter.print(date), "q", formattedQuery(query));
		String query = formattedQuery(latitude, longitude, ip, airport, pwd);
		String featurePath = null;
		if (date != null)
			featurePath = dateTimeFormatter.print(date);	
		else if (feature == FEATURE.unknown || feature == FEATURE.now || feature == FEATURE.today || feature == FEATURE.current)
			featurePath = FEATURE.conditions.name();
		else if (feature == FEATURE.location)
			featurePath = FEATURE.geolookup.name();
		else
			featurePath = feature.name();
		
		urlRef = Joiner.on('/').join(urlRef, featurePath, "q", query);
		
		return getUrlResponse(urlRef, ip);
	}


	/**
	 * return location information
	 * @param latitude
	 * @param longitude
	 * @param ip
	 * @param airport
	 * @param pwd
	 * @return
	 */
	public JsonElement getLocation(String latitude, String longitude, String ip, String airport, String pwd){
		String urlRef = Joiner.on('/').join(baseUrl, apiKey); // , dateTimeFormatter.print(date), "q", formattedQuery(query));
		String query = formattedQuery(latitude, longitude, ip, airport, pwd);
		
		urlRef = Joiner.on('/').join(urlRef, FEATURE.geolookup.name(), "q", query);
		
		return getUrlResponse(urlRef, ip);
	}
	
	/**
	 * Return response
	 * @param urlRef
	 * @param ip
	 * @return
	 */
	private JsonElement getUrlResponse(String urlRef, String ip){
		try {
			final URIBuilder builder =  new URIBuilder(urlRef);
			if (XcStringUtils.isValid(ip))
				builder.addParameter(PARAM_IP, ip);
			
			return XcHttpClient.getJson(builder.build());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Return formatted query for wunderground.com
	 * @param latitude
	 * @param longitude
	 * @param ip
	 * @param airport
	 * @param pwd
	 * @return
	 */
	private String formattedQuery(String latitude, String longitude, String ip, String airport, String pwd){
		String query = null;
		if (XcStringUtils.isValid(latitude) && XcStringUtils.isValid(longitude)){
			query = latitude + ',' + longitude ;
		}else if (XcStringUtils.isValid(ip)){
			query = QUERY_AUTO_IP;
		}else if (XcStringUtils.isValid(airport)){
			query = airport;
		}else if (XcStringUtils.isValid(pwd)){
			query = "pwd:" + pwd;
		}else{
			query = QUERY_AUTO_IP;
		}
		query += FORMAT_JSON;
		
		return query;
	}
}
