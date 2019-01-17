package com.ximpl.lib.io;

import java.io.File;
import java.io.IOException;

import org.apache.tika.Tika;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;

@SuppressWarnings("serial")
public class XcFile extends File{
	/**
	 * Constructor
	 * @param filePath
	 */
	public XcFile(String filePath) {
		super(filePath);
	}
	
	/**
	 * Convert to Base64 String
	 * @return
	 */
	public String toBase64(){
		byte[] bytes = toBytes();
		return bytes == null ? null : BaseEncoding.base64().encode(bytes);
	}
	
	/**
	 * Convert to byte array
	 * @return
	 */
	public byte[] toBytes(){
		try {
			return Files.toByteArray(this);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getContentType(){
		return new Tika().detect(this.getName());
	}
}
