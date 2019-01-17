package com.ximpl.lib.doc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public abstract class ExcelReader {
	private XMLReader xmlReader;
	private XSSFReader xssfReader;
	
	public ExcelReader(String path){
		try {
			OPCPackage pkg = OPCPackage.open(path);
			setPackage(pkg);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	public ExcelReader(InputStream is){
		try {
			OPCPackage pkg = OPCPackage.open(is);
			setPackage(pkg);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setPackage(OPCPackage pkg){
		try {
			xssfReader = new XSSFReader( pkg );
			SharedStringsTable stringsTable = xssfReader.getSharedStringsTable();

			xmlReader = fetchSheetParser(stringsTable);		
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		}
	}
	
	public void parceSheet(String sheetName){
		InputStream sheetIS;
		try {
			sheetIS = xssfReader.getSheet(sheetName);
			InputSource sheetSource = new InputSource(sheetIS);
			xmlReader.parse(sheetSource);
			sheetIS.close();		
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public void parseAll(){
		try {
			Iterator<InputStream> sheets = xssfReader.getSheetsData();
			while(sheets.hasNext()) {
				InputStream is = sheets.next();
				InputSource sheetSource = new InputSource(is);
				xmlReader.parse(sheetSource);
				is.close();
			}		
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	public XMLReader fetchSheetParser(SharedStringsTable stringsTable){
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
			ContentHandler handler = (ContentHandler) new SheetHandler(stringsTable);
			xmlReader.setContentHandler(handler);
			return xmlReader;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract void onSheetDataStart();
	public abstract boolean onNewRow(ExcelRow row);
	public abstract void onSheetDataEnd();
	
	private class SheetHandler extends DefaultHandler {
		private static final String CELL_NUM = "r";
		private static final String CELL_TYPE = "t";

		private SharedStringsTable stringsTable;
		
		private ExcelRow row = null;

		private String curCellName = null;
		private String curCellType = null;
		private String curCellValue;
		
		private SheetHandler(SharedStringsTable stringsTable) {
			this.stringsTable = stringsTable;
		}
		
		public void startElement(String uri, String localName, String name, Attributes attributes){
			// c => cell
			if (name.equals("row")){ //new row started
				row = new ExcelRow();
				
			}else if(name.equals("c")) {
				// Clear contents cache
				curCellValue = "";

				//Cell Num : AA##
				curCellName = attributes.getValue(CELL_NUM);

				// Figure out if the value is an index in the Strings Table
				curCellType = attributes.getValue(CELL_TYPE);
				
			}else if(name.equals("sheetData")){
				onSheetDataStart();
			}
		}
		
		public void endElement(String uri, String localName, String name){
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (name.equals("row")){ //Row ended
				onNewRow(row);
			}else if(name.equals("c")) { //Cell ended
				if(isStringCell()) {
					int idx = Integer.parseInt(curCellValue);
					curCellValue = new XSSFRichTextString(stringsTable.getEntryAt(idx)).toString();
					curCellType = null;
				}
				
				row.addCell(curCellName, curCellValue);
			}else if (name.equals("sheetData")){
				onSheetDataEnd();
			}
		}

		public void characters(char[] ch, int start, int length){
			curCellValue += new String(ch, start, length);
		}
		
		private boolean isStringCell(){
			return curCellType != null && curCellType.equals("s");
		}
	}
}
