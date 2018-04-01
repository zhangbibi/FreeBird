package com.freebird.filter;

import com.freebird.wx.common.http.WeixinHttpURLSet;
import com.freebird.wx.common.wx.WeixinUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 配置需要进行网页授权的url 从目录 /项目名/xxx/xxx.html 开始配置
 *
 * @author zhangyaping001
 */
public class WebAuthFilterConfig {

    private static final Logger logger = LoggerFactory.getLogger(WebAuthFilterConfig.class);

    List<String> uriList = new ArrayList<String>();

    public void init() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    this.getClass().getResource("/AuthURLs.txt").getPath())));
            String uri = br.readLine();
            while (StringUtils.isNotEmpty(uri)) {
                uriList.add(uri.trim());
                uri = br.readLine();
            }
        } catch (Exception e) {
            logger.error("parse AuthURLs.txt exception", e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                logger.error("close BufferedReader exception", e);
            }
        }
    }

    public boolean check(String uri) {
        if (CollectionUtils.isEmpty(uriList)) {
            logger.info("uriList is empty.");
            return false;
        }

        for (String temp : uriList) {
            if (temp.equals(uri)) {
                return true;
            }
        }
        return false;
    }

    private static boolean inArray(String key, String[] keys) {
        if (keys == null || keys.length <= 0) {
            return false;
        }

        for (String temp : keys) {
            if (temp.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public String createWxAuthUrl(String url, WeixinUtil weixinUtil) {
        return WeixinHttpURLSet.WX_AUTH_URL.replace("APPID", weixinUtil.getAppid()).replace("REDIRECT_URI", URLEncoder.encode(url));
    }

}
