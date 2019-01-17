package com.ximpl.lib.utility.message;

import java.util.HashMap;
import java.util.Map;

import com.ximpl.lib.constant.MessageConst;
import com.ximpl.lib.util.XcStringUtils;

public class SmsMessage{
	private SendIn messageSender;
	private Map<String, String> message;
	
	/**
	 * Constructor
	 */
	public SmsMessage(SendIn messageSender){
		setMessageSender(messageSender);
		message = new HashMap<String, String>();
	}

	/**
	 * Constructor
	 * @param messageSender
	 * @param from
	 */
	public SmsMessage(SendIn messageSender, String from){
		this(messageSender);
		this.setFrom(from);
	}

	/**
	 * Get Message Sender(SendIn)
	 * @return
	 */
	public SendIn getMessageSender(){
		return this.messageSender;
	}
	
	/**
	 * Set Message Sender(SendIn)
	 * @param messageSender
	 */
	public void setMessageSender(SendIn messageSender){
		this.messageSender = messageSender;
	}
	
	/**
	 * Get message map object
	 * @return
	 */
	public Map<String, String> getMessage(){
		return this.message;
	}
	
	/**
	 * Set message map object
	 * @param message
	 */
	public void setMessage(Map<String, String> message){
		this.message = message;
	}

	/**
	 * Set sender name
	 * @param from
	 */
	public void setFrom(String from){
		if (XcStringUtils.isValid(from))
			this.message.put(MessageConst.FROM, from);
	}
	
	/**
	 * Set Receiver
	 * @param phoneNumber
	 */
	public void setTo(String phoneNumber){
		this.message.put(MessageConst.TO, phoneNumber);
	}

	/**
	 * Set message text
	 * @param text
	 */
	public void setText(String text){
		this.message.put(MessageConst.TEXT, text);
	}
	
	/**
	 * Set Callback URL
	 * @param webUrl
	 */
	public void setCallbackUrl(String webUrl){
		if (XcStringUtils.isValid(webUrl))
			this.message.put(MessageConst.WEB_URL, webUrl);
	}

	/**
	 * Set tag
	 * @param tag
	 */
	public void setTag(String tag){
		if (XcStringUtils.isValid(tag))
			this.message.put(MessageConst.TAG, tag);
	}

	/**
	 * Set type
	 * @param messageType
	 */
	public void setType(MESSAGE_TYPE messageType){
		if (messageType == null || messageType == MESSAGE_TYPE.unknown)
			messageType = MESSAGE_TYPE.transactional;
		this.message.put(MessageConst.TYPE, messageType.name());
	}
	
	/**
	 * Send message
	 * @return
	 */
	public String send(){
		return this.messageSender == null ? null : this.messageSender.send_sms(getMessage());
	}
}
