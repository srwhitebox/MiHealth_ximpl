package com.ximpl.lib.type;

import com.ximpl.lib.type.TOKEN_TYPE;
import com.ximpl.lib.util.XcStringUtils;


public enum TOKEN_TYPE {
	REQUEST_ACTIVATION	('a'),
	LOGGED_IN			('l'),
	STUDENT_LOGGED_IN	('s'),
	REMEMBER_LOGGED_IN  ('r'),
	RESET_PASSWORD		('p'),
	UNKNOWN				('u')
	;
	
	private char value;
	
	TOKEN_TYPE(char value){
		this.value = value;
	}
	
	public char value(){
		return value;
	}

	public static TOKEN_TYPE get(char value){
		for(TOKEN_TYPE tokenType : values()){
			if (tokenType.value == value)
				return tokenType;
		}
		return UNKNOWN;
	}
	
	public static TOKEN_TYPE get(String value){
		if (XcStringUtils.isNullOrEmpty(value))
			return UNKNOWN;
		return get(value.charAt(0));
	}
}
