package com.ximpl.lib.utility.message;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.ximpl.lib.constant.MessageConst;
import com.ximpl.lib.io.XcFile;
import com.ximpl.lib.io.XcInputStream;
import com.ximpl.lib.io.XcUrl;
import com.ximpl.lib.util.XcEmailUtils;
import com.ximpl.lib.util.XcStringUtils;

public class SendInMailMessage{
	private SendIn mailSender;
	private Map<String, Object> message;
	
	/**
	 * Constructor
	 */
	public SendInMailMessage(SendIn mailSender){
		super();
	}
	
	public SendIn getMailSender(){
		return this.mailSender;
	}
	
	public void setMailSender(SendIn mailSender){
		this.mailSender = mailSender;
	}
	
	public Map<String, Object> getMessage(){
		return this.message;
	}
	
	public void setMessage(Map<String, Object> message){
		this.message = message;
	}

	/**
	 * Set sender
	 * @param email
	 * @param name
	 */
	public void setFrom(String email, String name){
		this.setMail(MessageConst.FROM, email, name);
	}

	/**
	 * Set reply to
	 * @param email
	 * @param name
	 */
	public void setReplyTo(String email, String name){
		this.setMail(MessageConst.REPLY_TO, email, name);
	}
	
	/**
	 * Set To Mails
	 * @param mails
	 */
	public void setTo(String ... mails){
		this.setMails(MessageConst.TO, mails);
	}
	
	/**
	 * Set CC Mails
	 * @param mails
	 */
	public void setCc(String ... mails){
		this.setMails(MessageConst.CC, mails);
	}

	/**
	 * Set BCC Mails
	 * @param mails
	 */
	public void setBcc(String ... mails){
		this.setMails(MessageConst.BCC, mails);
	}

	/**
	 * Set mails
	 * @param mails
	 */
	public void setMails(String field, String ... mails){
		String formattedMails = Joiner.on('|').join(mails);
		this.message.put(MessageConst.TO, formattedMails);
	}
	
	/**
	 * Add receiver
	 * @param email
	 * @param name
	 */
	public void addTo(String email, String name){
		this.addMailTo(MessageConst.TO, email, name);
	}
	
	/**
	 * Add CC
	 * @param email
	 * @param name
	 */
	public void addCc(String email, String name){
		this.addMailTo(MessageConst.CC, email, name);
	}
	
	/**
	 * Add BCC
	 * @param email
	 * @param name
	 */
	public void addBcc(String email, String name){
		this.addMailTo(MessageConst.BCC, email, name);
	}

	/**
	 * Get receiver map
	 * @return
	 */
	public Map<String, String> getTo(){
		return this.getMapField(MessageConst.TO);
	}
	
	/**
	 * Get CC map
	 * @return
	 */
	public Map<String, String> getCc(){
		return this.getMapField(MessageConst.CC);
	}
	
	/**
	 * Get BCC map
	 * @return
	 */
	public Map<String, String> getBcc(){
		return this.getMapField(MessageConst.BCC);
	}

	/**
	 * Set Subject
	 * @param subject
	 */
	public void setSubject(String subject){
		this.message.put(MessageConst.SUBJECT, subject);
	}
	
	/**
	 * Set template ID
	 * @param id
	 */
	public void setTemplateId(String id){
		this.message.put(MessageConst.ID, id);
	}

	/**
	 * Set text message
	 * @param message
	 */
	public void setTextContent(String message){
		this.message.put(MessageConst.TEXT, message);
	}
	
	/**
	 * Set HTML content
	 * @param content
	 */
	public void setHtmlContent(String content){
		this.message.put(MessageConst.HTML, content);
	}
	
	/**
	 * Set plain text & HTML content
	 * @param textContent
	 * @param htmlContent
	 */
	public void setConent(String textContent, String htmlContent){
		this.setTextContent(textContent);
		this.setHtmlContent(htmlContent);
	}
	
