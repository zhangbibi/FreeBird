package com.freebird.common.wx;


import com.threeguys.common.util.LogUtils;
import org.slf4j.Logger;

/**
 * Created by zhangyaping on 16-7-19.
 */
public class WeixinUrlFactory {

    private static Logger logger = LogUtils.getLogger();

    private WeixinUtil weixinUtil = WeixinUtilFactory.getInstance();

    private static WeixinUrlFactory instance = new WeixinUrlFactory();

    private WeixinUrlFactory() {}

    public static WeixinUrlFactory getInstance() {
        return instance;
    }

    public String mediaDownloadUrl(String media_id) {
//        return "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=afKtTRvN-T7IgY8UGXnRJzLyYN5wsMmd-SapSZZLe0_qP6ZU2x1XoF9N82yEoRJawTaUzm4PrMQHXgDSu5ObFW2yWiTRlvCl4o2Ny3FQtzloYAlf9sMqdaNzniPM1vZYEVHhAAAJAM&media_id=" + media_id;
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=" + weixinUtil.getAccessToken()
                + "&media_id=" + media_id;
        logger.info("多媒体下载url: " + url);
        return url;
    }
}
