package com.ximpl.lib.net;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.repackaged.com.google.common.base.Strings;

public class XcCookies {
	public static Map<String, String> parse(String cookies){
		if (Strings.isNullOrEmpty(cookies))
			return null;
		
		final HashMap<String, String> map = new HashMap<String, String>();
		Pattern cookiePattern = Pattern.compile("([^=]+)=([^\\;]*);?\\s?");
        Matcher matcher = cookiePattern.matcher(cookies);
        while (matcher.find()) {
        	String key = matcher.group(1);
            String value = matcher.group(2);
            map.put(key, value);
        }
		return map;
	}
}
