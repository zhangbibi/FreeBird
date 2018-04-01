package com.freebird.wx.common.wx;

import com.freebird.wx.common.http.BusinessResponse;
import com.freebird.wx.common.http.NetUtil;
import com.freebird.wx.common.http.WeixinHttpURLSet;
import com.freebird.wx.common.util.RedisClientUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * 微信接口调用工具类实现
 *
 * @author zhangyaping001
 */
public class WeixinUtilImpl implements WeixinUtil {

    private static final Logger logger = LoggerFactory.getLogger(WeixinUtilImpl.class);
    private String appId;
    private String secret;

    private Gson gson = new Gson();

    public String getAppid() {
        return this.appId;
    }

    public String getSecret() {
        return this.secret;
    }

    public WeixinUtilImpl(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }

    /**
     * 根据code获取openId
     *
     * @param code
     * @return
     */
    public WebAuthAccessToken getWebAccessTokenByCode(String code) {
        String requestURL = WeixinHttpURLSet.GET_AUTHORIZE_ACCESSTOKEN
                .replace("APPID", this.appId).replace("SECRET", this.secret)
                .replace("CODE", code);
        try {
            WebAuthAccessToken webAuthAccessToken = new WebAuthAccessToken();
            BusinessResponse<Object> response = NetUtil.getInstance().get(requestURL, MediaType.APPLICATION_JSON);
            if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
                Map<String, String> data = gson.fromJson(response.getData().toString(), new TypeToken<Map<String, String>>() {
                }.getType());
                String openid = data.get("openid");
                String access_token = data.get("access_token");
                Long expires_in = Long.parseLong(data.get("expires_in"));
                String refresh_token = data.get("refresh_token");
                String scope = data.get("scope");
                webAuthAccessToken.setAccess_token(access_token);
                webAuthAccessToken.setExpires_in(expires_in);
                webAuthAccessToken.setOpenid(openid);
                webAuthAccessToken.setRefresh_token(refresh_token);
                webAuthAccessToken.setScope(scope);

                setWebAuthAccessTokenToCache(openid, webAuthAccessToken.getAccess_token(), webAuthAccessToken.getRefresh_token());
            }

            return webAuthAccessToken;
        } catch (Exception e) {
            logger.error("get openId exception.url: " + requestURL, e);
            return null;
        }
    }

    /**
     * 获取接口调用凭证
     *
     * @return
     */
    public String getAccessToken() {
        AccessToken token = null;
        String idByte = this.getTokenFromRedis();
        if (StringUtils.isNotEmpty(idByte)) {
            token = gson.fromJson(idByte, AccessToken.class);
            if (token.needRefresh()) {
                logger.info("WeixinUtilImpl.getAccessToken:access_token is invalid need refresh.");
                refreshToken();
                idByte = this.getTokenFromRedis();
                token = gson.fromJson(idByte, AccessToken.class);
            }
        } else {
            this.initToken();
            idByte = this.getTokenFromRedis();
            token = gson.fromJson(idByte, AccessToken.class);
        }
        logger.info("WeixinUtilImpl.getAccessToken :　" + token.getAccess_token());
        return token.getAccess_token();
    }

    /**
     * 初始化token
     */
    private synchronized void initToken() {
        String idByte = this.getTokenFromRedis();
        //再次判断防止并发问题
        if (StringUtils.isEmpty(idByte)) {
            AccessToken token = new AccessToken();
            try {
                long now = System.currentTimeMillis();
                String getAccessTokenURL = WeixinHttpURLSet.GET_ACCESSTOKEN.replace("APPID",
                        this.appId).replace("SECRET", this.secret);
                //JSONObject result = HttpRequestUtil.httpsGETRequest(getAccessTokenURL);
                BusinessResponse<Object> response = NetUtil.getInstance().get(getAccessTokenURL, MediaType.APPLICATION_JSON);
                if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
                    Map<String, String> data = gson.fromJson(response.getData().toString(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    // 请求时间设置放在请求之后，防止异常发生时，时间已设
                    token.setRequestTime(now);
                    token.setAccess_token(data.get("access_token"));
                    token.setExpires_in(Long.parseLong(data.get("expires_in")));
                    this.setTokenToRedis(token);
                    logger.info("WeixinUtilImpl.initToken: init done, token is: " + token);
                }
            } catch (Exception e) {
                logger.info("WeixinUtilImpl.initToken exception={}", e);
                e.printStackTrace();
                this.setTokenToRedis(token);
            }
            //刷新token时同步初始化jsapi_ticket
            refreshTicket(token.getAccess_token());
        }
    }

    /**
     * 刷新token
     */
    private synchronized void refreshToken() {
        String idByte = this.getTokenFromRedis();
        AccessToken token = gson.fromJson(idByte, AccessToken.class);
        //再次判断防止并发问题
        if (token.needRefresh()) {
            try {
                long now = System.currentTimeMillis();
                String getAccessTokenURL = WeixinHttpURLSet.GET_ACCESSTOKEN.replace("APPID",
                        this.appId).replace("SECRET", this.secret);
//				JSONObject result = HttpRequestUtil.httpsGETRequest(getAccessTokenURL);
                BusinessResponse<Object> response = NetUtil.getInstance().get(getAccessTokenURL, MediaType.APPLICATION_JSON);
                if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
                    Map<String, String> data = gson.fromJson(response.getData().toString(), new TypeToken<Map<String, String>>() {
                    }.getType());
                    // 请求时间设置放在请求之后，防止异常发生时，时间已设
                    token.setRequestTime(now);
                    token.setAccess_token(data.get("access_token"));
                    token.setExpires_in(Long.parseLong(data.get("expires_in")));
                    this.setTokenToRedis(token);
                    logger.info("WeixinUtilImpl.refreshToken The refresh token is: " + token);
                }
            } catch (Exception e) {
                logger.info("WeixinUtilImpl.refreshToken exception={}", e);
                e.printStackTrace();
                this.setTokenToRedis(token);
            }
            //刷新token时同步刷新jsapi_ticket
            refreshTicket(token.getAccess_token());
        }
    }


    private String getTokenFromRedis() {
        return RedisClientUtil.getString("accessToken_" + this.appId);
    }

    private void setTokenToRedis(AccessToken token) {
        RedisClientUtil.setString(("accessToken_" + this.appId), gson.toJson(token));
    }

    public String refreshTicket(String token) {
        String url_getticket = WeixinHttpURLSet.GET_TICKET_BY_ACCESSTOKEN
                .replace("ACCESS_TOKEN", token);
//		JSONObject ticketObj = HttpRequestUtil.httpsGETRequest(url_getticket);
        BusinessResponse<Object> response = NetUtil.getInstance().get(url_getticket, MediaType.APPLICATION_JSON);
        String jsapi_ticket = "";
        if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
            Map<String, String> data = gson.fromJson(response.getData().toString(), new TypeToken<Map<String, String>>() {
            }.getType());
            if ("0".equals(data.get("errcode"))) {
                jsapi_ticket = data.get("ticket");
            }
            logger.info("WeixinUtilImpl.refreshTicket The refresh Ticket={}", jsapi_ticket);
            RedisClientUtil.setString("jsapiTicket", jsapi_ticket);
        }
        return jsapi_ticket;
    }

    public String getTicket() {
        this.getAccessToken();
        String jsapi_ticket = RedisClientUtil.getString("jsapiTicket");
        return jsapi_ticket;
    }

    public String getWebAuthAccessTokenFromCache(String openid) {
        String authAccTokenKey = "AUTH_ACCESS_TOKEN_" + openid;
        String auth_access_token = RedisClientUtil.getString(authAccTokenKey);
        if (StringUtils.isEmpty(auth_access_token)) {  //access_token已过期 需要用refresh_token刷新
            auth_access_token = refreshAuthAccessToken(openid);
        }
        return auth_access_token;
    }

    private void setWebAuthAccessTokenToCache(String openid, String access_token, String refresh_token) {
        String authAccTokenKey = "AUTH_ACCESS_TOKEN_" + openid;
        String authRefTokenKey = "AUTH_REFRESH_TOKEN_" + openid;
        RedisClientUtil.setString(authAccTokenKey, access_token, 2 * 60 * 55);// 110分钟
        RedisClientUtil.setString(authRefTokenKey, refresh_token, 25 * 24 * 60 * 60);//refreshToken 25天
    }

    public Map<String, Object> getBaseUserInfoByAuth(String auth_access_token, String openid) {
        String url = WeixinHttpURLSet.GET_USERINFO_BY_AUTHTOKEN.replace("ACCESS_TOKEN", auth_access_token).replace("OPENID", openid);
        Map<String, Object> data = null;
        BusinessResponse<Object> response = NetUtil.getInstance().get(url, MediaType.APPLICATION_JSON);
        if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
            data = gson.fromJson(response.getData().toString(), new TypeToken<Map<String, Object>>() {
            }.getType());
            //token已失效
            if (data.get("errcode") != null && ((String) data.get("errcode")).startsWith("400")) {
                refreshAuthAccessToken(openid);
                return null;
            }
        }
        return data;
    }

    private String refreshAuthAccessToken(String openid) {

        String authRefTokenKey = "AUTH_REFRESH_TOKEN_" + openid;
        String refreshToken = RedisClientUtil.getString(authRefTokenKey);
        if (StringUtils.isEmpty(refreshToken)) {
            return null;
        }
        String url = WeixinHttpURLSet.GET_ACCESSTOKEN_BY_REFRESHTOKEN.replace("APPID", this.appId).replace("REFRESH_TOKEN", refreshToken);
        BusinessResponse<Object> response = NetUtil.getInstance().get(url, MediaType.APPLICATION_JSON);
        if (response.getRt_code() == BusinessResponse.RESPONSE_OK) {
            Map<String, String> data = gson.fromJson(response.getData().toString(), new TypeToken<Map<String, String>>() {
            }.getType());
            String access_token = data.get("access_token");
            String refresh_token = data.get("refresh_token");
            if (StringUtils.isNotEmpty(access_token) && StringUtils.isNotEmpty(refresh_token)) {
                setWebAuthAccessTokenToCache(openid, access_token, refresh_token);
                return access_token;
            }
            return null;
        }
        return null;
    }

}
