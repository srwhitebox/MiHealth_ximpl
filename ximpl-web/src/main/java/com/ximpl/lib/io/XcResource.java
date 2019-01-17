package com.ximpl.lib.io;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.springframework.http.MediaType;

import com.google.common.io.ByteStreams;
import com.google.common.base.Joiner;
import com.google.common.io.Files;

public class XcResource {
	/**
	 * Copy resource file to HTTP Response
	 * @param servletContext
	 * @param response
	 * @param resourcePath
	 */
	public static void copyResourceToResponse(ServletContext servletContext, HttpServletResponse response, String resourcePath){
		response.setContentType(new Tika().detect(resourcePath));
		final InputStream is = servletContext.getResourceAsStream(resourcePath);
		copyToResponse(servletContext, response, is);
	}
	
	public static void copyFileToResponse(ServletContext servletContext, HttpServletResponse response, String filePath){
		response.setContentType(new Tika().detect(filePath));
		try {
			InputStream is = new FileInputStream(filePath);
			copyToResponse(servletContext, response, is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void copyToResponse(ServletContext servletContext, HttpServletResponse response, InputStream is){
		try {
			if (is != null)
				ByteStreams.copy(is,response.getOutputStream());
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Copy Buffered Image to HTTP response
	 * @param response
	 * @param image
	 */
	public static void copyToResponse(HttpServletResponse response, BufferedImage image){
		try {
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
			ImageIO.write(image, "png", response.getOutputStream());
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
