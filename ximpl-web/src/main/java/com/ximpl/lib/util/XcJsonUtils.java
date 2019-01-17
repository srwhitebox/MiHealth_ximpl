package com.ximpl.lib.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import java.util.TimeZone;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.api.client.json.Json;
import com.google.api.client.util.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ximpl.lib.gson.typeAdapter.CompatibleTimeZoneTypeAdapter;
import com.ximpl.lib.type.XcMap;

public class XcJsonUtils {
	public static String getString(JsonObject jInfo, String property){
		JsonElement element = getProperty(jInfo, property); 
		return element == null ? null : element.getAsString();
	}
	
	public static int getInt(JsonObject jInfo, String property, int defaultValue){
		JsonElement element = getProperty(jInfo, property); 
		return element == null ? defaultValue : element.getAsInt();
	}

	public static long getLong(JsonObject jInfo, String property, long defaultValue){
		JsonElement element = getProperty(jInfo, property); 
		return element == null ? defaultValue : element.getAsLong();
	}

	public static float getFloat(JsonObject jInfo, String property, float defaultValue){
		JsonElement element = getProperty(jInfo, property); 
		return element == null ? defaultValue : element.getAsFloat();
	}

	public static Date getDate(JsonObject jInfo, String property, Date defaultValue){
		JsonElement element = getProperty(jInfo, property); 
		return element == null ? defaultValue : new Date(element.getAsLong());
	}

