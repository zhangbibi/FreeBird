package com.freebird.common.http;

/**
 * business响应类
 * @param <T>
 */
public class BusinessResponse<T> {
    public static final int RESPONSE_OK         = 200;
    public static final int RESPONSE_ERROR      = 500000;

    private int             rt_code             = 500000;
    private String          rt_msg              = null;
    private T               data                = null;

    public BusinessResponse() {
        this.rt_code = RESPONSE_OK;
        this.rt_msg = "";
    }

    public static BusinessResponse ok(Object data) {
        BusinessResponse<Object> response = new BusinessResponse();
        response.setData(data);
        response.setRt_code(RESPONSE_OK);
        response.setRt_msg("success");
        return response;
    }

    public static BusinessResponse fail(int code, String msg) {
        BusinessResponse response = new BusinessResponse();
        response.setRt_code(code);
        response.setRt_msg(msg);
        return response;
    }

    public int getRt_code() {
        return rt_code;
    }

    public BusinessResponse setRt_code(int rt_code) {
        this.rt_code = rt_code;
        return this;
    }

    public String getRt_msg() {
        return rt_msg;
    }

    public BusinessResponse setRt_msg(String rt_msg) {
        this.rt_msg = rt_msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public BusinessResponse setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "BusinessResponse [rt_code=" + rt_code + ", rt_msg=" + rt_msg + ", data=" + data + "]";
    }

}
