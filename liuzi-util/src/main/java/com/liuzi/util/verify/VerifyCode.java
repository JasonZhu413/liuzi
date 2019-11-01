package com.liuzi.util.verify;


import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.liuzi.util.common.Log;

public class VerifyCode{
	
	@Getter @Setter
	private boolean useSession = true;
	@Autowired
	private DefaultKaptcha kaptcha;
	
	/**
	 * 创建验证码，存入session中
	 * @param request
	 * @param response
	 * @return
	 */
	public String create(HttpServletRequest request, HttpServletResponse response)  {
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        
        String capText = null;
        try(ServletOutputStream out = response.getOutputStream();){
        	// create the text for the image
            capText = kaptcha.createText();
            // store the text in the session
            if(useSession){
            	request.getSession().setAttribute(VerifyConfig.staticSessionKey, capText);
            	request.getSession().setAttribute(VerifyConfig.staticSessionDate, System.currentTimeMillis());
            }
            // create the image with the text
            BufferedImage bi = kaptcha.createImage(capText);
        	// write the data out
            ImageIO.write(bi, "jpg", out);
            out.flush();
            Log.info("验证码创建成功：{}", capText);
        } catch (IOException e){
        	Log.error(e, "验证码创建错误");
        }
        
        return capText;
    }
	
	/**
	 * 删除session验证码
	 * @param request
	 */
	public void remove(HttpServletRequest request){
		if(useSession){
			HttpSession session = request.getSession();
			session.removeAttribute(VerifyConfig.staticSessionKey);
			session.removeAttribute(VerifyConfig.staticSessionDate);
		}
	}
	
	/**
	 * 查看session验证码是否超时
	 * @param request
	 * @param min 自定义超时时间（分钟）
	 * @return
	 */
	public boolean isOutTime(HttpServletRequest request, int min){
		if(useSession){
			//验证码不存在
			HttpSession session = request.getSession();
			Object code = session.getAttribute(VerifyConfig.staticSessionKey);
			if(StringUtils.isEmpty(code)){
				remove(request);
				return true;
			}
			//验证码不存在
			Object date = session.getAttribute(VerifyConfig.staticSessionDate);
			if(StringUtils.isEmpty(date)){
				remove(request);
				return true;
			}
			
			//验证码超过
			long create = Long.valueOf(date.toString());
			int time = (min <= 0 ? 5 : min) * 60 * 1000;
			if(System.currentTimeMillis() - create > time){
				return true;
			}
		}
		return false;
	}
}
