/**
 * Project Name:dao
 * File Name:DaoConfig.java
 * Package Name:cn.fzz.dao.config
 * Date:2017年12月10日下午3:00:44
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package cn.fzz.dao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.fzz.dao.AdminDao;

/**
 * ClassName:DaoConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年12月10日 下午3:00:44 <br/>
 * @author   zhen
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Configuration
@ComponentScan(basePackages="cn.fzz.dao")
public class AdminDaoConfig
{
    @Bean
    public AdminDao getAdminDao(){
        return new AdminDao();
    }
}

