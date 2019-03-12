package com.liuzi.util.upoload;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.liuzi.util.common.RandomUtil;

/*
 * 缩略图
 */
public class ImageResize {
	
	private static Logger logger = LoggerFactory.getLogger(ImageResize.class);
	
	public static final MediaTracker tracker = new MediaTracker(new Component(){
		private static final long serialVersionUID = 1234162663955668507L;
	});
	
	/**
	 * fastdfs缩略图
	 * @param url 原图地址
	 * @return 缩略图全路径
	 */
	public static Map<String, Object> resize(String url){
		return resize(url, 0);
	}
	
	/**
	 * fastdfs缩略图
	 * @param url 原图地址
	 * @param width 缩略图宽
	 * @return 缩略图全路径
	 */
	public static Map<String, Object> resize(String url, int index){
		if(StringUtils.isEmpty(url)){
			throw new IllegalArgumentException("url is empty");
		}
		
		int first = url.indexOf("/");
		String groupName = url.substring(0, first);
		String filePath = url.substring(first + 1);
		return resize(groupName, filePath);
	}
	
	/**
	 * fastdfs缩略图
	 * @param group 原图group
	 * @param path 原图path
	 * @return 缩略图全路径
	 */
	public static Map<String, Object> resize(String group, String path){
		return resize(group, path, 0);
	}
	
	/**
	 * fastdfs缩略图
	 * @param group 原图group
	 * @param path 原图path
	 * @param width 缩略图宽
	 * @return 缩略图全路径
	 */
	public static Map<String, Object> resize(String group, String path, int width){
		if(StringUtils.isEmpty(group) || StringUtils.isEmpty(path)){
			throw new IllegalArgumentException("url is empty");
		}
		
		String url = group + "/" + path;
		
		Map<String, Object> map = resize_object(url, width);
		map.put("group", group);
		map.put("path", path);
		
		return map;
	}
	
	/**
	 * 正常缩略图
	 * @param url 原图地址
	 * @return 缩略图全路径
	 */
	public static Map<String, Object> normalResize(String url){
		return resize_object(url, 0);
	}
	
	/**
	 * 正常缩略图
	 * 名字：原图名称 + ImageResize.file_resize_name
	 * @param url 原图地址
	 * @param width 缩略图宽
	 * @return 缩略图全路径
	 */
	public static Map<String, Object> normalResize(String url, int width){
		return resize_object(url, width);
	}
	
