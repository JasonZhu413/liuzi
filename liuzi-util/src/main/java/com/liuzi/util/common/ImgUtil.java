package com.liuzi.util.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

@Slf4j
public class ImgUtil {
	
	private static final String SUFFIX = "png";
	private static final float QUALITY = 1;
	private static final float SCALE = 1;
	private static final Positions POSITIONS = Positions.BOTTOM_RIGHT;
	private static final float WATERMARK_QUALITY = 1;
	
	private Builder<File> builder;
	
	private boolean success = false;
	private boolean watermark = false;
	private Boolean watermarkByImg;
	private Positions g_positions = POSITIONS;
	private float g_watermarkQuality = WATERMARK_QUALITY;
	
	public ImgUtil(String filePath){
		this(filePath, SUFFIX, QUALITY);
	}
	
	public ImgUtil(String filePath, String outputSuffix){
		this(filePath, outputSuffix, QUALITY);
	}
	
	public ImgUtil(String filePath, float quality){
		this(filePath, SUFFIX, quality);
	}
	
	public ImgUtil(String filePath, String outputSuffix, float quality){
		if(StringUtils.isEmpty(filePath)){
			throw new IllegalArgumentException("[图片处理] 参数错误");
		}
		
		builder = Thumbnails.of(filePath);
		//扩展名
		outputSuffix = StringUtils.isEmpty(outputSuffix) ? SUFFIX : outputSuffix;
		builder.outputFormat(outputSuffix);
		debug("[图片处理] 扩展名：" + outputSuffix);
		//透明度
		quality = quality <= 0 || quality > 1 ? QUALITY : quality;
		builder.outputQuality(quality);
		debug("[图片处理] 透明度：" + quality);
		
		success = true;
	}
	
	/**
	 * 等比压缩
	 * @return
	 */
	public ImgUtil compressByScale(){
		return compressByScale(SCALE);
	}
	
	/**
	 * 等比压缩
	 * @return
	 */
	public ImgUtil compressByScale(float scale){
		//压缩比例
		scale = scale <= 0 || scale > 1 ? SCALE : scale;
		builder.scale(scale);
		debug("[图片处理] 等比压缩，压缩比例：" + scale);
		return this;
	}
	
	/**
	 * 按宽高压缩
	 * @return
	 */
	public ImgUtil compressByWidthAndHeight(int width, int height){
		if(width > 0 && height > 0){
			//宽高压缩
			builder.keepAspectRatio(false);
			builder.size(width, height);
			debug("[图片处理] 宽高压缩，宽：" + width + "，高：" + height);
		}
		return this;
	}
	
	/**
	 * 添加水印
	 * @return
	 */
	public ImgUtil watermark(Positions positions, float quality){
		debug("[图片处理] 添加水印，开始...");
		if(positions != null){
			g_positions = positions;
		}
		debug("[图片处理] 水印位置：" + g_positions);
		//水印透明度
		if(quality > 0 && quality < 1){
			g_watermarkQuality = quality;
		}
		debug("[图片处理] 水印透明度：" + g_watermarkQuality);
		
		watermark = true;
		return this;
	}
	
	/**
	 * 添加图片水印
	 * @return
	 * @throws IOException 
	 */
	public ImgUtil watermarkByImg(String path, int width, int height) throws IOException{
		if(watermark && (watermarkByImg == null || watermarkByImg)){
			debug("[图片处理] 图片水印，宽：" + width + "，高：" + height + "目录：" + path);
			BufferedImage bufferedImage = Thumbnails.of(path)
				        .size(width, height)
				        .asBufferedImage();
			builder.watermark(g_positions, bufferedImage, g_watermarkQuality);
			watermarkByImg = true;
		}
		return this;
	}
	
