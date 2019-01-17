package com.ximpl.lib.utility.message;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.ximpl.lib.io.XcFile;
import com.ximpl.lib.io.XcUrl;
import com.ximpl.lib.util.XcEmailUtils;
import com.ximpl.lib.util.XcStringUtils;

public class SmtpMessage {
	private JavaMailSender mailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;
	
	public SmtpMessage(JavaMailSender mailSender){
		this.mailSender = mailSender;
		this.setMessage(mailSender.createMimeMessage());
	}
	
	public MimeMessage getMessage() {
		return message;
	}

	public void setMessage(MimeMessage mimeMessage) {
		this.message = mimeMessage;
		try {
			messageHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void setFrom(String email, String name){
		final String address = XcEmailUtils.formattedMail(email, name);
		if (address != null ){
			try {
				this.messageHelper.setFrom(address);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addTo(String email, String name){
		final String address = XcEmailUtils.formattedMail(email, name);
		if (address != null ){
			try {
				this.messageHelper.addTo(address);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void addCc(String email, String name){
		final String address = XcEmailUtils.formattedMail(email, name);
		if (address != null ){
			try {
				this.messageHelper.addCc(address);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void addBcc(String email, String name){
		final String address = XcEmailUtils.formattedMail(email, name);
		if (address != null ){
			try {
				this.messageHelper.addBcc(address);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void setReplyTo(String email, String name){
		final String address = XcEmailUtils.formattedMail(email, name);
		if (address != null ){
			try {
				this.messageHelper.setReplyTo(address);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	public void setSubject(String subject){
		try {
			this.messageHelper.setSubject(subject);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void setTextContent(String content){
		try {
			this.messageHelper.setText(content);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void setHtmlContent(String content){
		try {
			this.messageHelper.setText(content, true);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void setConent(String textContent, String htmlContent){
		this.setTextContent(textContent);
		this.setHtmlContent(htmlContent);
	}
	
	public void attachInputStream(String fileName, InputStream is){
		if (XcStringUtils.isValid(fileName) && is != null){
			try {
				messageHelper.addAttachment(fileName, new InputStreamResource(is));
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void attachHostFile(String fileName, String url){
		try {
			attachHostFile(fileName, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void attachHostFile(String fileName, URL url){
		if (XcStringUtils.isValid(fileName) && url != null){
			InputStreamResource isr = XcUrl.toInputStreamResource(url);
			if (isr != null)
				try {
					messageHelper.addAttachment(fileName, isr);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void attachFile(String fileName, String filePath){
		XcFile file = new XcFile(filePath);
		if (file.exists()){
			if (XcStringUtils.isNullOrEmpty(fileName))
				fileName = file.getName();
	        try {
				messageHelper.addAttachment(fileName, file);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void inlineInputStream(String fileName, InputStream is){
		if (XcStringUtils.isValid(fileName) && is != null){
			try {
				final String contentType = new Tika().detect(fileName);
				messageHelper.addInline(fileName, new InputStreamResource(is), contentType);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void inlineHostImage(String fileName, String url){
		try {
			attachHostFile(fileName, new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void inlineHostImage(String fileName, URL url){
		if (XcStringUtils.isValid(fileName) && url != null){
			InputStreamResource isr = XcUrl.toInputStreamResource(url);
			if (isr != null)
				try {
					messageHelper.addInline(fileName, isr);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
		}
	}
	
	public void addImage(String fileName, String filePath){
		XcFile file = new XcFile(filePath);
		if (file.exists()){
			if (XcStringUtils.isNullOrEmpty(fileName))
				fileName = file.getName();
			try {
				this.messageHelper.addInline(fileName, file);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void send(){
		this.mailSender.send(message);
	}
}
