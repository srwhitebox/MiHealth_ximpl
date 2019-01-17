package com.ximpl.lib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XcBooleanUtils {
	@SuppressWarnings("serial")
	private final static List<Object> falseValues = new ArrayList<Object>(){
		{
			Object[] trueValues = {
					'f', 'n', 'x', '0',
					"false", "no", "not", "off", "down", "stop", "cancel", "disable", "disabled", "negative"
				};
			this.addAll(
					Arrays.asList(trueValues)
				);
		}
	};

	public static Boolean isTrue(Object value){
		if (value instanceof Boolean){
			return (Boolean)value;
		} else if (value instanceof Character){
			return !falseValues.contains(Character.toLowerCase((Character)value)); 
		} else if (value instanceof String) {
			return !falseValues.contains(((String)value).toLowerCase());
		} else if (value instanceof Number){
			return ((Number)value).doubleValue() != 0;
		} else 
			return null;
	}
}
