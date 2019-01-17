package com.ximpl.lib.io;

import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;

public class XcInputStream{
	public static byte[] toBytes(InputStream is){
		try {
			return ByteStreams.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String toBase64Encoded(InputStream is){
		byte[] bytes = toBytes(is);
		return bytes == null ? null : BaseEncoding.base64().encode(bytes);
	}
}
