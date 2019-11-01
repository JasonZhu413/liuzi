package com.liuzi.mybatis.proxy;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/*@Configuration
@EnableTransactionManagement(proxyTargetClass = true)*/
public class MsDataSourceConfiguration{
	
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(@Qualifier("msDataSource") MsDataSource msDataSource) 
    		throws SQLException, IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(msDataSource);
        Properties properties = new Properties();
        properties.setProperty("sqlType", "mysql");
        sqlSessionFactoryBean.setConfigurationProperties(properties);
        //分页插件
        //sqlSessionFactoryBean.setPlugins(new Interceptor[]{new PaginationInterception()});
        return sqlSessionFactoryBean;
    }
 
    @Bean
    public MsDataSourceTransactionManager transactionManager(
    		@Qualifier("msDataSource") MsDataSource msDataSource) {
        MsDataSourceTransactionManager dynamicDataSourceTransactionManager = 
        		new MsDataSourceTransactionManager();
        dynamicDataSourceTransactionManager.setDataSource(msDataSource);
        return dynamicDataSourceTransactionManager;
    }

	
}
