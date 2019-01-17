package com.ximpl.lib.util;


import java.nio.ByteBuffer;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.google.common.io.BaseEncoding;

public class XcUuidUtils {
	private static final String tokenPassword = "MiHEALTH@MiTAC.com";
	private static final String tokenSalt = "171891893b5cef2f"; //KeyGenerators.string().generateKey();
	private static final TextEncryptor encryptor = Encryptors.queryableText(tokenPassword, tokenSalt);	
	
	public static byte[] toBytes(UUID uuid) {
	    long hi = uuid.getMostSignificantBits();
	    long lo = uuid.getLeastSignificantBits();
	    return ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
	}
	
	public static String toHexString(UUID uuid){
		return uuid.toString().replace("-", "");
	}
	
	public static byte[] toOrderedBytes(UUID uuid){
		String hexValue = uuid.toString().replace("-", "");
		return DatatypeConverter.parseHexBinary(hexValue);
	}
	
	public static UUID fromString(String hexValue){
		byte[] byteUuid = DatatypeConverter.parseHexBinary(hexValue.replace("-", ""));
		String formattedValue = toStringFromOrderedBytes(byteUuid);
		return UUID.fromString(formattedValue);
	}
	
	public static UUID fromOrderedBytes(byte[] idValue){
		return UUID.fromString(toStringFromOrderedBytes(idValue));
	}
	
	public static String toStringFromOrderedBytes(byte[] idValue){
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < 16; i++){
			byte byteValue = i < idValue.length ?  idValue[i] : 0;
			sb.append(String.format("%02x", byteValue));
			if (i == 3 || i == 5 || i== 7 || i == 9){
				sb.append('-');
			}
		}
		return sb.toString();
	}
	
	/**
	 * Generate Base 64 URL safe string
	 * @param uuid
	 * @return
	 */
	public static String toBase64String(UUID uuid) {
		return BaseEncoding.base64Url().encode(toOrderedBytes(uuid));
	}
	
	public static UUID toUuidFromBase64(String token){
		return UUID.fromString(toStringFromOrderedBytes(BaseEncoding.base64Url().decode(token)));
	}
	
	public static String encrypt(UUID uuid){
		return encryptor.encrypt(toHexString(uuid));
	}
	
	public static UUID decrypt(String encryptedUuid){
		return fromString(encryptor.decrypt(encryptedUuid));
	}
	
}
