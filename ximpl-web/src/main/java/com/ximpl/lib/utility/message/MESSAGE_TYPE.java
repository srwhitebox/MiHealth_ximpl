package com.ximpl.lib.utility.message;

import com.ximpl.lib.util.XcStringUtils;

public enum MESSAGE_TYPE {
	marketing,
	transactional,
	
	unknown;
	
	public static MESSAGE_TYPE get(final String feature){
		try{
			return XcStringUtils.isNullOrEmpty(feature) ? transactional : valueOf(feature.toLowerCase());
		}catch(Exception e){
			return unknown;
		}
	}		

}
