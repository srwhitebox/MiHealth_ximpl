package com.ximpl.lib.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.docx4j.Docx4J;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;

public class DocxDocument {
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	private static long chunkNum = 0;
	private WordprocessingMLPackage sourcePkg;
	
	public DocxDocument(){
		try {
			sourcePkg = WordprocessingMLPackage.createPackage();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}
	
	public DocxDocument(String filePath){
		open(filePath);
	}
	
	public void open(String filePath){
		open(new File(filePath));
	}
	
	public void open(File file){
		try {
			sourcePkg = Docx4J.load(file);
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
	
	public void insert(WordprocessingMLPackage wordPkg){
		final byte[] bytes = wordPkg == null ? null : toBytes(wordPkg);
		
		if (bytes == null)
			return;
		
		final MainDocumentPart mainPart = sourcePkg.getMainDocumentPart();
	    
	    try{
	        AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/part" + (chunkNum++) + ".docx"));
	        afiPart.setContentType(new ContentType(CONTENT_TYPE));
	        afiPart.setBinaryData(bytes);
	        Relationship altChunkRel = mainPart.addTargetPart(afiPart);
	
	        CTAltChunk chunk = Context.getWmlObjectFactory().createCTAltChunk();
	        chunk.setId(altChunkRel.getId());
	
	        mainPart.addObject(chunk);
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
	}
	
	public static byte[] toBytes(WordprocessingMLPackage wordPkg){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			wordPkg.save(bos);
			
			return bos.toByteArray();
		} catch (Docx4JException e1) {
			e1.printStackTrace();
			
			return null;
		}
	}
}
