package com.sbtest.mapper;

import com.sbtest.dto.Voucher;
import org.mapstruct.Mapper;

/**
 * Created by zhangyaping on 17/8/7.
 */
@Mapper
public interface VoucherMapper {
    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    Voucher getVoucher(String userId);
}