	public static URL getUrl(JsonObject jInfo, String property){
		JsonElement element = getProperty(jInfo, property); 
		try {
			return element == null ? null : new URL(element.getAsString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JsonElement getProperty(JsonObject jInfo, String property){
		if (jInfo == null){
			jInfo = new JsonObject();
		}
		return jInfo == null ? null : jInfo.get(property);
	}

	/**
	 * Determine the JSON object is valie ( not null or not empty)
	 * @param jObject
	 * @return
	 */
	public static boolean isValid(JsonObject jObject){
		return !isNullOrEmpty(jObject);
	}
	
	public static boolean isNullOrEmpty(JsonObject jObject){
		return jObject == null || jObject.entrySet().isEmpty();
	}
	
   public static JsonElement toJson(byte[] content){
	   if (content == null)
		   return null;
	   ByteArrayInputStream is = new ByteArrayInputStream(content);
	   return new JsonParser().parse(new InputStreamReader(is));
    }
	   
	public static void setProperty(JsonObject jInfo, String property, Object value){
		if (jInfo == null){
			jInfo = new JsonObject();
		}
		
		if (value instanceof String)
			jInfo.addProperty(property, (String)value);
		else if (value instanceof Integer)
			jInfo.addProperty(property, (Integer)value);
		else if (value instanceof Integer)
			jInfo.addProperty(property, (Integer)value);
		else if (value instanceof Long)
			jInfo.addProperty(property, (Long)value);
		else if (value instanceof Float)
			jInfo.addProperty(property, (Float)value);
		else if (value instanceof URL)
			jInfo.addProperty(property, ((URL)value).toString());
		else if (value instanceof Date)
			jInfo.addProperty(property, ((Date)value).getTime());
		else if (value instanceof JsonElement)
			jInfo.add(property, (JsonElement)value);
	}
	
	public static List<String> toList(JsonObject json){
		if (json==null || json.isJsonNull())
			return null;
		
		ArrayList<String> list = new ArrayList<String>();
		for(Entry<String, JsonElement> entry : json.entrySet()){
			JsonElement element = entry.getValue();
			if (element.isJsonNull())
				continue;
			list.add(entry.getKey());
			list.add(element.getAsString());
		}
		return list;
	}
	
	public static Gson getGson(){
		return Converters.registerDateTime(new GsonBuilder().registerTypeAdapter(TimeZone.class, new CompatibleTimeZoneTypeAdapter())).create();
	}
	
	public static JsonElement toJsonElement(String jsonSource){
		try{
			return XcStringUtils.isNullOrEmpty(jsonSource) ? null : new JsonParser().parse(jsonSource);
		}catch(JsonSyntaxException e){
			return null;
		}
	}
	
	public static JsonElement toJsonElement(HttpServletRequest request){
		try {
			return toJsonElement(request.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static JsonElement toJsonElement(InputStream is){
		return toJsonElement(new InputStreamReader(is, Charsets.UTF_8));
	}
	
	public static JsonElement toJsonElement(Reader jsonReader){
		try{
			return jsonReader == null ? null : new JsonParser().parse(jsonReader);
		}catch(JsonSyntaxException e){
			return null;
		}
	}
	
	
	public static JsonElement toJsonElement(Object value){
		if (value == null)
			return null;
		
		return getGson().toJsonTree(value);
	}
	public static XcMap toMap(String jsonSource){
		if (XcStringUtils.isNullOrEmpty(jsonSource))
			return null;
		
		JsonElement jElement = toJsonElement(jsonSource);
		return toMap(jElement.getAsJsonObject());
	}

	public static XcMap toMap(JsonObject jObject){
		if (jObject == null || jObject.entrySet().isEmpty())
			return null;
		
		return new Gson().fromJson(jObject, new TypeToken<HashMap<String, Object>>() {}.getType());
	}
	
	public static Map<String, Boolean> toBooleanMap(String jsonSource){
		if (XcStringUtils.isNullOrEmpty(jsonSource))
			return null;
		
		JsonElement jElement = toJsonElement(jsonSource);
		return jElement.isJsonObject() ? toBooleanMap(jElement.getAsJsonObject()) : null;
	}

	public static Map<String, Boolean> toBooleanMap(JsonObject jObject){
		if (jObject == null || jObject.entrySet().isEmpty())
			return null;
		
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		
		for(Entry<String, JsonElement> jEntry : jObject.entrySet()){
			boolean value = false;
			if (jEntry.getValue().isJsonPrimitive()){
				JsonPrimitive jPrimitive = jEntry.getValue().getAsJsonPrimitive();
				if (jPrimitive.isBoolean())
					value = jPrimitive.getAsBoolean();
				else if (jPrimitive.isNumber())
					value = jPrimitive.getAsDouble() != 0;
				else if (jPrimitive.isString())
					value = XcBooleanUtils.isTrue(jPrimitive.getAsString());
			}				 
			map.put(jEntry.getKey(), value);
		}
		return map;
	}
	
	public static <T> T toObject(HttpServletRequest request, Class<T> dataClass){
		try {
			return toObject(toJsonElement(request.getInputStream()), dataClass);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object toObject(InputStream is, Class<?> dataClass){
		return toObject(toJsonElement(is), dataClass);
	}
	
	public static <T> T toObject(String jElement, Class<T> dataClass){
		return toObject(toJsonElement(jElement), dataClass);
	}
	
	public static <T> T toObject(JsonElement jElement, Class<T> dataClass){
		return jElement == null ? null : getGson().fromJson(jElement, dataClass);
	}

	public static String toJsonCreate(JsonObject jObject){
		if (jObject == null || jObject.isJsonNull()|| jObject.entrySet().isEmpty())
			return null;
		
		StringBuilder sb = new StringBuilder("COLUMN_CREATE(");
		boolean added = false;
		for (Entry<String, JsonElement> entry : jObject.entrySet()){
			if (added)
				sb.append(",");
			sb.append("'");
			sb.append(entry.getKey());
			sb.append("', '");
			JsonElement jElement = entry.getValue();
			if (jElement.isJsonPrimitive())
				sb.append(entry.getValue().getAsString());
			else
				sb.append(entry.getValue().toString());
			sb.append("'");
			if (!added)
				added = true;
		}
		sb.append(")");
		return sb.toString();
	}
}