	/**
	 * Add attribute
	 * @param key
	 * @param value
	 */
	public void addAttr(String key, String value){
		this.addEntryToMapField(MessageConst.ATTR, key, value);
	}
	
	/**
	 * Attach a host file
	 * Available extension is gif, png, bmp, cgm, jpg, jpeg, txt, css, shtml, html, htm, csv, zip, pdf, xml, doc, ics, xls, ppt, tar, and ez.
	 * @param url
	 */
	public void attachUrl(String url){
		this.message.put(MessageConst.ATTACHMENT_URL, url);
	}
	
	/**
	 * Attach a file
	 * @param fileName
	 * @param filePath
	 */
	public void attachFile(String fileName, String filePath){
		XcFile file = new XcFile(filePath);
		if (file.exists()){
			attach(fileName, file.toBase64());
		}
	}
	
	/**
	 * Attach File from InputStream
	 * @param fileName
	 * @param is
	 */
	public void attachInputStream(String fileName, InputStream is){
		if (XcStringUtils.isValid(fileName) && is!=null){
			String base64Encoded = XcInputStream.toBase64Encoded(is);
			if (XcStringUtils.isValid(base64Encoded))
				attach(fileName, base64Encoded);
		}
	}
	
	/**
	 * Attach File from Host URL
	 * @param fileName
	 * @param url
	 */
	public void attachHostFile(String fileName, String url){
		if (XcStringUtils.isValid(fileName) && XcStringUtils.isValid(url)){
			String base64Encoded = XcUrl.toBase64Encoded(url);
			if (XcStringUtils.isValid(base64Encoded))
				attach(fileName, base64Encoded);
		}
	}

	/**
	 * Attach
	 * @param fileName
	 * @param base64Encoded
	 */
	public void attach(String fileName, String base64Encoded){
		this.addEntryToMapField(MessageConst.ATTACHMENT, fileName, base64Encoded);
	}
	
	/**
	 * Add Inline Image
	 * @param fileName
	 * @param filePath
	 */
	public void addImage(String fileName, String filePath){
		XcFile file = new XcFile(filePath);
		if (file.exists()){
			this.addInlineImage(fileName, file.toBase64());
		}
	}
	
	/**
	 * Add Inline image
	 * @param fileName
	 * @param base64Encoded
	 */
	public void addInlineImage(String fileName, String base64Encoded){
		this.addEntryToMapField(MessageConst.INLINE_IMAGE, fileName, base64Encoded);
	}
	
	/**
	 * Send this email using sender
	 * @param sender
	 * @return
	 */
	public String send(){
		return mailSender.send_email(this);
	}

	/**
	 * set Mail filed as like To, ReplyTo
	 * @param field
	 * @param email
	 * @param name
	 */
	private void setMail(String field, String email, String name){
		if (XcEmailUtils.isValid(email)){
			if (XcStringUtils.isNullOrEmpty(name)){
				name = email.substring(0, email.indexOf('@'));
			}
			this.message.put(field, new String[]{email, name});
		}
	}

	/**
	 * Add mail to map
	 * @param field
	 * @param email
	 * @param name
	 */
	private void addMailTo(String field, String email, String name){
		if (XcEmailUtils.isValid(email)){
			email = email.toLowerCase();
			if (XcStringUtils.isNullOrEmpty(name)){
				name = email.substring(0, email.indexOf('@'));
			}
			Map<String, String> map = getMapField(field);
			map.put(email, name);
		}
	}

	/**
	 * Add entry name and value into map
	 * @param field
	 * @param entryName
	 * @param value
	 */
	private void addEntryToMapField(String field, String entryName, String value){
		this.getMapField(field).put(entryName, value);
	}
	
	/**
	 * get map
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> getMapField(String field){
		Map<String, String> map = (Map<String, String>)this.message.get(field);
		
		if (map == null){
			map = new HashMap<String, String>();
			this.message.put(field, map);
		}
		
		return map;
	}
}
