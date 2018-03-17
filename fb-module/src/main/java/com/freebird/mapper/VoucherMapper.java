package com.freebird.mapper;

import com.freebird.dto.User;

/**
 * Created by zhangyaping on 17/8/7.
 */
public interface VoucherMapper {
    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    User getVoucher(String userId);
}
