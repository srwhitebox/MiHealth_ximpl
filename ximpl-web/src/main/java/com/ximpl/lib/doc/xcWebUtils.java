package com.ximpl.lib.doc;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.CharEncoding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class xcWebUtils {

	public static Document loadHtml(String urlRef){
		return load(urlRef, Parser.htmlParser());
	}

	public static Document loadXml(String urlRef){
		return load(urlRef, Parser.xmlParser());
	}

	public static Document load(String urlRef, Parser parser){
		try {
			URL url = new URL(urlRef);
	        URLConnection connection = url.openConnection();
	        InputStream is = connection.getInputStream();

	        return Jsoup.parse(is, CharEncoding.UTF_8, "", parser);
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}  
		
		return null;
	}
	
}
