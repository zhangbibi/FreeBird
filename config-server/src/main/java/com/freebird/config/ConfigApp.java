package com.freebird.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigServer
public class ConfigApp {
    public static void main(String[] args) {

        SpringApplication.run(ConfigApp.class, args);

    }

}
