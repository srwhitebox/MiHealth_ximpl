package com.ximpl.lib.doc;

import java.util.HashMap;

public class ExcelRow extends HashMap<Integer, ExcelCell>{
	
	private static final long serialVersionUID = -2035806587539130093L;
	
	public void addCell(String cellName, String value){
		ExcelCell cell = new ExcelCell(cellName, value);
		this.put(cell.columnIndex, cell);
	}
	
	public int getRowIndex(){
		return this.size() > 0 ? this.get(0).rowIndex : -1;
	}
	
	public String getString(int columnIndex){
		ExcelCell cell = this.get(columnIndex);
		return cell == null ? "" : cell.toString();
	}
	
	public int getInteger(int columnIndex){
		ExcelCell cell = this.get(columnIndex);
		return cell == null ? 0 : cell.toInteger();
	}

	public float getFloat(int columnIndex){
		ExcelCell cell = this.get(columnIndex);
		return cell == null ? 0 : cell.toFloat();
	}	
}
