package com.liuzi.fastdfs;




import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;

import com.liuzi.fastdfs.base.ClientGlobal;
import com.liuzi.util.common.ImgUtil;


@Slf4j
public class ImgReset {
	
	public static void excute(FdfsFile fdfsFile, HttpServletRequest request){
		//是否需要水印
		String watermark = request.getParameter("watermark");
		boolean wm = StringUtils.isEmpty(watermark) ? ClientGlobal.g_watermark : Boolean.parseBoolean(watermark);
		//是否需要压缩
		String compress = request.getParameter("compress");
		boolean cp = StringUtils.isEmpty(compress) ? ClientGlobal.g_compress : Boolean.parseBoolean(compress);
		
		if(!wm && !cp){
			return;
		}
		
		int wmw = 0, wmh = 0, wmfs = 0;
		String watermarkContent = null;
		//如果需要水印，获取水印参数
		if(wm){
			if(ClientGlobal.g_watermark_img){
				//图片水印宽
				String watermarkWidth = request.getParameter("watermarkWidth");
				wmw = StringUtils.isEmpty(watermarkWidth) ? ClientGlobal.g_watermark_img_width : Integer.parseInt(watermarkWidth);
				//图片水印高
				String watermarkHeight = request.getParameter("watermarkHeight");
				wmh = StringUtils.isEmpty(watermarkHeight) ? ClientGlobal.g_watermark_img_height : Integer.parseInt(watermarkHeight);
			}else{
				//文字水印字体大小
				String watermarkFontSize = request.getParameter("watermarkFontSize");
				wmfs = StringUtils.isEmpty(watermarkFontSize) ? ClientGlobal.g_watermark_font_size : Integer.parseInt(watermarkFontSize);
				watermarkContent = request.getParameter("watermarkContent");
			}
		}
		excute(fdfsFile, cp, wm, wmw, wmh, wmfs, watermarkContent);
	}
	
	/**
	 * 
	 * @param fdfsFile
	 * @param watermark 是否添加水印
	 * @param watermarkWidth 图片水印宽
	 * @param watermarkHeight 图片水印高
	 * @param watermarkFontSize 文字水印字体大小
	 * @param watermarkContent 文字水印内容
	 */
	public static void excute(FdfsFile fdfsFile, boolean compress, boolean watermark, int watermarkWidth, 
			int watermarkHeight, int watermarkFontSize, String watermarkContent){
		
		//原图目录 /temp/abc.png
		String wholePath = fdfsFile.getWholePath();
		//扩展名 png
		String suffix = fdfsFile.getSuffix();
		
		//不包含扩展名目录 /temp/abc
		String namePath = wholePath.substring(0, wholePath.lastIndexOf("."));
		
		//大图目录 /temp/abc_L.png
		String large = namePath + "_L." + suffix;
		//中图目录 /temp/abc_M.png
		String middle = namePath + "_M." + suffix;
		//小IOException图目录 /temp/abc_S.png
		String small = namePath + "_S." + suffix;
		
		try{
			log.info("大图处理...");
			imgutil(wholePath, suffix, large, ClientGlobal.g_compressScaleLarge, 
					watermark, (int)(watermarkWidth * ClientGlobal.g_compressScaleLarge), 
					(int)(watermarkHeight * ClientGlobal.g_compressScaleLarge), watermarkFontSize, 
					watermarkContent, compress);
			fdfsFile.setLarge(ClientGlobal.fileServer + large);
		}catch(IOException e){
			log.info("大图处理失败，使用原图，原因：" + e.getMessage());
			fdfsFile.setLarge(fdfsFile.getUrl());
		}
		
		try{
			log.info("中图处理...");
			imgutil(wholePath, suffix, middle, ClientGlobal.g_compressScaleMiddle, 
					watermark, (int)(watermarkWidth * ClientGlobal.g_compressScaleMiddle), 
					(int)(watermarkHeight * ClientGlobal.g_compressScaleMiddle), watermarkFontSize, 
					watermarkContent, compress);
			fdfsFile.setMiddle(ClientGlobal.fileServer + middle);
		}catch(IOException e){
			log.info("中图处理失败，使用原图，原因：" + e.getMessage());
			fdfsFile.setMiddle(fdfsFile.getUrl());
		}
		
		try{
			log.info("小图处理...");
			imgutil(wholePath, suffix, small, ClientGlobal.g_compressScaleSmall, 
					watermark, (int)(watermarkWidth * ClientGlobal.g_compressScaleSmall), 
					(int)(watermarkHeight * ClientGlobal.g_compressScaleSmall), watermarkFontSize, 
					watermarkContent, compress);
			fdfsFile.setSmall(ClientGlobal.fileServer + small);
		}catch(IOException e){
			log.info("小图处理失败，使用原图，原因：" + e.getMessage());
			fdfsFile.setSmall(fdfsFile.getUrl());
		}
	}
	
