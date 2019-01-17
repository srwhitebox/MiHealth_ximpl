package com.ximpl.lib.type;

import java.util.HashMap;

import org.joda.time.DateTime;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ximpl.lib.util.XcBooleanUtils;
import com.ximpl.lib.util.XcJsonUtils;

@SuppressWarnings("serial")
public class XcMap extends HashMap<String, Object>{
	public void setClassId(String classId){
		this.put("classId",  classId);
	}
	
	public String getString(String key){
		Object value = this.get(key);
		if (value == null)
			return null;
		return value instanceof String ? (String)value : value.toString();
	}

	public Integer getInt(String key){
		Object value = this.get(key);
		if (value == null)
			return null;
		return value instanceof Integer ? (Integer)value : Integer.parseInt((String)value);
	}

	public Long getLong(String key){
		Object value = this.get(key);
		if (value == null)
			return null;
		
		return value instanceof Long ? (Long)value : Long.parseLong((String)value);
	}

	public Float getFloat(String key){
		Object value = this.get(key);
		if (value == null)
			return null;
		return value instanceof Float ? (Float)value : Float.parseFloat((String)value);
	}

	public Double getDouble(String key){
		Object value = this.get(key);
		if (value == null)
			return null;
		return value instanceof Double ? (Double)value : Double.parseDouble((String)value);
	}

	public Boolean getBoolean(String key){
		Object value = this.get(key);
		if (value == null)
			return null;
		
		return XcBooleanUtils.isTrue(value);
	}
	
	public DateTime getDateTime(String key){
		return DateTime.parse(key);
	}
	
	public JsonObject getJsonObject(String key){
		return getJsonElement(key).getAsJsonObject();
	}

	public JsonArray getJsonArray(String key){
		return getJsonElement(key).getAsJsonArray();
	}

	public JsonElement getJsonElement(String key){
		return XcJsonUtils.toJsonElement(getString(key));
	}
	
}
