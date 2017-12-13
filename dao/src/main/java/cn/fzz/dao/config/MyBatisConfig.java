package cn.fzz.dao.config;
/**
 * Project Name:dao
 * File Name:MyBatisConfig.java
 * Package Name:cn.fzz.dao.config
 * Date:2017年12月10日下午3:24:49
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * ClassName:MyBatisConfig <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017年12月10日 下午3:24:49 <br/>
 * Desc：    将spring和mybatis整合
 * @version  
 * @since    JDK 1.8
 * @see
 */
@Configuration
public class MyBatisConfig
{
    @Bean
    @ConditionalOnMissingBean //当容器里没有指定的Bean的情况下创建该对象
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 设置mybatis的主配置文件
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource mybatisConfigXml = resolver.getResource("classpath:mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);
        // 设置别名包
       // sqlSessionFactoryBean.setTypeAliasesPackage("com.taotao.cart.pojo");
        return sqlSessionFactoryBean;
    }

}

