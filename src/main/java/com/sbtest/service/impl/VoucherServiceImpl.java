package com.sbtest.service.impl;

import com.sbtest.dto.Voucher;
import com.sbtest.service.VoucherServcie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl implements VoucherServcie{

    private static final Logger logger = LoggerFactory.getLogger(VoucherServiceImpl.class);
    @Resource
    VoucherMapper mapper;
    @Override
    public Voucher getVoucher(String userId) {
        return mapper.getVoucher(userId);
    }

    @Override
    public void testLog() {
        logger.info("log info...........");
        logger.warn("log warn...........");
        logger.error("log error...........");
        logger.debug("log debug...........");
    }
}