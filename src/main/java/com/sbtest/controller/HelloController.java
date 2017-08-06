package com.sbtest.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangyaping on 17/8/6.
 */

@RestController
@EnableAutoConfiguration
public class HelloController {

    @Value("${com.sbtest.controller.HelloController.name}")
    private String name;

    @Value("${com.sbtest.controller.HelloController.age}")
    private int age;

    @RequestMapping("/hello")
    @ResponseBody
    public String index() {
        return "Hello " + name + " " + age;

    }
}
