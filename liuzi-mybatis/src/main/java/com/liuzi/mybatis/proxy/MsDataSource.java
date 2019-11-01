package com.liuzi.mybatis.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.liuzi.util.common.Log;


/**
 * 
 * https://blog.csdn.net/weixin_34137799/article/details/92601609
 * 
 * 	<!-- 数据源注入到动态数据源 -->
    <bean id="msDataSource" class="com.liuzi.mybatis.proxy.MsDataSource">
        <property name="targetDataSources">
            <map>
                <entry key="master" value-ref="masterDataSource"/>
                <entry key="slave1" value-ref="slaveDataSource1"/>
                <entry key="slave2" value-ref="slaveDataSource2"/>
            </map>
        </property>
        <property name="slaveDataSources">
            <list>
                <value>slave1</value>
                <value>slave2</value>
            </list>
        </property>
        <!-- 设置默认数据源 -->
        <property name="defaultTargetDataSource" ref="masterDataSource"/>
    </bean>
    
    

@Configuration
public class DataSourceConfiguration {
 
    @Bean(name = "masterDataSource", destroyMethod = "close", initMethod = "init")
    [@Primary](https://my.oschina.net/primary) // 这个注解表示主数据源
    @ConfigurationProperties(prefix = "druid.datasource")
    public DataSource datasource() {
        return new DruidDataSource();
    }
 
    @Bean(name = "slaveDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "druid.datasource2")
    public DataSource datasource2() {
        return new DruidDataSource();
    }
 
    
    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                               @Qualifier("slaveDataSource") DataSource slaveDataSource) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
 
        targetDataSources.put("master", masterDataSource);
        targetDataSources.put("slave1", slaveDataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
 
        List<Object> slaveDataSources = new ArrayList<Object>();
        slaveDataSources.add("slave1");
 
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource);
        dynamicDataSource.setSlaveDataSources(slaveDataSources);
 
        return dynamicDataSource;
 
    }
 
}
 *
 */
public class MsDataSource extends AbstractRoutingDataSource {
	
	/**
     * 轮询计数
     */
    private AtomicInteger squence = new AtomicInteger(0);
    
    /**
     * 从数据源
     */
    private List<Object> slaveDataSources = new ArrayList<Object>();
 
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
    }
 
    @Override
    protected Object determineCurrentLookupKey() {
        Object key = "";
        //主库
        if (MsDataSourceHolder.isMaster()) {
            key = MsDataSourceHolder.MASTER;
        } else {
            //从库
            key = getSlaveKey();
        }
        Log.debug("==> select datasource key [{}]", key);
        return key;
    }
 
    public void setSlaveDataSources(List<Object> slaveDataSources) {
        this.slaveDataSources = slaveDataSources;
    }
 
    /**
     * 轮询获取从库
     * @return
     */
    public Object getSlaveKey() {
        if (squence.intValue() == Integer.MAX_VALUE) {
            synchronized (squence) {
                if (squence.intValue() == Integer.MAX_VALUE) {
                    squence = new AtomicInteger(0);
                }
            }
        }
        return slaveDataSources.get(squence.getAndIncrement() % slaveDataSources.size());
    }
	
}
