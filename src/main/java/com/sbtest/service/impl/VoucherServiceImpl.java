package com.sbtest.service.impl;

import com.sbtest.dto.Voucher;
import com.sbtest.mapper.VoucherMapper;
import com.sbtest.service.VoucherServcie;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VoucherServiceImpl implements VoucherServcie{

    @Resource
    VoucherMapper mapper;
    @Override
    public Voucher getVoucher(String userId) {
        return mapper.getVoucher(userId);
    }
}