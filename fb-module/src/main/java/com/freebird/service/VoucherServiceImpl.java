package com.freebird.service;

import com.freebird.dto.User;
import com.freebird.mapper.VoucherMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class VoucherServiceImpl implements VoucherServcie {

    private static final Logger logger = LoggerFactory.getLogger(VoucherServiceImpl.class);
    @Resource
    VoucherMapper mapper;


    @Override
    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public User getVoucher(String userId) {
        return mapper.getVoucher(userId);
    }

    public User getDefaultUser(String userId) {
        User user = new User();
        user.setId(9L);
        user.setUsername("TEst");
        user.setPassword("pass");
        return user;
    }


    @Override
    public void testLog() {
        logger.info("log info...........");
        logger.warn("log warn...........");
        logger.error("log error...........");
        logger.debug("log debug...........");
    }
}
