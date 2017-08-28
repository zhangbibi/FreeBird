package com.sbtest.dto;

public class Voucher {

    private String vouName;
    private String vouId;
    private String userId;

    public Voucher(){

    }

    public Voucher(String vouName, String vouId, String userId) {
        this.vouName = vouName;
        this.vouId = vouId;
        this.userId = userId;
    }

    public String getVouName() {
        return vouName;
    }

    public void setVouName(String vouName) {
        this.vouName = vouName;
    }

    public String getVouId() {
        return vouId;
    }

    public void setVouId(String vouId) {
        this.vouId = vouId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}