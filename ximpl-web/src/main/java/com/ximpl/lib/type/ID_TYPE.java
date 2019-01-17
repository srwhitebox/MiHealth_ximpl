package com.ximpl.lib.type;

import java.util.Locale;

import com.ximpl.lib.type.COMMAND;

public enum ID_TYPE {
	LOGIN_ID	((byte)0x01),
	EMAIL		((byte)0x11),
	NATIONAL_ID	((byte)0x21),
	PASSPORT_NO	((byte)0x31),
	EASYCARD	((byte)0x41),
	NFC			((byte)0x51),
	RFID		((byte)0x52),
	MOBILE_PHONE((byte)0x61),
	TELEPHONE	((byte)0x71),
	
	UNKNOWN		((byte)0xFF),
	;
	
	private byte typeId;
	
	ID_TYPE(byte typeId){
		this.typeId = typeId;
	}
	
	public byte value(){
		return typeId;
	}
	
	public static ID_TYPE get(int typeId){
		byte value = (byte)typeId;
		for(ID_TYPE id : values()){
			if (id.typeId == value)
				return id;
		}
		
		return UNKNOWN;
	}
	
	public static ID_TYPE get(String typeId){
		try{
			return ID_TYPE.valueOf(typeId.toUpperCase(Locale.getDefault()));
		}catch(IllegalArgumentException e){
			return UNKNOWN;
		}
	}
	
}
