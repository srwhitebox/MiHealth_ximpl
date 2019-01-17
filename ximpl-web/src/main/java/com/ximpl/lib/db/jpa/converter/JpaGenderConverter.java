package com.ximpl.lib.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.ximpl.lib.type.GENDER;

@Converter(autoApply = true)
public class JpaGenderConverter implements AttributeConverter<GENDER, Character>{

	public Character convertToDatabaseColumn(GENDER gender) {
		return gender == null ? null : gender.getChar();
	}

	public GENDER convertToEntityAttribute(Character gender) {
		return gender == null ? null : GENDER.get(gender);
	}

}
