package com.ximpl.lib.type;

import com.ximpl.lib.type.COMMAND;

public enum COMMAND {
	update,
	edit,
	save,
	delete,
	remove,
	enable,
	disable,
	activate,
	unknown
	;
	public static COMMAND get(String paramName){
		try{
			return COMMAND.valueOf(paramName.toLowerCase());
		}catch(Exception e){
			return unknown;
		}
	}
}
