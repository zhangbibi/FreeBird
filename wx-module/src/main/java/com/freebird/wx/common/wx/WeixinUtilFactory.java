package com.freebird.wx.common.wx;


import org.springframework.beans.factory.annotation.Value;

/**
 * 获取微信接口调用工具类的工厂方法
 * 进行本地调试返回mock()
 *
 * @author zhangyaping001
 */
public class WeixinUtilFactory {

    @Value("${wx.appId}")
    private static String wxAppId;

    @Value("${wx.appsecret}")
    private static String wxAppsecret;

    private static WeixinUtil utilInstance = new WeixinUtilImpl(wxAppId, wxAppsecret);

    public static WeixinUtil getInstance() {
        return utilInstance;
    }

}
