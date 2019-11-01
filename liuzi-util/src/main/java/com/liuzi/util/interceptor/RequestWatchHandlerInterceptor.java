package com.liuzi.util.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Setter;

import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.liuzi.util.common.Log;
import com.liuzi.util.http.IPUtil;

/**
 * 监听请求信息及执行时间
 * @author zsy
 */
//public class RequestWatchHandlerInterceptor extends HandlerInterceptorAdapter{
public class RequestWatchHandlerInterceptor implements HandlerInterceptor{
	
	private static final NamedThreadLocal<Long> STTL = new NamedThreadLocal<Long>("StopWatch-StartTime");
	
	@Setter
	private int slowTime = 500;
	@Setter
	private boolean debug = false;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
		throws Exception {
		if(!debug){
			return true;
		}
		
		//开始时间
		STTL.set(System.currentTimeMillis());
		//继续流程  
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,   
			Object handler, Exception ex) throws Exception { 
		if(!debug){
			return;
		}
		
		if(!(handler instanceof HandlerMethod)){
			return;
		}
		//消耗时间	
		long consumeTime = consumeTime();
		//打印
		logRequestInfo(request, handler, consumeTime);
	}
	
	private long consumeTime(){
		//结束时间
		long endTime = System.currentTimeMillis();
		//获取开始时间
		long beginTime = STTL.get();
		STTL.remove();
		//计算消耗时间
		return endTime - beginTime;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	private void logRequestInfo(HttpServletRequest request, Object handler, long consumeTime){
		//获取handler
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		//请求地址
		String url = request.getRequestURL().toString();
		//请求方式
		String httpMethod = request.getMethod();
		//访问ip地址
		String ip = IPUtil.getIP(request);
		//请求对应方法
		Method method = handlerMethod.getMethod();
		String returnType = method.getReturnType().getSimpleName();
		Class<?> declaringClass = method.getDeclaringClass();
		String className = declaringClass.getName();
		String methodName = method.getName();
		//参数
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
		//拼接参数及参数值
		StringBuilder paramsBuilder = null;
		//参数个数
		int length = methodParameters.length;
		if(length > 0){
			paramsBuilder = new StringBuilder("");
			paramsBuilder.append("[").append(length).append("] - ");
			//遍历
			for(MethodParameter mp : methodParameters){
				//参数名称
				String parameterName = mp.getParameterName();
				//参数值
				Object[] values = getMethodParameterValues(request, parameterName);
				
				paramsBuilder.append(parameterName);
				paramsBuilder.append("[").append(mp.getParameterType().getSimpleName()).append("]: ");
				paramsBuilder.append(Arrays.toString(values)).append(", ");
			}
			paramsBuilder.deleteCharAt(paramsBuilder.length() - 2);
		}
		
		//处理时间超过500毫秒的请求定义为慢请求  
		if(consumeTime >= slowTime){
			Log.warn("【REQUEST_URL_ ====>>>> 】：[{}] {}", httpMethod, url);
			Log.warn("【REQ_VISIT_IP ====>>>> 】：{}", ip);
			Log.warn("【CLASS_METHOD ====>>>> 】：{} {}.{}", returnType, className, methodName);
			Log.warn("【METHOD_ARGS_ ====>>>> 】：{}", paramsBuilder == null ? "" : paramsBuilder.toString());
			Log.warn("【TAKE_UP_TIME ====>>>> 】：{}ms", consumeTime);
			return;
		}
		
		Log.info("【REQUEST_URL_ ====>>>> 】：[{}] {}", httpMethod, url);
		Log.info("【REQ_VISIT_IP ====>>>> 】：{}", ip);
		Log.info("【CLASS_METHOD ====>>>> 】：{} {}.{}", returnType, className, methodName);
		Log.info("【METHOD_ARGS_ ====>>>> 】：{}", paramsBuilder == null ? "" : paramsBuilder.toString());
		Log.info("【TAKE_UP_TIME ====>>>> 】：{}ms", consumeTime);
	}
	
	private Object[] getMethodParameterValues(HttpServletRequest request, String name){
		return request.getParameterValues(name);
    }
	
} 
