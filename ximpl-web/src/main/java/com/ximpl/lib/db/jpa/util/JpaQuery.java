package com.ximpl.lib.db.jpa.util;

import com.google.common.base.Joiner;
import com.ximpl.lib.util.XcStringUtils;

public class JpaQuery{
	private StringBuilder sbColumns = new StringBuilder();
	private StringBuilder sbConditions = new StringBuilder();
	private String tableName;
	private String orderBy;
	
	public JpaQuery(String tableName){
		setTableName(tableName);
	}

	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	
	public void setOrderBy(String orderBy){
		this.orderBy = orderBy;
	}
	
	public void addColumn(String column){
		if (sbColumns.length() > 0){
			sbColumns.append(", ");
		}
		
		sbColumns.append(column);
	}
	
	public void addColumn(String ... column){
		addColumn(Joiner.on(",").join(column));
	}
	
	public void addJsonColumn(String dbColumn, String columnName){
		String column = "CAST(COLUMN_JSON(" + dbColumn + ") AS CHAR) AS " +columnName;
		addColumn(column);
	}

	public void addJsonColumn(String dbColumn, String dynamicColumn, String type, String columnName){
		String column = "COLUMN_GET(" + dbColumn + ", "+ dynamicColumn +" AS "+ type +") AS " +columnName;
		addColumn(column);
	}
	
	public String toQuery(){
		StringBuilder sbQuery = new StringBuilder("SELECT ");
		if (sbColumns.length() == 0)
			sbQuery.append(" * ");
		else
			sbQuery.append(sbColumns);
		
		sbQuery.append(" FROM ");
		sbQuery.append(this.tableName);
		
		if (sbConditions.length() > 0){
			sbQuery.append(" WHERE ");
			sbQuery.append(this.sbConditions);
		}
	
		if (XcStringUtils.isValid(this.orderBy)){
			sbQuery.append(" ORDER BY ");
			sbQuery.append(this.orderBy);
		}
		
		return sbQuery.toString(); 
	}
	
}
