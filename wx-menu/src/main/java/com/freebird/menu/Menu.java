package com.freebird.menu;

import java.net.URLEncoder;

/**
 * Created by zhangyaping on 18/4/2.
 */
public class Menu {

    public static String WX_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";

    private static String appid = "wxc66a20f7673924bb";

    public static void main(String[] args) {

        String url = "http://127.0.0.1:8888/fb/html/wxuserinfo.html";

        System.out.println(createWxAuthUrl(url));
    }


    public static String createWxAuthUrl(String url) {
        return WX_AUTH_URL.replace("APPID", appid).replace("REDIRECT_URI", URLEncoder.encode(url));
    }


    /**
     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc66a20f7673924bb&redirect_uri=http%3A%2F%2F127.0.0.1%3A8888%2Ffb%2Fhtml%2Fwxuserinfo.html&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect
     */

}
