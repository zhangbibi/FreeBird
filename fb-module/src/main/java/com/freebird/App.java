package com.freebird;

import com.freebird.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;

@ImportResource(locations = {"classpath:spring.xml"})
@SpringBootApplication
@EnableCircuitBreaker
@MapperScan("com.freebird.mapper")
public class App {
    public static void main(String[] args) {


        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);

    }

}
