package com.ximpl.lib.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

import com.google.common.base.Joiner;

public class XcResponse {
	public static ResponseEntity<?> getResource(HttpServletRequest request, MediaType mediaType, String path){
		String subFolderName = mediaType.getType().equals("image") ? "image" : mediaType.getSubtype();
		
		String resourcePath = Joiner.on("/").join("/resources/default",subFolderName, path);
		
		return getResourceFromUrl(request, mediaType, resourcePath);
	}
	
	public static ResponseEntity<?> getResourceFromUrl(HttpServletRequest request, MediaType mediaType, String resourcePath){
		try {
			URL resourceURL = request.getSession().getServletContext().getResource(resourcePath);
			if (resourceURL != null){
				URLConnection urlConnection = resourceURL.openConnection();
				int contentLength = urlConnection.getContentLength();
				if (contentLength == 0)
					return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
				BodyBuilder builder = ResponseEntity.ok();
				builder.contentLength(contentLength);
				builder.contentType(mediaType);
	
				return builder.body(new InputStreamResource(urlConnection.getInputStream()));
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		
	}
	
	public static ResponseEntity<?> getFile(HttpServletRequest request, MediaType mediaType, String path){
		try {
			File file = new File(path);
			if (!file.exists() || !file.isFile())
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			
			BodyBuilder builder = ResponseEntity.ok();
			builder.contentLength(file.length());
			builder.contentType(mediaType);
			builder.header("Content-Disposition", "filename="+"\""+ file.getName()+"\"");

			return builder.body(new InputStreamResource(new FileInputStream(file)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

}
