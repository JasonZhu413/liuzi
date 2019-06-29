package com.liuzi.druid.boot;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.liuzi.util.common.LiuziUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DruidConfig {
	
	@Value("${spring.datasource.type}")
    private String type;
	@Value("${spring.datasource.driver}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    
	@Value("${spring.datasource.initialSize}")
    private int initialSize;
	@Value("${spring.datasource.minIdle}")
    private int minIdle;
	@Value("${spring.datasource.maxActive}")
    private int maxActive;
	@Value("${spring.datasource.maxWait}")
    private int maxWait;
	@Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
	@Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
	@Value("${spring.datasource.validationQuery}")
    private String validationQuery;
	@Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
	@Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
	@Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;
	@Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
	@Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
	@Value("${spring.datasource.filters}")
    private String filters;
	@Value("${spring.datasource.logSlowSql}")
    private String logSlowSql;
	@Value("${spring.datasource.loginUsername}")
    private String loginUsername;
    @Value("${spring.datasource.loginPassword}")
    private String loginPassword;
	
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
    	LiuziUtil.tag("--------  Liuzi Druid初始化  --------");
    	
    	log.info("--------  Liuzi Druid初始化，注入servletRegistrationBean  --------");
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", loginUsername);
        reg.addInitParameter("loginPassword", loginPassword);
        reg.addInitParameter("logSlowSql", logSlowSql);
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
    	log.info("--------  Liuzi Druid初始化，注入filterRegistrationBean  --------");
    	
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public DataSource dataSource() {
    	log.info("--------  Liuzi Druid初始化，注入dataSource  --------");
    	
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driver);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        //datasource.setDbType(type);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("druid configuration initialization filter", e);
        }
        
        log.info("--------  Liuzi Druid初始化完成 --------");
        
        return datasource;
    }
}
