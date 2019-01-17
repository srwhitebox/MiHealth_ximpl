package com.ximpl.lib.internet.mail;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

import org.apache.commons.codec.CharEncoding;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.ximpl.lib.io.XcFile;

/**
 * Mail sender
 */
public class XcMailSender extends JavaMailSenderImpl{
	private MimeMessageHelper mailMessage;
	
	/**
	 * Constructor
	 */
	public XcMailSender(){
		super();
		this.setDefaultEncoding(CharEncoding.UTF_8);
		try {
			this.mailMessage = new MimeMessageHelper(this.createMimeMessage(), true, "UTF-8");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor
	 * @param host
	 * @param port
	 * @param protocol
	 * @param userName
	 * @param password
	 */
	public XcMailSender(String host, int port, String protocol, String userName, String password){
		this();
		this.setHost(host);
		this.setPort(port);
		this.setProtocol(protocol);
		this.setUsername(userName);
		this.setPassword(password);
	}
	
	/**
	 * Set sender
	 * @param email
	 * @return
	 */
	public boolean setSender(String email){
		try {
			this.mailMessage.setFrom(email);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Set sender
	 * @param email
	 * @param name
	 * @return
	 */
	public boolean setSender(String email, String name){
		try {
			this.mailMessage.setFrom(email, name);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Add recipient for To
	 * @param email
	 * @return
	 */
	public boolean addTo(String email){
		try {
			this.mailMessage.addTo(email);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Add recipient for To
	 * @param email
	 * @param name
	 * @return
	 */
	public boolean addTo(String email, String name){
		try {
			this.mailMessage.addTo(email, name);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Add recipient for CC
	 * @param email
	 * @return
	 */
	public boolean addCc(String email){
		try {
			this.mailMessage.addCc(email);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add recipient for CC
	 * @param email
	 * @param name
	 * @return
	 */
	public boolean addCc(String email, String name){
		try {
			this.mailMessage.addCc(email, name);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Add recipient for BCC
	 * @param email
	 * @return
	 */
	public boolean addBcc(String email){
		try {
			this.mailMessage.addBcc(email);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add recipient for BCC
	 * @param email
	 * @param name
	 * @return
	 */
	public boolean addBcc(String email, String name){
		try {
			this.mailMessage.addBcc(email, name);
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Set subject
	 * @param subject
	 * @return
	 */
	public boolean setSubject(String subject){
		try {
			this.mailMessage.setSubject(subject);
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Set content
	 * @param content
	 * @return
	 */
	public boolean setText(String content){
		return this.setContent(content, false);
	}

	public boolean setHtml(String content){
		return this.setContent(content, true);
	}

	/**
	 * Set content
	 * @param content
	 * @param isHtml
	 * @return
	 */
	public boolean setContent(String content, boolean isHtml){
		try {
			this.mailMessage.setText(content, isHtml);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add attachment
	 * @param filePath
	 * @return
	 */
	public boolean addAttachment(final String filePath){
		final XcFile file = new XcFile(filePath);
		if (file.exists())
			return addAttachment(file.getName(), file);
		return false;
	}
	
	/**
	 * Add attachments
	 * @param filePaths
	 * @return
	 */
	public boolean addAttachment(final String ...filePaths){
		if (filePaths == null || filePaths.length == 0)
			return false;
		
		for(String filePath : filePaths){
			if (!addAttachment(filePath))
				return false;
		}
		return true;
	}
	
	/**
	 * Add attachment
	 * @param fileName
	 * @param file
	 * @return
	 */
	public boolean addAttachment(final String fileName, final File file){
		try {
			mailMessage.addAttachment(fileName, file);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add attachment
	 * @param fileName
	 * @param is
	 * @return
	 */
	public boolean addAttachment(final String fileName, InputStream is){
		try {
			mailMessage.addAttachment(fileName, new InputStreamResource(is));
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add attachment
	 * @param fileName
	 * @param bytes
	 * @param contentType
	 * @return
	 */
	public boolean addAttachment(final String fileName, final byte[] bytes, final String contentType){
		try {
			mailMessage.addAttachment(fileName, new ByteArrayResource(bytes), contentType);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add inline image
	 * @param filePath
	 * @return
	 */
	public boolean addImage(final String filePath){
		final XcFile file = new XcFile(filePath);
		if (file.exists())
			return addImage(file.getName(), file);
		return false;
	}
	
	/**
	 * Add inline image
	 * @param filePaths
	 * @return
	 */
	public boolean addImage(final String ...filePaths){
		if (filePaths == null || filePaths.length == 0)
			return false;
		
		for(String filePath : filePaths){
			if (!addImage(filePath))
				return false;
		}
		return true;
	}

	/**
	 * Add inline image
	 * @param fileName
	 * @param file
	 * @return
	 */
	public boolean addImage(final String fileName, final File file){
		try {
			mailMessage.addInline(fileName, file);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Add inline image
	 * @param fileName
	 * @param is
	 * @return
	 */
	public boolean addImage(final String fileName, InputStream is){
		try {
			mailMessage.addInline(fileName, new InputStreamResource(is));
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Add inline image
	 * @param fileName
	 * @param bytes
	 * @param contentType
	 * @return
	 */
	public boolean addImage(final String fileName, final byte[] bytes, final String contentType){
		try {
			mailMessage.addInline(fileName, new ByteArrayResource(bytes), contentType);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Send mail message
	 */
	public void send(){
		this.send(mailMessage.getMimeMessage());
	}	
}
