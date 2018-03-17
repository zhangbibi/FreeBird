package com.freebird.service;

import com.freebird.dto.Voucher;

public interface VoucherServcie {

    Voucher getVoucher(String userId);

    void testLog();
}
