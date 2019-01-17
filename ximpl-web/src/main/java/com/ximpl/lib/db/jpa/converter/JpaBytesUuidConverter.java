package com.ximpl.lib.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.ximpl.lib.util.XcStringUtils;

@Converter(autoApply = true)
public class JpaBytesUuidConverter implements AttributeConverter<String, byte[]>{
	private static final String tokenPassword = "MiHEALTH@MiTAC.com";
	private static final String tokenSalt = "171891893b5cef2f"; //KeyGenerators.string().generateKey();
	private static final TextEncryptor encryptor = Encryptors.queryableText(tokenPassword, tokenSalt);	

	public byte[] convertToDatabaseColumn(String encryptedUuid) {
		String hexText = encryptor.decrypt(encryptedUuid);
		return XcStringUtils.toBytes(hexText);
	}

	public String convertToEntityAttribute(byte[] uuidBytes) {
		final String hexText = XcStringUtils.toHexString(uuidBytes);
		
		return uuidBytes == null ? null : encryptor.encrypt(hexText);
	}

}
