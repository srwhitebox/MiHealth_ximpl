package com.ximpl.lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tika.Tika;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Resources;

public class XcUrl {
	public static byte[] toBytes(String url){
		try {
			return toBytes(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] toBytes(URL url){
		try {
			return Resources.toByteArray(url);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String toBase64Encoded(String url){
		byte[] bytes = toBytes(url);
		return bytes == null ? null : BaseEncoding.base64().encode(bytes);
	}
	
	public static String toBase64Encoded(URL url){
		byte[] bytes = toBytes(url);
		return bytes == null ? null : BaseEncoding.base64().encode(bytes);
	}
	
	public static InputStreamSource toInputStreamSource(String url){
		byte[] bytes = toBytes(url); 
		return bytes == null ? null : new ByteArrayResource(bytes);
	}

	public static InputStream toInputStream(URL url){
		try {
			return url.openStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static InputStreamResource toInputStreamResource(URL url){
		if (url != null){
			InputStream is = toInputStream(url);
			if (is != null)
				return new InputStreamResource(is);
		}
		return null;
	}
	
	public static String getContentType(String url){
		return new Tika().detect(url);
	}
	
	public static String getContentType(URL url){
		try {
			return new Tika().detect(url);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
