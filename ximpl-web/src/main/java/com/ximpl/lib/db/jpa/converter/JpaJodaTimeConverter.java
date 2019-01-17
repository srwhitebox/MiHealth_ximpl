package com.ximpl.lib.db.jpa.converter;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;

@Converter(autoApply = true)
public class JpaJodaTimeConverter implements AttributeConverter<DateTime, Date>{

	public Date convertToDatabaseColumn(DateTime dateTime) {
		return dateTime == null ? null : dateTime.toDate();
	}

	public DateTime convertToEntityAttribute(Date date) {
		return date == null ? null : new DateTime(date);
	}

}
