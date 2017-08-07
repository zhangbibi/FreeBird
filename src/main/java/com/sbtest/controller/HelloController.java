package com.sbtest.controller;

import com.sbtest.dto.Voucher;
import com.sbtest.service.VoucherServcie;
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

    @Value("${com.sbtest.controller.HelloController.name}")
    private String name;

    @Value("${com.sbtest.controller.HelloController.age}")
    private int age;

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
}
