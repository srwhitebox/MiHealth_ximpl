package com.ximpl.lib.util;

import javax.servlet.http.HttpServletRequest;

public class XcUrlUtils {
	public static String getHostUrl(HttpServletRequest request){
		
		String hostUrl = request.getRequestURL().toString();
		hostUrl = hostUrl.substring(0, hostUrl.indexOf(request.getRequestURI()));
		String contextPath = request.getContextPath();
		if (!XcStringUtils.isNullOrEmpty(contextPath)){
			hostUrl += contextPath;
		}
		
		return hostUrl;
	}
	
	public static String getPropertyKey(String key){
		if (key.contains(".")){
			return key.substring(key.indexOf(".")+1);
		}else if (key.contains("[")){
			final int start = key.indexOf('\'')+1;
			final int last = key.lastIndexOf('\'');
			return key.substring(start, last).trim();
			
		}
		return null;
	}

}
