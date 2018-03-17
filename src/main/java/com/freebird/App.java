package com.freebird;

import com.freebird.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;


@ImportResource(locations = {"classpath:spring.xml"})
@SpringBootApplication
@MapperScan("com.freebird.mapper")
public class App {
    public static void main(String[] args) {


        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);

    }

}
