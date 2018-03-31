package com.freebird.wx.common.wxpay;

import com.freebird.wx.common.util.RandomNum.RandomUtil;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by zhangyaping on 2017/4/26.
 */
public class QueryCloseOrderParams {


    private String nonce_str;

    private String out_trade_no;

    @Value("${wx.appId}")
    private String appid;

    @Value("${wx.mch_id}")
    private String mch_id;

    public QueryCloseOrderParams() {

//        this.appid="wx0b903766a5ba1197";
//        this.mch_id="1392148902";
        this.nonce_str = RandomUtil.gen32UUID();
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @Override
    public String toString() {
        return "QueryOrderParams{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                '}';
    }
}
