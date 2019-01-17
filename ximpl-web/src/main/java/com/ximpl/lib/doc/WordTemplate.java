package com.ximpl.lib.doc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.ximpl.lib.util.XcStringUtils;

/**
 * MS Word document(*.docx) template builder 
 */
public class WordTemplate {
	
	private XWPFDocument document;
	private Map<String, Object> model;
	private byte[] sourceBytes;
	
	/**
	 * Constructor
	 * @param filePath
	 */
	public WordTemplate(String filePath){
		open(filePath);
	}
	
	/**
	 * Open document with given path
	 * @param filePath
	 */
	public void open(String filePath){
		open(new File(filePath));
	}
	
	/**
	 * Open document with given file
	 * @param file
	 */
	public void open(File file){
		try {
			if (file.exists()){
				setSourceBytes(OPCPackage.open(file));
				if (this.sourceBytes != null)
					open();
			}
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}			
	}

	/**
	 * Reset template document
	 */
	public void reset(){
		this.close();
		this.open();
	}
	
	/**
	 * Open document as inputstream
	 * @param is
	 */
	private void open(){
		try {
			document = new XWPFDocument(OPCPackage.open(new ByteArrayInputStream(this.sourceBytes)));
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Set source byte with given OPCPackage
	 * @param pkg
	 */
	private void setSourceBytes(OPCPackage pkg){
		this.sourceBytes = packageToBytes(pkg);
	}
	
	/**
	 * Convert OPCPackage to byte array
	 * @param pkg
	 * @return
	 */
	private byte[] packageToBytes(OPCPackage pkg){
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			pkg.save(bos);
			pkg.close();
			return bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Close document
	 */
	public void close(){
		try {
			if (document != null)
				document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Return the document
	 * @return
	 */
	public XWPFDocument getDocument(){
		return this.document;
	}
	
	/**
	 * Return model
	 * @return
	 */
	public Map<String,Object> getModel(){
		if (this.model == null)
			this.model = new HashMap<String, Object>();
		return this.model;
	}
	
	/**
	 * Set model
	 * @param model
	 */
	public void setModel(Map<String,Object> model){
		this.model = model;
	}
	
	/**
	 * Set model key and value
	 * @param key
	 * @param value
	 */
	public void setModel(String key, Object value){
		getModel().put(key,  value);
	}
	
	/**
	 * Patch document
	 */
	public void patch(){
		for(IBodyElement bodyElement : document.getBodyElements()){
			switch(bodyElement.getElementType()){
			case PARAGRAPH:
				patch((XWPFParagraph)bodyElement);
				break;
			case TABLE:
				patch((XWPFTable)bodyElement);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Write to a file with given path
	 * @param filePath
	 */
	public void write(String filePath){
		write (new File(filePath));
	}
	
	/**
	 * Write to a file
	 * @param file
	 */
	public void write(File file){
		try {
			write(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Write to output stream
	 * @param os
	 */
	public void write(OutputStream os){
		try {
			if (os != null){
				patch();
				document.write(os);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	
	/**
	 * Patch paragraph
	 * @param paragraph
	 */
	private void patch(XWPFParagraph paragraph){
		paragraph.setSpacingAfter(0);
		paragraph.setSpacingBefore(0);

		final TextSegement segmentStart = paragraph.searchText("{{", new PositionInParagraph());
		if (segmentStart == null)
			return;
		XWPFRun run = paragraph.getRuns().get(segmentStart.getBeginRun());
		
		TextSegement segmentEnd = paragraph.searchText("}}", new PositionInParagraph());
		if (segmentEnd == null)
			return;
		TextSegement tempEnd = null;
		do{
			tempEnd = paragraph.searchText("}}", segmentEnd.getEndPos());
			if (tempEnd != null)
				segmentEnd = tempEnd;
		}while(tempEnd != null);
		
		final StringBuilder sb = new StringBuilder(run.getText(0));
		final int startIndex = segmentStart.getBeginRun() +1;
		final int endIndex = segmentEnd.getEndRun() +1;
		int count = endIndex - startIndex;
		for(int i = startIndex; i < startIndex + count; i++){
			sb.append(paragraph.getRuns().get(startIndex));
			
			paragraph.removeRun(startIndex);
		}
		run.setText(parseText(sb.toString()), 0);
	}
	
	/**
	 * Patch table
	 * @param table
	 */
	private void patch(XWPFTable table){
	   for (XWPFTableRow row : table.getRows()) {
	      for (XWPFTableCell cell : row.getTableCells()) {
	         for (XWPFParagraph p : cell.getParagraphs()) {
	        	 patch(p);
	         }
	      }
	   }
	}
	
	/**
	 * Parse key and format and apply model value
	 * @param source
	 * @return
	 */
	public String parseText(String source){
		final Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
		final Matcher matcher = pattern.matcher(source);
		final StringBuilder sb = new StringBuilder();
		int startIndex = 0;
		while(matcher.find()){
			sb.append(source.substring(startIndex, matcher.start()));
			final String group = matcher.group(1);
			final int index = group.indexOf('|');
			final String key = index < 0 ? group : group.substring(0,  index);
			final String format = index < 0 ? null : group.substring(index+1);
			final Object value = model.get(key);

			if (value != null){
				if (format==null)
					sb.append(value);
				else{
					if (value instanceof Date){
						final SimpleDateFormat formatter = new SimpleDateFormat(format);
						sb.append(formatter.format(value));
					}else
						sb.append(String.format(format, value));
				}
			}
			startIndex = matcher.end();
		}
		if (startIndex < source.length())
			sb.append(source.substring(startIndex));
		
		return sb.toString();
	}
}