	/**
	 * 添加文字水印
	 * @return
	 */
	public ImgUtil watermarkByFont(String content, int fontSize, Color fontColor){
		if(watermark && (watermarkByImg == null || !watermarkByImg)){
			debug("[图片处理] 文字水印，内容：" + content);
			BufferedImage bufferedImage = handleTextWaterMark(content, fontSize, fontColor);
			
			builder.watermark(g_positions, bufferedImage, g_watermarkQuality);
			watermarkByImg = false;
		}
		return this;
	}
	
	
	/**
	 * 输出文件
	 * @return
	 * @throws IOException 
	 */
	public void toFile(String outputPath) throws IOException{
		if(!success || StringUtils.isEmpty(outputPath)){
			debug("[图片处理] 失败，未初始化或输出文件目录缺失...");
		}
		builder.toFile(outputPath);
	}
	
	
	/**
	 * 图片处理
	 * @param filePath 原图片目录
	 * @param suffix 原图片扩展名
	 * @param outputPath 新图片目录
	 * @param keepAspectRatio 是否等比缩放
	 * @param scale 等比缩放比例
	 * @param width 缩放宽度（缩放比例为false）
	 * @param height 缩放高度（缩放比例为false）
	 * @param quality 透明度
	 * @param watermark 是否添加水印
	 * @param positions 水印位置
	 * @param wartermarkImg 水印是否为图片
	 * @param watermarkWidth 水印宽度（水印为图片）
	 * @param watermarkHeight 水印高度（水印为图片）
	 * @param fontSize 水印文字大小（水印为文字）
	 * @param fontColor 水印文字颜色（水印为文字）
	 * @param watermarkContent 水印内容（若水印为文字，则为文字内容，若为图片，则为图片目录）
	 * @param watermarkPositions 水印方向
	 * @param watermarkQuality 水印透明度
	 */
	/*public void reset(String filePath, String suffix, String outputPath, boolean keepAspectRatio, 
			float scale, int width, int height, float quality, boolean watermark, Positions positions,
			boolean wartermarkImg, int watermarkWidth, int watermarkHeight, int fontSize, Color fontColor, 
			String watermarkContent, String watermarkPositions, float watermarkQuality){
		long start = System.currentTimeMillis();
		try{
			debug("[图片处理] 开始...");
			Builder<File> builder = Thumbnails.of(filePath);
			//扩展名
			builder.outputFormat(StringUtils.isEmpty(suffix) ? "png" : suffix);
			debug("[图片处理] 扩展名：" + suffix);
			//透明度
			quality = quality <= 0 || quality > 1 ? 1 : quality;
			builder.outputQuality(quality);
			debug("[图片处理] 透明度：" + quality);
			//压缩
			if(keepAspectRatio){//等比压缩
				//压缩比例
				scale = scale <= 0 || scale > 1 ? 1 : scale;
				builder.scale(scale);
				debug("[图片处理] 等比压缩，压缩比例：" + scale);
			}else{//宽高压缩
				if(width > 0 && height > 0){
					builder.keepAspectRatio(false);
					builder.size(width, height);
					debug("[图片处理] 宽高压缩，宽：" + width + "，高：" + height);
				}else{
					debug("[图片处理] 图片比例保持");
				}
			}
			
			//水印
			if(watermark && !StringUtils.isEmpty(watermarkContent)){
				debug("[图片处理] 添加水印，开始...");
				positions = positions == null ? Positions.BOTTOM_RIGHT : positions;
				debug("[图片处理] 水印位置：" + positions);
				//水印透明度
				watermarkQuality = watermarkQuality <= 0 || watermarkQuality > 1 ? 1 : watermarkQuality;
				debug("[图片处理] 水印透明度：" + watermarkQuality);
				BufferedImage bufferedImage;
				if(wartermarkImg){//图片水印
					debug("[图片处理] 图片水印，宽：" + watermarkWidth + "，高：" + watermarkHeight + "目录：" + watermarkContent);
					bufferedImage = Thumbnails.of(watermarkContent)
					        .size(watermarkWidth, watermarkHeight)
					        .asBufferedImage();
				}else{
					debug("[图片处理] 文字水印，内容：" + watermarkContent);
					bufferedImage = handleTextWaterMark(watermarkContent, fontSize, fontColor);
				}
				debug("[图片处理] 水印大小，宽：" + watermarkWidth + "，高：" + watermarkHeight);
				builder.watermark(positions, bufferedImage, watermarkQuality);
			}
			debug("[图片处理] 图片输出，目录：" + outputPath);
			//输出目录文件
			builder.toFile(outputPath);
			
			debug("[图片处理] 完成，耗时：" + (System.currentTimeMillis() - start) + "毫秒 ...");
		}catch(Exception e){
			log.warn("图片处理失败: " + e.getMessage());
		}
	}*/

    @SuppressWarnings("restriction")
	private static BufferedImage handleTextWaterMark(String str, int fontSize, Color color) {
    	Font font = new Font("宋体", Font.BOLD, fontSize);
    	sun.font.FontDesignMetrics metrics = sun.font.FontDesignMetrics.getMetrics(font);
    	//计算图片的宽
        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            width += metrics.charWidth(str.charAt(i));
        }
        //计算高
        int height = metrics.getHeight();
        
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        image = g.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color == null ? Color.red : color);
        g.setFont(font);
        g.drawString(str, 0, metrics.getAscent());
        g.dispose();
        return image;
    }
    
    private static void debug(String str){
    	log.info(str);
    }
}
