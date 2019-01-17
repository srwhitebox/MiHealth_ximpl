package com.ximpl.lib.utility.weather;

import com.ximpl.lib.util.XcStringUtils;

public enum FEATURE {
	alerts,
	almanac,
	astronomy,
	now,
	today,
	current,
	conditions,
	currenthurricane,
	forecast,
	forecast10day,
	location,
	geolookup,
	history,
	hourly,
	hourly10day,
	planner,
	rawtide,
	satellite,
	tide,
	webcams,
	yesterday,
	aqi,
	
	unknown;
	
	public static FEATURE get(final String feature){
		try{
			return XcStringUtils.isNullOrEmpty(feature) ? conditions : valueOf(feature.toLowerCase());
		}catch(Exception e){
			return unknown;
		}
	}		
}
