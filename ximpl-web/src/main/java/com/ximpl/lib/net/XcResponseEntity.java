package com.ximpl.lib.net;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public class XcResponseEntity {
	public static ResponseEntity<?> get(HttpServletRequest request, MediaType mediaType, byte[] bytes){
		BodyBuilder builder = ResponseEntity.ok();
		builder.contentLength(bytes.length);
		builder.contentType(mediaType);

		return builder.body(bytes);
	}

	public static ResponseEntity<?> get(MediaType mediaType, InputStream is, int contentLength){
		BodyBuilder builder = ResponseEntity.ok();
		builder.contentLength(contentLength);
		builder.contentType(mediaType);

		return builder.body(new InputStreamResource(is));
	}
}
