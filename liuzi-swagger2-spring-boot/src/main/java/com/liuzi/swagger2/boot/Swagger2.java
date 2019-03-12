package com.liuzi.swagger2.boot;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.liuzi.util.LiuziUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2
 * http://ip:port/swagger-ui.html
 * @author zsy
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger2 extends WebMvcConfigurerAdapter{
	
	@Value("${swg.basePackage}")
	private String basePackage;
	@Value("${swg.title}")
	private String title;
	@Value("${swg.description}")
	private String description;
	@Value("${swg.termsOfServiceUrl}")
	private String termsOfServiceUrl;
	@Value("${swg.version}")
	private String version;
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("  --------  Liuzi Swagger2初始化，Override addResourceHandlers --------");
        registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
        log.info("  --------  Liuzi Swagger2初始化完成 --------");
    }
	
    @Bean
    public Docket docket() {
    	LiuziUtil.tag("  --------  Liuzi Swagger2初始化  --------");
		log.info("  --------  Liuzi Swagger2初始化，注入docket --------");
		
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
    	log.info("  --------  Liuzi Swagger2初始化，new ApiInfoBuilder --------");
    	
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsOfServiceUrl)
                .version(version)
                .build();
    }

}
