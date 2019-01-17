package com.ximpl.lib.utility.weather;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ximpl.lib.constant.GeneralConst;
import com.ximpl.lib.constant.LocationConst;
import com.ximpl.lib.net.XcHttpClient;
import com.ximpl.lib.util.XcStringUtils;

/**
 * BreezoMeter API class
 */
public class BreezoMeter {
	private static final String baseUrl = "http://api.breezometer.com/baqi/?";
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
	private String apiKey;	
	
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
	 * Get AQI(Air Quality Index) by GEO Location
	 * If dateTime is null, it'll return current AQI.
	 * If dateTime is defined, it'll return AQI for historical record. 
	 * @param dateTime
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public JsonObject getAqi(DateTime dateTime, String latitude, String longitude){
		URI uri = getUri(dateTime, latitude, longitude);
		if (uri == null)
			return null;
		
		JsonElement jElement = XcHttpClient.getJson(uri);
		
		return jElement == null ? null : jElement.getAsJsonObject();
	}
	
	/**
	 * Return API URI 
	 * @param date
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	private URI getUri(DateTime date, String latitude, String longitude){
		if (XcStringUtils.isNullOrEmpty(apiKey))
			return null;
		
		try {
			if (XcStringUtils.isNullOrEmpty(latitude) || XcStringUtils.isNullOrEmpty(longitude))
				return null;

			final URIBuilder builder =  new URIBuilder(baseUrl).addParameter(GeneralConst.KEY_KEY, apiKey);
			
			builder.addParameter(LocationConst.KEY_LON, longitude).addParameter(LocationConst.KEY_LAT, latitude);
			
			if (date != null){
				builder.addParameter(GeneralConst.KEY_DATETIME, dateTimeFormatter.print(date));
			}
			return builder.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
