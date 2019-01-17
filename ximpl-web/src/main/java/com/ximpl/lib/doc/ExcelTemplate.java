package com.ximpl.lib.doc;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import com.google.gson.internal.LazilyParsedNumber;
import com.ximpl.lib.io.XcFile;
import com.ximpl.lib.util.XcDateTimeUtils;
import com.ximpl.lib.util.XcStringUtils;

public class ExcelTemplate {
	class TemplateCell {
		public String key;
		public String format;
		public XSSFCell cell;
		
		public TemplateCell(XSSFCell cell){
			this.cell = cell;
			patchKey(cell);
		}
		
		private void patchKey(XSSFCell cell){
			final Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
			final String keyText = cell.getStringCellValue();
			final Matcher matcher = pattern.matcher(keyText);
			if (matcher.matches()){
				final String group = matcher.group(1);
				final int index = group.indexOf('|');
				this.key = index < 0 ? group : group.substring(0,  index);
				this.format = index < 0 ? null : group.substring(index+1);
			}
		}
	}
	private XSSFWorkbook workBook;
	private XSSFSheet templateSheet;
	private CreationHelper createHelper;
	private XSSFRow templateRow;
	private int templateRowIndex = -1;
	private ArrayList<TemplateCell> templateCells = new ArrayList<TemplateCell>();
	
	public ExcelTemplate(){
		
	}
	
	public void open(String path){
		try {
			XcFile file = new XcFile(path);
			InputStream is = new ByteArrayInputStream(file.toBytes());
			workBook = new XSSFWorkbook(is);
			createHelper = workBook.getCreationHelper();
			
			Iterator<Sheet> sheets = workBook.sheetIterator();
			templateRow = null;
			while (sheets.hasNext() && this.templateRow == null){
				XSSFSheet sheet = (XSSFSheet)sheets.next();
				Iterator<Row> rows = sheet.rowIterator();
				while(rows.hasNext()  && this.templateRow == null){
					XSSFRow row = (XSSFRow) rows.next();
					Iterator<Cell> cells = row.cellIterator();
					while(cells.hasNext() && this.templateRow == null){
						XSSFCell cell = (XSSFCell)cells.next();
						final String cellValue = cell.getStringCellValue();
						if (cellValue.startsWith("{{")){
							patchTemplateRow(row);
							this.templateSheet = sheet;
							this.templateRow = row;
							this.templateRowIndex = cell.getRowIndex();
							break;
						}
					}
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void patchTemplateRow(XSSFRow row){
		Iterator<Cell> cells = row.cellIterator();
		while(cells.hasNext()){
			templateCells.add(new TemplateCell((XSSFCell)cells.next()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addRow(Object object){
		ObjectMapper m = new ObjectMapper();
		Map<String, Object> model = m.convertValue(object, Map.class);
		addRow(model);
	}
	
	public void addRow(Map<String, Object> model){
		XSSFRow row = templateSheet.createRow(templateRowIndex);
		int index = 0;
		try{
			for(TemplateCell cell : templateCells){
				if (cell != null){
					XSSFCell destCell = row.createCell(index++);
	//				destCell.setCellStyle(cell.cell.getCellStyle());
	//				destCell.setCellType(cell.cell.getCellType());
					
					Object value = model.get(cell.key);
					if (value instanceof Date){
						CellStyle cellStyle = workBook.createCellStyle();
					    cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(cell.format != null ? cell.format : "yyyy-mm-dd h:mm"));
					    destCell.setCellStyle(cellStyle);
//					    Date localDate = XcDateTimeUtils.toLocalTime((Date) value, DateTimeZone.getDefault()).toDate();
					    Date localDate = (Date) value;
						destCell.setCellValue(localDate);
					}else if(value instanceof LazilyParsedNumber){
						final LazilyParsedNumber number = (LazilyParsedNumber) value;
						destCell.setCellValue(number.doubleValue());
					}else if(value instanceof Number){
						final Number number = (Number)value;
						destCell.setCellValue(number.doubleValue());
					}else if(value instanceof Boolean){
						destCell.setCellValue((Boolean)value);
					}else{
						destCell.setCellValue((String)value);
					}
				}
			}
			templateRowIndex++;
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Write to HTTP Servlet Response
	 * @param response
	 * @param fileName
	 */
	public void write(HttpServletResponse response, String fileName){
		if (XcStringUtils.isNullOrEmpty(fileName)){
			return;
		}
		try {
			final URI uri = new URI(null, null, fileName, null);
			final String encodedFileName = uri.toASCIIString();
			response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);
			write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void write(String path){
		try {
			write(new FileOutputStream(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void write(OutputStream os){
		try {
			workBook.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		try {			
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
