/**
 * Project Name:dao
 * File Name:MapperScanConfig.java
 * Package Name:cn.fzz.dao.config
 * Date:2017年12月10日下午3:26:44
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package cn.fzz.dao.config;

import org.springframework.context.annotation.Configuration;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * ClassName:MapperScanConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年12月10日 下午3:26:44 <br/>
 * @author   zhen
 * @version  
 * @since    JDK 1.8
 * @see 	 
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)
public class MapperScanConfig
{
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("cn.fzz.dao.mapper");
        return mapperScannerConfigurer;
    }

}

