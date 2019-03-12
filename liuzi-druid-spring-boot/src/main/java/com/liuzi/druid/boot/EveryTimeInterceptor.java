package com.liuzi.druid.boot;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 监听请求响应时间
 * @author zsy
 */
@Slf4j
@Configuration
public class EveryTimeInterceptor implements HandlerInterceptor {

    private final ThreadLocal<StopWatch> stopWatchLocal = new ThreadLocal<StopWatch>();

    @Value("${control.run.min.time}")
    private int logMin;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
    		Object handler) throws Exception {
        StopWatch stopWatch = new StopWatch(handler.toString());
        stopWatchLocal.set(stopWatch);
        stopWatch.start(handler.toString());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        StopWatch stopWatch = stopWatchLocal.get();
        stopWatch.stop();
        String currentPath = request.getRequestURI();
        String queryString = request.getQueryString();
        queryString = queryString == null ? "" : "?" + queryString;
        if (stopWatch.getTotalTimeMillis() > logMin) {
            log.warn("Access URL [" + currentPath + queryString + "] " + 
            		stopWatch.getTotalTimeMillis() + " ms");
        }
        stopWatchLocal.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
    		Object handler, Exception ex) throws Exception {
    }

}
