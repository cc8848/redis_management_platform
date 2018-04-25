package cn.fzz.startup;

import cn.fzz.framework.common.Common;
import cn.fzz.framework.redis.RedisCommon;
import cn.fzz.framework.redis.RedisConnection;
import cn.fzz.thread.Monitor;
import cn.fzz.framework.redis.Subscriber;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by fanzezhen on 2017/12/13.
 * Desc:
 * ClassName: StartupApplication <br/>
 * Function: springboot启动类，自动加载启动配置,注册服务. <br/>
 * @SpringBootApplication 服务启动注解<br/>
 * @ComponentScan 注解会自动扫描子工程下的Bean,包括引入的jar包
 *
 * @since JDK 1.8
 */
@ComponentScan(basePackages = {"cn.fzz"})
@SpringBootApplication
public class StartupApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(StartupApplication.class, args);

        new Monitor().start();

        Common.startSubscriber();
//        // 启动的时候要注意，由于我们在controller中注入了RestTemplate，所以启动的时候需要实例化该类的一个实例
//        @Autowired
//        private RestTemplateBuilder builder;
//
//        // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
//        @Bean
//        public RestTemplate restTemplate() {
//            return builder.build();
//        }
    }
}
