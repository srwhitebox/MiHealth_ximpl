package com.ximpl.lib.doc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.CharMatcher;
import com.ximpl.lib.util.XcDateTimeUtils;
import com.ximpl.lib.util.XcNumberUtils;

public class ExcelCell {
	public int columnIndex;
	public int rowIndex;
	public String cellName;
	public String value;
	
	public ExcelCell(String cellName, String value){
		this.cellName = cellName;
		this.value = value;
		
		this.columnIndex = getColumnIndex(cellName);
		this.rowIndex = getRowIndex(cellName);
	}
	
	// Return column name
	public String getColumnName(){
		return CharMatcher.DIGIT.removeFrom(this.cellName);
	}
	
	public int getColumnIndex(){
		return this.columnIndex;
	}
	
	// Return row number
	private int getRowIndex(String cellName){
		return Integer.parseInt(CharMatcher.DIGIT.retainFrom(cellName))-1;
	}

	private int getColumnIndex(String cellName){
		String columnName = CharMatcher.DIGIT.removeFrom(cellName).toUpperCase();
		String reversedName =  new StringBuffer(columnName).reverse().toString();
		int index = 0;
		for(int i=0; i < reversedName.length(); i++){
			int n = reversedName.charAt(i) - 'A';
			index +=  i==0 ? n : 0 + i* 26 * (n+1);
		}
		return index;
	}
	
	
	public String toString(){
		return value;
	}
	
	public int toInteger(){
		return XcNumberUtils.toInt(value);
	}
	
	public float toFloat(){
		return XcNumberUtils.toFloat(value);
	}
	
	public Date toDate() {
		String dateText = this.value;
		Date date = null;
		String[] formats = {"yyyyMMdd", "yyyy-M-d", "d.M.yyyy", "d/M/yyyy"};
		
		for(String format : formats){
			try {
				SimpleDateFormat df = new SimpleDateFormat( format );
				date = df.parse(dateText);
				break;
			} catch (ParseException e) {
			}
		}

		return date;
	}
	
	public Date toTwDate() {
		return XcDateTimeUtils.parseTwDate(value).toDate();
	}
}
