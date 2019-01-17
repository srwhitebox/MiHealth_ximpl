package com.ximpl.lib.type;

import com.ximpl.lib.type.COMMAND;

public enum GENDER {
	MALE ('M'),
	FEMALE('F'),
	UNKNOWN('U')
	;
	
	private char gender;
	
	GENDER(char gender){
		this.gender = gender;
	}
	
	public char getChar(){
		return this.gender;
	}

	public static GENDER get(Object gender){
		if (gender instanceof Character)
			return get((Character) gender);
		else if (gender instanceof String)
			return get((String) gender);
		return UNKNOWN;
	}
	
	public static GENDER get(int gender){
		return gender == 0 ? FEMALE : MALE;
	}

	public static GENDER get(String gender){
		return get(gender.charAt(0));
	}
	
	public static GENDER get(Character gender){
		if (gender == null)
			return UNKNOWN;
		switch(Character.toUpperCase(gender)){
		case 'M':
		case '1':
			return MALE;
		case 'F':
		case '0':
			return FEMALE;
		default:
			return UNKNOWN;
		}
	}
}
