package com.freebird.wx.common.wx;

import java.io.Serializable;

/**
 * 网页授权access_token
 * 非微信接口调用凭证access_token
 *
 * @author zhangyaping001
 */
public class WebAuthAccessToken implements Serializable {

    private static final long serialVersionUID = 1L;
    private String access_token;
    private Long expires_in;
    private String refresh_token;
    private String openid;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "WebAuthAccessToken [access_token=" + access_token
                + ", expires_in=" + expires_in + ", refresh_token="
                + refresh_token + ", openid=" + openid + ", scope=" + scope
                + "]";
    }
}
