package com.ximpl.lib.gson.typeAdapter;

import java.lang.reflect.Type;
import java.util.TimeZone;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CompatibleTimeZoneTypeAdapter implements JsonSerializer<TimeZone>, JsonDeserializer<TimeZone>{

	public TimeZone deserialize(JsonElement json, Type typeOfTarget, JsonDeserializationContext context) throws JsonParseException {
		String timeZone = json.getAsString();
		return TimeZone.getTimeZone(timeZone);
	}

	public JsonElement serialize(TimeZone timeZone, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(timeZone.getID());
	}

}
