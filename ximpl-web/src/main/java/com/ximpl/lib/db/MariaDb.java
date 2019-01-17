package com.ximpl.lib.db;

import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MariaDb {
	/**
	 * Convert JsonObject to dynamic column value for create/update
	 * @param jObject
	 * @return
	 */
	public static String toDynamicColumnForCreate(JsonObject jObject){
		StringBuilder sb = new StringBuilder("COLUMN_CREATE(");
		boolean isFirst = true;
		for(Entry<String, JsonElement> entry : jObject.entrySet()){
			sb.append((isFirst? "'" : ",'") + entry.getKey()+ "\',");
			JsonElement jElement = entry.getValue(); 
			if (jElement.isJsonPrimitive()){
				sb.append("'" + entry.getValue().getAsString() + "\'");
				isFirst = false;
			}
		}
		sb.append(")");
		
		return sb.toString();
	}
}
