package com.liuzi.druid.boot;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.druid.util.StringUtils;


@Slf4j
@EnableWebMvc
@Configuration
public class InterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	/*@Value("${time.path.add.patterns}")
    private String tpap;
	@Value("${time.path.exclude.patterns}")
    private String tpep;
	
	@Value("${resource.path.handler}")
    private String resourcePathHandler;
	@Value("${resource.path.locations}")
    private String resourcePathLocations;
	
	@Value("${page.error.common}")
	private String common;
	@Value("${page.error.unauthorized}")
	private String unauthorized;
	@Value("${page.error.not_found}")
    private String not_found;
	@Value("${page.error.internal_server_error}")
    private String internal_server_error;*/
	
	
	@Bean
	public EveryTimeInterceptor everyTimeInterceptor() {
		return new EveryTimeInterceptor();
	}
	
	/*@Bean
	public InterceptorRegistration interceptorRegistration(InterceptorRegistry registry) {
		InterceptorRegistration interceptorRegistration = registry.addInterceptor(everyTimeInterceptor());
		if(!StringUtils.isEmpty(tpap)){
			interceptorRegistration.addPathPatterns(tpap);
    	}else{
    		interceptorRegistration.addPathPatterns("/**");
    	}
    	if(!StringUtils.isEmpty(tpep)){
    		interceptorRegistration.excludePathPatterns(tpep);
    	}
		
		log.info("------ interceptorRegistration add：" + tpap + ", exclude：" + tpep + " --------");
		
		return interceptorRegistration;
	}
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	interceptorRegistration(registry);
    }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if(!StringUtils.isEmpty(resourcePathHandler) && !StringUtils.isEmpty(resourcePathLocations)){
			String[] rphs = resourcePathHandler.split(",");
			String[] rpls = resourcePathLocations.split(",");
			
			if(rphs.length == rpls.length){
				for(int i = 0, len = rphs.length; i < len; i ++){
					registry.addResourceHandler(rphs[i])
						.addResourceLocations(rpls[i]);
				}
			}
		}
		
		log.info("------ addResourceHandlers resourcePathHandler：" + resourcePathHandler + 
				", resourcePathLocations：" + resourcePathLocations + " --------");
		
		super.addResourceHandlers(registry);
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		log.info("------ containerCustomizer init error --------");
		
		return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if(!StringUtils.isEmpty(common)){
					unauthorized = common;
					not_found = common;
					internal_server_error = common;
				}
				ErrorPage error401 = new ErrorPage(HttpStatus.UNAUTHORIZED, unauthorized);
				ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, not_found);
				ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, internal_server_error);
				container.addErrorPages(error401, error404, error500);
	      }
	   };
	}*/
}
