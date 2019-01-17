package com.ximpl.lib.type;

import com.ximpl.lib.type.COMMAND;

public enum PARAMETER {
	useruid,
	name,
	email,
	mail,
	id,
	campusid,
	campus_id,
	idtype,
	loginid,
	userid,
	rfid,
	nfc,
	nid,
	nationalid,
	national_id,
	pid,
	personalid,
	personal_id,
	password,
	birth,     
	birthdate,
	birth_date,
	birthday,
	birth_day, 
	gender,
	sex,
	blood,
	bloodtype,
	blood_type,
	schoolregister,
	school_register,
	register,
	properties,
	settings,
	roles,
	
	campusuid,
	campus_uid,
	schoolyear,
	school_year,
	grade,
	semester,
	
	parentuid,
	parent_uid,
	
	boardid,
	board_id,
	
	boarduid,
	board_uid,
	
	categoryid,
	category_id,
	
	categoryuid,
	category_uid,

	displayorder,
	display_order,

	writeruid,
	writer_uid,
	writer,
	
	title,
	content,
	
	enable,
	enabled,
	activatedat,
	activated_at,
	registeredat,
	registered_at,
	unknown
	;
	
	public static PARAMETER get(String paramName){
		try{
			return PARAMETER.valueOf(paramName.toLowerCase());
		}catch(Exception e){
			return unknown;
		}
	}
}