	private static Map<String, Object> resize_object(String url, int width){
		if(StringUtils.isEmpty(url)){
			throw new IllegalArgumentException("url is empty");
		}
				
		logger.info("压缩文件url：" + url);
		File file = new File(url);
		if(!file.exists()){
			throw new IllegalArgumentException("file is not exists");
		}
		
		width = width == 0 ? 150 : width;
		
		try {
			BufferedImage img = ImageIO.read(file);
			if(img.getWidth() < width){
				throw new IllegalArgumentException("图片已小于将要压缩的尺寸，无需压缩...");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("读取图片错误：" + e.getMessage());
		}
		
		String getPath = url.substring(0, url.lastIndexOf("/") + 1);
		String format = url.substring(url.lastIndexOf(".") + 1);
		String newName = System.currentTimeMillis() + RandomUtil.letter(6) + "." + format;
		String fileName = getPath + newName;
		
		File reFile = new File(fileName);
		
		try {
			if("gif".equals(format.toLowerCase())){
				resize(file, reFile, format, width, 1);
			}else{
				resize(file, reFile, width, format);
			}
			
			
			logger.info("图片压缩成功：" + fileName);
			
			Map<String, Object> map = new HashMap<>();
			map.put("oldName", newName);
			map.put("newName", newName);
			map.put("url", url);
			map.put("size", reFile.length() / 1024);
			map.put("ext", format);
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("压缩图片缩略图失败：" + e.getMessage());
		}
	}
	
	/**
	 * 缩放
     * @param originalFile 原图像
     * @param resizedFile 压缩后的图像
     * @param width 图像宽
     * @param format 图片格式 jpg, png, gif(非动画)
     * @throws IOException
	*/
	private static void resize(File originalFile, File resizedFile, int width, String format) throws IOException {
		/*if(format != null && "gif".equals(format.toLowerCase())){
			resize(originalFile, resizedFile, width, 1);
			return;
		}*/
		
		FileInputStream fis = new FileInputStream(originalFile);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		int readLength = -1;
		int bufferSize = 1024;
		byte bytes[] = new byte[bufferSize];
		while ((readLength = fis.read(bytes, 0, bufferSize)) != -1) {
			byteStream.write(bytes, 0, readLength);
		}
		byte[] in = byteStream.toByteArray();
		fis.close();
		byteStream.close();

		Image inputImage = Toolkit.getDefaultToolkit().createImage( in );
		waitForImage( inputImage );
		int imageWidth = inputImage.getWidth( null );
		if ( imageWidth < 1 ) 
			throw new IllegalArgumentException( "image width " + imageWidth + " is out of range" );
			int imageHeight = inputImage.getHeight( null );
		if ( imageHeight < 1 ) 
			throw new IllegalArgumentException( "image height " + imageHeight + " is out of range" );

		// Create output image.
		int height = -1;
		double scaleW = (double) imageWidth / (double) width;
		double scaleY = (double) imageHeight / (double) height;
		if (scaleW >= 0 && scaleY >=0) {
			if (scaleW > scaleY) {
				height = -1;
			} else {
				width = -1;
			}
		}
		Image outputImage = inputImage.getScaledInstance( width, height, java.awt.Image.SCALE_DEFAULT);
		checkImage( outputImage ); 
		encode(new FileOutputStream(resizedFile), outputImage, format); 
	} 

	/** Checks the given image for valid width and height. */
	private static void checkImage( Image image ) {
		waitForImage( image );
		int imageWidth = image.getWidth( null );
		if ( imageWidth < 1 ) 
			throw new IllegalArgumentException( "image width " + imageWidth + " is out of range" );
		int imageHeight = image.getHeight( null );
		if ( imageHeight < 1 ) 
			throw new IllegalArgumentException( "image height " + imageHeight + " is out of range" );
		}

		/** Waits for given image to load. Use before querying image height/width/colors. */
		private static void waitForImage( Image image ) {
		try {
			tracker.addImage( image, 0 );
			tracker.waitForID( 0 );
			tracker.removeImage(image, 0);
		} catch( InterruptedException e ) { e.printStackTrace(); }
	} 

	/** Encodes the given image at the given quality to the output stream. */
	private static void encode( OutputStream outputStream, Image outputImage, String format ) 
		throws java.io.IOException {
		int outputWidth = outputImage.getWidth( null );
		if ( outputWidth < 1 ) 
			throw new IllegalArgumentException( "output image width " + outputWidth + " is out of range" );
		int outputHeight = outputImage.getHeight( null );
		if ( outputHeight < 1 ) 
			throw new IllegalArgumentException( "output image height " + outputHeight + " is out of range" );

		// Get a buffered image from the image.
		BufferedImage bi = new BufferedImage( outputWidth, outputHeight,
		BufferedImage.TYPE_INT_RGB ); 
		Graphics2D biContext = bi.createGraphics();
		biContext.drawImage( outputImage, 0, 0, null );
		ImageIO.write(bi, format, outputStream);
		outputStream.flush(); 
	} 

	/**
     * 缩放gif图片
     * @param originalFile 原图片
     * @param resizedFile 缩放后的图片
     * @param format 扩展名
     * @param newWidth 宽度
     * @param quality 缩放比例 (等比例)
     * @throws IOException
	*/
	private static void resize(File originalFile, File resizedFile, String format, 
			int newWidth, float quality) throws IOException {
		if (quality < 0 || quality > 1) {
			throw new IllegalArgumentException("Quality has to be between 0 and 1");
		} 
		
		ImageIcon ii = new ImageIcon(originalFile.getCanonicalPath());
		Image i = ii.getImage();
		Image resizedImage = null; 
		int iWidth = i.getWidth(null);
		int iHeight = i.getHeight(null); 
		if (iWidth > iHeight) {
			resizedImage = i.getScaledInstance(newWidth, (newWidth * iHeight) / iWidth, Image.SCALE_SMOOTH);
		} else {
			resizedImage = i.getScaledInstance((newWidth * iWidth) / iHeight, newWidth, Image.SCALE_SMOOTH);
		} 
		// This code ensures that all the pixels in the image are loaded.
		Image temp = new ImageIcon(resizedImage).getImage(); 
		// Create the buffered image.
		BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null),
		BufferedImage.TYPE_INT_RGB); 
		// Copy image to buffered image.
		Graphics g = bufferedImage.createGraphics(); 
		// Clear background and paint the image.
		g.setColor(Color.white);
		g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
		g.drawImage(temp, 0, 0, null);
		g.dispose(); 
		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = {0, softenFactor, 0, softenFactor, 1-(softenFactor*4), softenFactor, 0, softenFactor, 0};
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null); 
		// Write the jpeg to a file.
		FileOutputStream out = new FileOutputStream(resizedFile); 
		// Encodes image as a JPEG data stream
		
		/*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage); 
		param.setQuality(quality, true); 
		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);*/
		
        ImageIO.write(bufferedImage, format, out);
	}
}
