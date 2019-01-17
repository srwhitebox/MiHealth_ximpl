package com.ximpl.lib.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.encrypt.Encryptors;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

/**
 * String utilities
 */
public class XcStringUtils {
	/**
	 * Validate string, if it is null or empty or all white, it'll return false.
	 * @param text
	 * @return
	 */
	public static boolean isValid(String text){
		return !isNullOrEmpty(text, true);
		
	}
	
	/**
	 * Determine the text is null or empty or all white space.
	 * @param text
	 * @return
	 */
	public static boolean isNullOrEmpty(String text){
		return isNullOrEmpty(text, true);
	}
	
	/**
	 * Determine whether the text is null or empty depend on trimming options.
	 * @param text
	 * @param trimAllWhiteSpace
	 * @return
	 */
	public static boolean isNullOrEmpty(String text, boolean trimAllWhiteSpace){
		if (Strings.isNullOrEmpty(text))
			return true;
		
		if (trimAllWhiteSpace)
			text = CharMatcher.WHITESPACE.removeFrom(text);
		
		return Strings.isNullOrEmpty(text);	
	}

	/**
	 * Convert csv string to integer list
	 * 
	 */
	public static List<Integer> convertToIntList(String csvString){
		if (csvString == null)
			return null;
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0, a = 0, n = csvString.length(); i < n; i++) {
		    switch(csvString.charAt(i)) {
		        case ',': list.add(a); a = 0; break;
		        case ' ': break;
		        case '0': a = a * 10; break;
		        case '1': a = a * 10 + 1; break;
		        case '2': a = a * 10 + 2; break;
		        case '3': a = a * 10 + 3; break;
		        case '4': a = a * 10 + 4; break;
		        case '5': a = a * 10 + 5; break;
		        case '6': a = a * 10 + 6; break;
		        case '7': a = a * 10 + 7; break;
		        case '8': a = a * 10 + 8; break;
		        case '9': a = a * 10 + 9; break;
		        default: throw new AssertionError();
		    }
		}
		return list;
	}
	
	public static String toHexString(byte[] bytes){
		return bytes == null ? null : BaseEncoding.base16().lowerCase().encode(bytes);
	}
	
	public static byte[] toBytes(String hexString){
		return isNullOrEmpty(hexString) ? null : BaseEncoding.base16().lowerCase().decode(hexString);
	}
	
	public static String toEncryptedBytes(String tokenPassword, String tokenSalt, byte[] source){
		return Encryptors.queryableText(tokenPassword, tokenSalt).encrypt(toHexString(source));
	}
	
	public static byte[] toDecryptedBytes(String tokenPassword, String tokenSalt, String source){
		final String hexString = Encryptors.queryableText(tokenPassword, tokenSalt).decrypt(source);
		return toBytes(hexString);
	}

	public static String toEncrypted(String tokenPassword, String tokenSalt, String source){
		return Encryptors.queryableText(tokenPassword, tokenSalt).encrypt(source);
	}
	
	public static String toDecrypted(String tokenPassword, String tokenSalt, String source){
		return Encryptors.queryableText(tokenPassword, tokenSalt).decrypt(source);
	}

}
