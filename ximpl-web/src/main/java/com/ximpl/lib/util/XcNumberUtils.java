package com.ximpl.lib.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class XcNumberUtils {
	
	public static int toInt(String number){
		return toNumber(number).intValue();
	}
	
	/**
	 * Parse text to float
	 * @param number
	 * @return
	 */
	
	public static float toFloat(String number){
		return toNumber(number).floatValue();
	}
	
	/**
	 * Parse text to number
	 * @param number
	 * @return
	 */
	public static Number toNumber(String number){
		if (XcStringUtils.isNullOrEmpty(number))
			return 0;		
		try {
			return NumberFormat.getNumberInstance(Locale.getDefault()).parse(number);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
