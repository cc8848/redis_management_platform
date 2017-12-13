/**
 * Project Name:dao
 * File Name:DataSourceConfig.java
 * Package Name:cn.fzz.dao.config
 * Date:2017年12月10日下午2:47:41
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package cn.fzz.dao.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * ClassName:DataSourceConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年12月10日 下午2:47:41 <br/>
 * @author   zhen
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Configuration
@PropertySource(value="classpath:jdbc.properties",ignoreResourceNotFound=true)
@ComponentScan(basePackages="cn.fzz")
public class DataSourceConfig
{
    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driver}")
    private String driver;

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public DataSource dataSource(){
        
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(5);
        return dataSource;
    }
}

