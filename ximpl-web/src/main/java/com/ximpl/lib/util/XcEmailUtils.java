package com.ximpl.lib.util;

import java.util.regex.Pattern;

/**
 * Email Utilities Class
 */
public class XcEmailUtils {
	private static final Pattern emailPattern = Pattern.compile("^.+@.+(\\.[^\\.]+)+$");
	
	/**
	 * Determine the email is vaild.
	 * @param email
	 * @return
	 */
	public static boolean isValid(String email){
		return XcStringUtils.isNullOrEmpty(email) ? false : emailPattern.matcher(email).matches();
	}
	
	/**
	 * Format email address
	 * ex) David Kim<deokhan@gmail.com>
	 * @param email
	 * @param name
	 * @return
	 */
	public static String formattedMail(String email, String name){
		if (isValid(email)){
			if (XcStringUtils.isNullOrEmpty(name)){
				name = email.substring(0, email.indexOf('@'));
			}
			return String.format("%s<%s>", name, email);
		}
		return null;
	}
}
