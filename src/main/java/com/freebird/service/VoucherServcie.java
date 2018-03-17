package com.freebird.service;

import com.freebird.dto.User;

public interface VoucherServcie {

    User getVoucher(String userId);

    void testLog();
}
