package com.freebird.wx.common.http;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangyaping on 2017/4/21.
 */
public class Test {
    public static void main(String[] args) {


//        BusinessResponse<Object> response = NetUtil.getInstance().get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx0b903766a5ba1197&secret=42d2e5aff500b2aa79cce6022703b3ae",
//                MediaType.APPLICATION_JSON);
//
//        Map<String,String> data = new Gson().fromJson(response.getData().toString(), new TypeToken<Map<String, String>>() {
//        }.getType());
//
//
//        System.out.println(data);

//        Map<String,Object> m= null;
//        String url = WeixinHttpURLSet.GET_USERINFO_BY_AUTHTOKEN.replace("ACCESS_TOKEN", "vC93WgEA72upHCcwOeOIt-9e5qug_Uqs40ps2wqLJb8Cm8PaFFE3JIr1ykbk72_t-G8sILIY-AXyDnRt0YnfzB2tSD1ai18FMhW_dfX__Ic").replace("OPENID", "xxxxxxxxxxxxxx");
//
//        System.out.println(url);
//
//        BusinessResponse<Object> response = NetUtil.getInstance().get(url, MediaType.APPLICATION_JSON);
//
//        System.out.println(response);
//        if (response.getRt_code()==BusinessResponse.RESPONSE_OK) {
//            Object data = response.getData();
//            String dataStr = response.getData().toString();
//            m = new Gson().fromJson(dataStr, new TypeToken<Map<String, Object>>() {}.getType());
//            ArrayList list = (ArrayList) m.get("privilege");
//            System.out.println(list);
//        }
//
//        System.out.println(m);


        Date now = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        System.out.println(sf.format(now));
        //设置订单支付过期时间
        Date expireDate = new Date();
        expireDate.setTime(now.getTime() + 10 * 60000);
        System.out.println(sf.format(expireDate));

    }
}
