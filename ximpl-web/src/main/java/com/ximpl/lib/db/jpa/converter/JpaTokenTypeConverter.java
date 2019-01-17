package com.ximpl.lib.db.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.ximpl.lib.type.TOKEN_TYPE;

@Converter(autoApply = true)
public class JpaTokenTypeConverter implements AttributeConverter<TOKEN_TYPE, Character>{

	public Character convertToDatabaseColumn(TOKEN_TYPE tokenType) {
		return tokenType == null ? null : tokenType.value();
	}

	public TOKEN_TYPE convertToEntityAttribute(Character tokenType) {
		return tokenType == null ? null : TOKEN_TYPE.get(tokenType);
	}

}
