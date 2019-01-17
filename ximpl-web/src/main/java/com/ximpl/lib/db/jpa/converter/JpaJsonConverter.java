package com.ximpl.lib.db.jpa.converter;

import javax.persistence.AttributeConverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ximpl.lib.util.XcJsonUtils;

public class JpaJsonConverter implements AttributeConverter<JsonObject, String>{

	public String convertToDatabaseColumn(JsonObject jObject) {
		return jObject == null ? null : jObject.toString();
	}

	public JsonObject convertToEntityAttribute(String jObject) {
		JsonElement jElement = XcJsonUtils.toJsonElement(jObject);
		return jElement == null ? null : jElement.getAsJsonObject();
	}

}
