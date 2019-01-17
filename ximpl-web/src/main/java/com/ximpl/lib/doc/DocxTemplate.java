package com.ximpl.lib.doc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;

import org.docx4j.Docx4J;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.Spacing;
import org.docx4j.wml.STLineSpacingRule;
import org.docx4j.wml.Text;

@SuppressWarnings("restriction")
public class DocxTemplate {
	private WordprocessingMLPackage sourcePkg = null;
	private byte[] bytes = null;
	private Map<String, Object> model;
	
	public DocxTemplate(String filePath){
		open(filePath);
	}
	
	public void open(String filePath){
		open(new File(filePath));
	}
	
	public void open(File file){
		try {
			this.sourcePkg = Docx4J.load(file);
			this.bytes = DocxDocument.toBytes(sourcePkg);
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
	}
	
	public void write(String filePath){
		try {
			sourcePkg.save(new File(filePath));
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
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

	public WordprocessingMLPackage patch(){
		try {
			WordprocessingMLPackage tempPkg = Docx4J.load(new ByteArrayInputStream(this.bytes));
			//WordprocessingMLPackage tempPkg = Docx4J.clone(sourcePkg);
			patch(tempPkg);
			return tempPkg;
		} catch (Docx4JException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
	    List<Object> result = new ArrayList<Object>();
	    if (obj instanceof JAXBElement) obj = ((JAXBElement<?>) obj).getValue();

	    if (obj.getClass().equals(toSearch))
	        result.add(obj);
	    else if (obj instanceof ContentAccessor) {
	        List<?> children = ((ContentAccessor) obj).getContent();
	        for (Object child : children) {
	            result.addAll(getAllElementFromObject(child, toSearch));
	        }
	    }
	    return result;
	}

	private void patch(WordprocessingMLPackage wordPkg){
	    List<Object> paragraphs = getAllElementFromObject(wordPkg.getMainDocumentPart(), P.class);
	    for(Object paragraphObject : paragraphs){
	        P paragraph = (P) paragraphObject;
	        
	        List<Object> texts = getAllElementFromObject(paragraph, Text.class);
	        Text fieldText = null;
	        StringBuilder sbField = new StringBuilder();
	        for(Object text : texts){
	            Text curText = (Text)text;
	            String textContent = curText.getValue();
	            if(textContent.contains("{{")){
	            	fieldText = curText;
	            }
	            if (fieldText != null){
	            	sbField.append(textContent);
	            	fieldText.setValue(null);
	            }
	            if (textContent.contains("}}")){
	            	String value = getFieldValue(sbField.toString());
	            	fieldText.setValue(value);
	            	sbField = new StringBuilder();
	            	fieldText = null;
	            }
	        }
	       
	    }
	}
	
	/**
	 * Parse key and format and apply model value
	 * @param source
	 * @return
	 */
	private String getFieldValue(String source){
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
