package com.liuzi.util.excel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;


import com.liuzi.util.ConfigUtil;

/**
 * 图片压缩
 * @author zsy
 *
 */
@Slf4j
public class ImgCompress {
	
	private final static int def_width = 200;
	private final static int def_height = 0;
	
	public static String compress(String fileName){
		return compress(fileName, def_width, def_height);
	}
    
    public static String compress(String fileName, int width, int height){
    	if(width == 0 && height == 0){
    		return fileName;
    	}
    	
    	String path = ConfigUtil.getStringValue("cto.upload.file.path");
    	
    	String filePath = path + fileName;
    	File file = new File(filePath);  
    	
    	String fn = fileName.substring(0, fileName.lastIndexOf("."));
    	fn += "_compress";//新图片名
    	String ff = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//后缀
    	
    	String newFileName = fn + "." + ff;
    	
    	String newFilePath = path + newFileName;
    	File newFile = new File(newFilePath);
    	
    	try(
                OutputStream out = new FileOutputStream(newFile);//输出
        	){
    		
    		BufferedImage bi = ImageIO.read(file);
            double srcWidth = bi.getWidth(); // 源图宽度
            double srcHeight = bi.getHeight(); // 源图高度
     
            double scale = 1;
     
            if (width > 0) {
                scale = width / srcWidth;
            }
            if (height > 0) {
                scale = height / srcHeight;
            }
            if (width > 0 && height > 0) {
                scale = height / srcHeight < width / srcWidth ? height / srcHeight
                        : width / srcWidth;
            }
            
            thumbnail(file, (int) (srcWidth * scale), (int) (srcHeight * scale), out);
        }catch (Exception e) {
            log.error("压缩图片出错：" + e.getMessage());
        }
    	
    	return newFileName;
    }
    
    /**
     * 按照固定宽高原图压缩
     * 
     * @param img
     * @param width
     * @param height
     * @param out
     * @throws IOException
     */
    public static void thumbnail(File file, int width, int height,
            OutputStream out) throws IOException {
        BufferedImage BI = ImageIO.read(file);
        Image image = BI.getScaledInstance(width, height, Image.SCALE_SMOOTH);
 
        BufferedImage tag = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.setColor(Color.RED);
        g.drawImage(image, 0, 0, null); // 绘制处理后的图
        g.dispose();
        ImageIO.write(tag, "JPEG", out);
    }
    
    /**
     * 按照宽高裁剪
     * 
     * @param fileName
     * @param width
     * @param height
     * @param out
     */
    public static void cut(String fileName, int width, int height) {
    	
    	String path = ConfigUtil.getStringValue("cto.upload.file.path");
    	String filePath = path + fileName;
    	File file = new File(filePath);  
    	
    	String fn = fileName.substring(0, fileName.lastIndexOf("."));
    	fn += "_compress";//新图片名
    	String ff = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());//后缀
    	
    	String newFilePath = path + fn + "." + ff;
    	File newFile = new File(newFilePath);
    	
    	try(
                OutputStream out = new FileOutputStream(newFile);//输出
        	){
            
            cutWH(file, 0, 0, width, height, out);
        }catch (Exception e) {
            log.error("上传文件出错：" + e.getMessage());
        }
    	
    	
        
    }
 
    public static void cutWH(File srcImageFile, int x, int y, int destWidth,
            int destHeight, OutputStream out) {
        try {
            Image img;
            ImageFilter cropFilter;
            // 读取源图像
            BufferedImage bi = ImageIO.read(srcImageFile);
            int srcWidth = bi.getWidth(); // 源图宽度
            int srcHeight = bi.getHeight(); // 源图高度
 
            if (srcWidth >= destWidth && srcHeight >= destHeight) {
                Image image = bi.getScaledInstance(srcWidth, srcHeight,
                        Image.SCALE_DEFAULT);
 
                cropFilter = new CropImageFilter(x, y, destWidth, destHeight);
                img = Toolkit.getDefaultToolkit().createImage(
                        new FilteredImageSource(image.getSource(), cropFilter));
                BufferedImage tag = new BufferedImage(destWidth, destHeight,
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = tag.getGraphics();
                g.drawImage(img, 0, 0, null); // 绘制截取后的图
                g.dispose();
                // 输出为文件
                ImageIO.write(tag, "JPEG", out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*public static void main(String[] args) throws Exception {  
    	System.out.println("开始：" + new Date().toLocaleString());  
    	compress("test.123.jpg", 200, 0);
    	System.out.println("结束：" + new Date().toLocaleString());
	}  */
}
