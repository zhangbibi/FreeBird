package com.freebird.common.util;

/**
 * Created by zhangyaping on 16-7-15.
 */
public class TgResponse {

    private static final String CODE_SUCCESS = "000000";
    private static final String CODE_FAILED = "111111";
    private static final String CODE_ARG_ILLEGAL = "000001";

    private String result;
    private String msg;
    private Object data;

    public TgResponse(String result, String msg, Object data) {
        this.result = result;
        this.msg = msg;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TgResponse{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static TgResponse failed(Exception e) {
        return new TgResponse(CODE_FAILED, e.getMessage(), null);
    }

    public static TgResponse success() {
        return success(null);
    }

    public static TgResponse failed(String msg) {
        return new TgResponse(CODE_FAILED, msg, null);
    }

    public static TgResponse failed() {
        return new TgResponse(CODE_FAILED, "Unknown error!", null);
    }

    public static TgResponse success(Object data) {
        return new TgResponse(CODE_SUCCESS, "Request success!", data);
    }

    public static TgResponse failed(String result, String msg) {
        return new TgResponse(result, msg, null);
    }

    public static TgResponse argIllegal(String msg) {
        return new TgResponse(CODE_ARG_ILLEGAL, msg, null);
    }
}

