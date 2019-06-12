package com.liuzi.util.verify;

import java.util.Properties;

import javax.annotation.PostConstruct;

import lombok.Data;

import org.springframework.context.annotation.Bean;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;


@Data
public class VerifyConfig{
	
	/**
	 *  Constant 							|						描述 							|	默认值
	 *  ----------------------------------------------------------------------------------------------------------------------------------------------
		kaptcha.border  					|		图片边框，合法值：yes , no 						|	yes	
		kaptcha.border.color				|		边框颜色，合法值： r,g,b (and optional alpha) 
													或者 white,black,blue. 							|	black
		kaptcha.image.width					|		图片宽											|	200
		kaptcha.image.height				|		图片高											|	50
		kaptcha.producer.impl				|	图片实现类											|	com.google.code.kaptcha.impl.DefaultKaptcha
		kaptcha.textproducer.impl			|	文本实现类											|	com.google.code.kaptcha.text.impl.DefaultTextCreator
		kaptcha.textproducer.char.string	|	文本集合，验证码值从此集合中获取						|	abcde2345678gfynmnpwx
		kaptcha.textproducer.char.length	|	验证码长度											|	5
		kaptcha.textproducer.font.names		|	字体													|	Arial, Courier
		kaptcha.textproducer.font.size		|	字体大小												|	40px.
		kaptcha.textproducer.font.color		|	字体颜色，合法值： r,g,b  或者 white,black,blue.		|	black
		kaptcha.textproducer.char.space		|	文字间隔												|	2
		kaptcha.noise.impl					|	干扰实现类											|	com.google.code.kaptcha.impl.DefaultNoise
		kaptcha.noise.color					|	干扰 颜色，合法值： r,g,b 或者 white,black,blue.		|	black
		kaptcha.obscurificator.impl			|	图片样式：											|	com.google.code.kaptcha.impl.WaterRipple
												水纹 com.google.code.kaptcha.impl.WaterRipple 
												鱼眼 com.google.code.kaptcha.impl.FishEyeGimpy 
												阴影 com.google.code.kaptcha.impl.ShadowGimpy
		kaptcha.background.impl				|	背景实现类											|	com.google.code.kaptcha.impl.DefaultBackground
		kaptcha.background.clear.from		|	背景颜色渐变，开始颜色								|	light grey
		kaptcha.background.clear.to			|	背景颜色渐变， 结束颜色								|	white
		kaptcha.word.impl					|	文字渲染器											|	com.google.code.kaptcha.text.impl.DefaultWordRenderer
		kaptcha.session.key					|	session key											|	KAPTCHA_SESSION_KEY
		kaptcha.session.date				|	session date										|	KAPTCHA_SESSION_DATE
	 * 
	 */
	private static final String DEFAULT_BORDER = "yes";
	private static final String DEFAULT_BORDER_COLOR = "105,179,90";
	
	private static final String DEFAULT_BACKGROUND_FROM = "light grey";
	private static final String DEFAULT_BACKGROUND_TO = "white";
	
	private static final int DEFAULT_IMG_WIDTH = 125;
	private static final int DEFAULT_IMG_HEIGHT = 45;
	
	private static final String DEFAULT_FONT_COLOR = "blue";
	private static final int DEFAULT_FONT_SIZE = 45;
	private static final String DEFAULT_FONT_NAMES = "宋体,楷体,微软雅黑";
	
	private static final int DEFAULT_CHAR_LENGTH = 4;
	
	private static final String DEFAULT_NOISE_COLOR = "black";
	
	private static final String DEFAULT_SESSION_KEY = "SESSION_VERIFY_CODE";
	private static final String DEFAULT_SESSION_DATE = "SESSION_VERIFY_DATE";
	
	public static final boolean DEFAULT_USE_SESSION = true;
	
	private String border = DEFAULT_BORDER;
	private String borderColor = DEFAULT_BORDER_COLOR;
	
	private String backgroundFrom = DEFAULT_BACKGROUND_FROM;
	private String backgroundTo = DEFAULT_BACKGROUND_TO;
	
	private int imgWidth = DEFAULT_IMG_WIDTH;
	private int imgHeight = DEFAULT_IMG_HEIGHT;
	
	private String fontColor = DEFAULT_FONT_COLOR;
	private String fontNames = DEFAULT_FONT_NAMES;
	private int fontSize = DEFAULT_FONT_SIZE;
	
	private int charLength = DEFAULT_CHAR_LENGTH;
	
	private String noiseColor = DEFAULT_NOISE_COLOR;
	
	private String sessionKey = DEFAULT_SESSION_KEY;
	private String sessionDate = DEFAULT_SESSION_DATE;
	
	private Properties p;
	
	static String staticSessionKey = DEFAULT_SESSION_KEY;
	static String staticSessionDate = DEFAULT_SESSION_DATE;
	
	@PostConstruct
	public void p(){
		p = new Properties();
		p.setProperty("kaptcha.border", border);
		p.setProperty("kaptcha.border.color", borderColor);
		
		p.setProperty("kaptcha.background.clear.from", backgroundFrom);
		p.setProperty("kaptcha.background.clear.to", backgroundTo);
		
		p.setProperty("kaptcha.image.width", imgWidth + "");
		p.setProperty("kaptcha.image.height", imgHeight + "");
		
		p.setProperty("kaptcha.textproducer.font.color", fontColor);
		p.setProperty("kaptcha.textproducer.font.size", fontSize + "");
		p.setProperty("kaptcha.textproducer.font.names", fontNames);
		
		p.setProperty("kaptcha.textproducer.char.length", charLength + "");
		
		p.setProperty("kaptcha.noise.color", noiseColor);
		
		p.setProperty("kaptcha.session.key", sessionKey);
		p.setProperty("kaptcha.session.date", sessionDate);
		
		staticSessionKey = sessionKey;
		staticSessionDate = sessionDate;
	}
	
	@Bean
	public DefaultKaptcha kaptcha(){
		DefaultKaptcha kaptcha = new DefaultKaptcha();
		kaptcha.setConfig(new Config(p));
		return kaptcha;
	}
}
