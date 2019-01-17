package com.ximpl.lib.doc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import com.ximpl.lib.util.XcStringUtils;

public class WordDocument {
	private XWPFDocument document;
	
	public WordDocument(){
		document = new XWPFDocument();
	}
	
	public void close(){
		try {
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void append(XWPFDocument source){
		copyLayout(source);
		for(IBodyElement bodyElement : source.getBodyElements()){
			switch(bodyElement.getElementType()){
			case PARAGRAPH:
				XWPFParagraph paragraph = (XWPFParagraph)bodyElement; 
				copyStyle(source, source.getStyles().getStyle(paragraph.getStyleID()));
                XWPFParagraph newParagraph = this.document.createParagraph();
                newParagraph.setSpacingAfter(0);
                paragraph.setSpacingAfter(0);
                this.document.setParagraph(paragraph, this.document.getParagraphs().size() -1);
				break;
			case TABLE:
				XWPFTable table = (XWPFTable)bodyElement;
				copyStyle(source, source.getStyles().getStyle(table.getStyleID()));
				this.document.createTable();
				this.document.setTable(this.document.getTables().size()-1, table);
				break;
			default:
				break;
			}
		}
		
		this.addPageBreak();
	}
	
	public void addPageBreak(){
		XWPFParagraph paragraph = this.document.createParagraph();
		paragraph.setPageBreak(true);		
	}
	
	private void copyStyle(XWPFDocument srcDoc, XWPFStyle style){
        if (this.document == null || style == null)
            return;

        if (this.document.getStyles() == null) {
        	this.document.createStyles();
        }

        List<XWPFStyle> usedStyleList = srcDoc.getStyles().getUsedStyleList(style);
        for (XWPFStyle xwpfStyle : usedStyleList) {
        	this.document.getStyles().addStyle(xwpfStyle);
        }
    }
	
	private void copyLayout(XWPFDocument srcDoc){
//        CTPageMar pgMar = srcDoc.getDocument().getBody().getSectPr().getPgMar();
//
//        BigInteger bottom = pgMar.getBottom();
//        BigInteger footer = pgMar.getFooter();
//        BigInteger gutter = pgMar.getGutter();
//        BigInteger header = pgMar.getHeader();
//        BigInteger left = pgMar.getLeft();
//        BigInteger right = pgMar.getRight();
//        BigInteger top = pgMar.getTop();
//
//        CTPageMar addNewPgMar = this.document.getDocument().getBody().addNewSectPr().addNewPgMar();
//
//        addNewPgMar.setBottom(bottom);
//        addNewPgMar.setFooter(footer);
//        addNewPgMar.setGutter(gutter);
//        addNewPgMar.setHeader(header);
//        addNewPgMar.setLeft(left);
//        addNewPgMar.setRight(right);
//        addNewPgMar.setTop(top);
//
//        CTPageSz pgSzSrc = srcDoc.getDocument().getBody().getSectPr().getPgSz();
//
//        BigInteger code = pgSzSrc.getCode();
//        BigInteger h = pgSzSrc.getH();
//        org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.Enum orient = pgSzSrc.getOrient();
//        BigInteger w = pgSzSrc.getW();
//
//        CTPageSz addNewPgSz = this.document.getDocument().getBody().addNewSectPr().addNewPgSz();
//
//        addNewPgSz.setCode(code);
//        addNewPgSz.setH(h);
//        addNewPgSz.setOrient(orient);
//        addNewPgSz.setW(w);
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

}
