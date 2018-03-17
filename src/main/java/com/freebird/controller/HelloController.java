package com.freebird.controller;

import com.freebird.dto.Voucher;
import com.freebird.service.VoucherServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by zhangyaping on 17/8/6.
 */

@RestController
@EnableAutoConfiguration
public class HelloController {

    @Value("${com.freebird.controller.HelloController.name}")
    private String name;

    @Value("${com.freebird.controller.HelloController.age}")
    private int age;

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Resource
    VoucherServcie voucherServcie;

    @RequestMapping("/hello")
    @ResponseBody
    public String index() {

        return "Hello " + name + " " + age;
    }

    @RequestMapping(value = "/voucher/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public Voucher getVoucher(@PathVariable String userId) {
        System.out.println("getVoucher : " + userId);
        Voucher voucher = voucherServcie.getVoucher(userId);
        return voucher;
    }

    @RequestMapping("log")
    @ResponseBody
    public String testLog(){
        logger.info("log info...........");
        logger.warn("log warn...........");
        logger.error("log error...........");
        logger.debug("log debug...........");

        voucherServcie.testLog();
        return "OK";
    }
}
