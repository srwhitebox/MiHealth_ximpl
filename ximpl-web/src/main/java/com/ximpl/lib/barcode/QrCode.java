package com.ximpl.lib.barcode;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.common.io.Files;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ximpl.lib.util.XcStringUtils;

/**
 * QR Code Encoder
 */
public class QrCode {
	private static final int DEFAULT_WIDHT = 220;
	private static final int DEFAULT_MARGIN = 2;

	private String code;
	private int width = DEFAULT_WIDHT;
	private int margin = DEFAULT_MARGIN;
	/**
	 * Constructor
	 */
	public QrCode(){
		
	}
	
	/**
	 * Constructor
	 * @param code
	 * @param width
	 */
	public QrCode(String code, int width){
		setCode(code);
		setWidth(width);
	}
	
	/**
	 * return barcode data
	 * @return
	 */
	public String getCode(){
		return this.code;
	}
	
	/**
	 * Set barcode data to be generated
	 * @param code
	 */
	public void setCode(String code){
		this.code = code;
	}
	
	/**
	 * return barcode image width;
	 * @return
	 */
	public int getWdith(){
		return this.width;
	}
	
	/**
	 * Set barcode image width
	 * @param width
	 */
	public void setWidth(int width){
		this.width = width;
	}
	
	public int getMargin(){
		return this.margin;
	}
	
	public void setMargin(int margin){
		this.margin = margin;
	}

	/**
	 * Return Image
	 * @return
	 */
	public BufferedImage toImage(){
		return toImage(this.code, this.width, this.margin);
	}
	
	public byte[] toBytes(String fileFormat){
		return toBytes(toImage(), fileFormat);
	}
	
	/**
	 * Save as file. If the filePath contains extension, it'll save as the file type.
	 * @param filePath
	 * @return
	 */
	public File saveAs(String filePath){
		if (XcStringUtils.isNullOrEmpty(filePath)){
			return null;
		}
		
		
		String ext = Files.getFileExtension(filePath);
		
		return saveAs(filePath, ext);
	}
	
	/**
	 * Save as file
	 * @param filePath
	 * @param fileFormat
	 * @return
	 */
	public File saveAs(String filePath, String fileFormat){
		if (XcStringUtils.isNullOrEmpty(fileFormat))
			fileFormat = "png";  
		try {
			
			File outputFile = new File(filePath);
			ImageIO.write(toImage(), fileFormat, outputFile);
			return outputFile;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] toBytes(BufferedImage image, String fileFormat){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, fileFormat, baos);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Generate barcode image and return that.
	 * @param code
	 * @param size
	 * @return
	 */
	public static BufferedImage toImage(String code, Integer size, Integer margin){
		if (XcStringUtils.isNullOrEmpty(code)){
			return null;
		}
		
		if (size == null || size <= 16)
			size = DEFAULT_WIDHT;
		if (margin == null || margin < 0)
			margin = DEFAULT_MARGIN;
		
		Map<EncodeHintType, Object>  hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		hints.put(EncodeHintType.MARGIN, margin);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		try {
			BitMatrix bitMatrix = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, size, size, hints);
			return toImage(bitMatrix);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Convert BitMatrix to Image
	 * @param bitMatrix
	 * @return
	 */
	public static BufferedImage toImage(BitMatrix bitMatrix){
		if (bitMatrix == null)
			return null;
		
		final int width = bitMatrix.getWidth();
		final int height = bitMatrix.getHeight();
        final int black = Color.BLACK.getRGB();
        final int white = Color.WHITE.getRGB();
		
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        for (int y = 0; y < height; y++) {
        	for (int x = 0; x < width; x++) {
            	image.setRGB(x, y, bitMatrix.get(x, y) ? black : white);
            }
        }
        
        return image;		
	}
}