	private static void imgutil(String wholePath, String suffix, String outputPath,
			float scale, boolean watermark, int watermarkWidth, int watermarkHeight,
			int watermarkFontSize, String watermarkContent, boolean compress) throws IOException{
		
		if(!compress && !watermark){
			return;
		}
		
		ImgUtil imgUtil = new ImgUtil(wholePath, suffix, ClientGlobal.g_quality);
		
		//是否添加水印
		if(watermark){
			//添加水印
			imgUtil.watermark(ClientGlobal.g_watermark_positions, ClientGlobal.g_watermark_quality);
			//图片水印
			if(ClientGlobal.g_watermark_img){
				if(!StringUtils.isEmpty(ClientGlobal.g_watermark_img_path)){
					watermarkWidth = watermarkWidth < 0 ? ClientGlobal.g_watermark_img_width : watermarkWidth;
					watermarkHeight = watermarkHeight < 0 ? ClientGlobal.g_watermark_img_height : watermarkHeight;
					imgUtil.watermarkByImg(ClientGlobal.g_watermark_img_path, watermarkWidth, watermarkHeight);
				}
			}else{
				//文字水印字体大小
				watermarkFontSize = watermarkFontSize < 0 ? ClientGlobal.g_watermark_font_size : watermarkFontSize;
				imgUtil.watermarkByFont(watermarkContent, watermarkFontSize, ClientGlobal.g_watermark_font_color);
			}
		}
		
		//等比压缩
		if(compress){
			imgUtil.compressByScale(scale);
		}
		
		imgUtil.toFile(outputPath);
	}
	
	
	
    public static void main(String[] args) {
    	
    	/*ClientGlobal.g_quality = 1f;
    	ClientGlobal.g_compressScaleLarge = 1f;
    	ClientGlobal.g_compressScaleMiddle = 0.7f;
    	ClientGlobal.g_compressScaleSmall = 0.4f;
    	ClientGlobal.g_watermark_positions = Positions.BOTTOM_RIGHT;
    	ClientGlobal.g_watermark_quality = 0.8f;
    	ClientGlobal.g_watermark_img = false;
    	ClientGlobal.g_watermark_img_path = "C:\\Users\\Administrator\\Desktop\\test\\lot1_2.jpg";
    	ClientGlobal.g_watermark_img_width = 100;
    	ClientGlobal.g_watermark_img_height = 50;
    	ClientGlobal.g_watermark_font_size = 15;
    	ClientGlobal.g_watermark_font_color = Color.red;
    	
    	*/
    	
    	FdfsFile fdfsFile = new FdfsFile();
    	fdfsFile.setWholePath("C:\\Users\\Administrator\\Desktop\\test\\lot11.jpg");
    	fdfsFile.setSuffix("jpg");
    	
    	//excute(fdfsFile, true, 200, 100, 0, null);
    	//excute(fdfsFile, true, 0, 0, 20, "你好少年！");
	}
}
